(ns webchange.lesson-builder.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.flipbook :as flipbook-utils]))

(def path-to-db :lesson-builder/flipbook)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current stage

(def current-stage-key :current-stage)

(defn- get-current-stage
  [db]
  (get db current-stage-key 0))

(defn- set-current-stage
  [db value]
  (assoc db current-stage-key value))

(re-frame/reg-sub
  ::current-stage
  :<- [path-to-db]
  #(get-current-stage %))

;; show generated pages?

(def show-generated-pages-key :show-generated-pages?)

(defn- get-show-generated-pages
  [db]
  (get db show-generated-pages-key false))

(re-frame/reg-sub
  ::show-generated-pages?
  :<- [path-to-db]
  get-show-generated-pages)

(re-frame/reg-event-fx
  ::set-show-generated-pages
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db show-generated-pages-key value)}))

;; stages sequence

(re-frame/reg-sub
  ::activity-stages
  :<- [::state/activity-data]
  :<- [::current-stage]
  (fn [[activity-data current-stage-idx]]
    (let [page-idx->data (fn [page-idx]
                           (when (some? page-idx)
                             (let [page-data (flipbook-utils/get-page-data activity-data page-idx)]
                               (merge page-data
                                      {:idx   page-idx
                                       :title (str "Page " page-idx)}))))]
      (->> (flipbook-utils/get-stages-data activity-data)
           (map (fn [{:keys [idx name pages-idx]}]
                  {:id             (->> pages-idx
                                        (map #(or % "*"))
                                        (clojure.string/join "-")
                                        (str idx "-"))
                   :idx            idx
                   :title          name
                   :current-stage? (= idx current-stage-idx)
                   :left-page      (-> pages-idx first page-idx->data)
                   :right-page     (-> pages-idx last page-idx->data)}))))))

(re-frame/reg-sub
  ::activity-stages-filtered
  :<- [::activity-stages]
  :<- [::show-generated-pages?]
  (fn [[stages show-generated-pages?]]
    (->> stages
         (map (fn [{:keys [] :as stage-data}]
                (let [filter-generated-page (fn [{:keys [generated?] :as page-data} show-generated?]
                                              (when (or show-generated? (not generated?))
                                                page-data))]
                  (-> stage-data
                      (update :left-page filter-generated-page show-generated-pages?)
                      (update :right-page filter-generated-page show-generated-pages?)))))
         (filter (fn [{:keys [left-page right-page]}]
                   (or (some? left-page)
                       (some? right-page)))))))


;; stage selector

(re-frame/reg-event-fx
  ::show-flipbook-stage
  [(re-frame/inject-cofx :activity-data)
   (re-frame/inject-cofx :registered-transitions)
   (i/path path-to-db)]
  (fn [{:keys [activity-data transitions db]} [_ stage-idx]]
    (print "show-stage" stage-idx)
    (let [show-generated-pages? (get-show-generated-pages db)
          book-name (flipbook-utils/get-book-object-name activity-data)
          component-wrapper (get transitions book-name)]
      (when (some? component-wrapper)
        {:db                   (set-current-stage db stage-idx)
         :flipbook-show-spread {:component-wrapper     @component-wrapper
                                :spread-idx            stage-idx
                                :hide-generated-pages? (not show-generated-pages?)}}))))

(re-frame/reg-fx
  :flipbook-show-spread
  (fn [{:keys [component-wrapper spread-idx hide-generated-pages?]}]
    ((:show-spread component-wrapper) spread-idx {:hide-generated-pages? hide-generated-pages?})))

(re-frame/reg-event-fx
  ::show-next-stage
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [next-stage-idx (-> db get-current-stage inc)]
      {:dispatch [::show-flipbook-stage next-stage-idx]})))

(re-frame/reg-event-fx
  ::show-prev-stage
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [prev-stage-idx (-> db get-current-stage dec)]
      {:dispatch [::show-flipbook-stage prev-stage-idx]})))
