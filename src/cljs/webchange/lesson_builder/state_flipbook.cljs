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

;; stages sequence

(defn- get-activity-stages
  [activity-data current-stage-idx]
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
                 :right-page     (-> pages-idx last page-idx->data)})))))

(re-frame/reg-sub
  ::activity-stages
  :<- [::state/activity-data]
  :<- [::current-stage]
  (fn [[activity-data current-stage-idx]]
    (get-activity-stages activity-data current-stage-idx)))

(defn- get-activity-stages-filtered
  [activity-data current-stage-idx show-generated-pages?]
  (->> (get-activity-stages activity-data current-stage-idx)
       (map (fn [{:keys [] :as stage-data}]
              (let [filter-generated-page (fn [{:keys [generated?] :as page-data} show-generated?]
                                            (when (or show-generated? (not generated?))
                                              page-data))]
                (-> stage-data
                    (update :left-page filter-generated-page show-generated-pages?)
                    (update :right-page filter-generated-page show-generated-pages?)))))
       (filter (fn [{:keys [left-page right-page]}]
                 (or (some? left-page)
                     (some? right-page))))))

(re-frame/reg-sub
  ::activity-stages-filtered
  :<- [::state/activity-data]
  :<- [::current-stage]
  :<- [::show-generated-pages?]
  (fn [[activity-data current-stage-idx show-generated-pages?]]
    (get-activity-stages-filtered activity-data current-stage-idx show-generated-pages?)))

;; show generated pages?

(def show-generated-pages-key :show-generated-pages?)

(defn- get-show-generated-pages
  [db]
  (get db show-generated-pages-key false))

(re-frame/reg-sub
  ::show-generated-pages?
  :<- [path-to-db]
  get-show-generated-pages)

(defn- get-closest-available-stage-idx
  [current-idx available-idx]
  (->> available-idx
       (reduce (fn [{:keys [distance] :as result} stage-idx]
                 (let [current-distance (- current-idx stage-idx)]
                   (if (and (>= current-idx stage-idx)
                            (< current-distance distance))
                     {:idx      stage-idx
                      :distance current-distance}
                     result)))
               {:idx      0
                :distance ##Inf})
       (:idx)))

(re-frame/reg-event-fx
  ::set-show-generated-pages
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ value]]
    (let [current-stage-idx (get-current-stage db)
          stages-idx (->> (get-activity-stages-filtered activity-data current-stage-idx value)
                          (map :idx))
          closest-available-idx (get-closest-available-stage-idx current-stage-idx stages-idx)]
      (cond-> {:db (assoc db show-generated-pages-key value)}
              (not= current-stage-idx closest-available-idx) (assoc :dispatch [::show-flipbook-stage closest-available-idx])))))

;; stage selector

(re-frame/reg-event-fx
  ::show-flipbook-stage
  [(re-frame/inject-cofx :activity-data)
   (re-frame/inject-cofx :registered-transitions)
   (i/path path-to-db)]
  (fn [{:keys [activity-data transitions db]} [_ stage-idx]]
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

(defn- prepare-get-stage-data
  "Should be passed 'db' and 'activity-data' or 'stages' and 'current-stage-idx'"
  [{:keys [db activity-data stages current-stage-idx]}]
  (if (and (some? stages)
           (some? current-stage-idx))
    {:stages            stages
     :current-stage-idx current-stage-idx}
    (let [current-stage-idx (get-current-stage db)
          show-generated-pages? (get-show-generated-pages db)
          stages (get-activity-stages-filtered activity-data current-stage-idx show-generated-pages?)]
      {:stages            stages
       :current-stage-idx current-stage-idx})))

(defn- get-next-stage-idx
  [params]
  (let [{:keys [stages current-stage-idx]} (prepare-get-stage-data params)]
    (->> stages
         (map-indexed vector)
         (some (fn [[seq-idx stage]]
                 (let [next-stage (nth stages (inc seq-idx) nil)]
                   (and (= (:idx stage) current-stage-idx)
                        (:idx next-stage))))))))

(defn- get-prev-stage-idx
  [params]
  (let [{:keys [stages current-stage-idx]} (prepare-get-stage-data params)]
    (->> stages
         (map-indexed vector)
         (some (fn [[seq-idx stage]]
                 (let [prev-stage (nth stages (dec seq-idx) nil)]
                   (and (= (:idx stage) current-stage-idx)
                        (:idx prev-stage))))))))

(re-frame/reg-event-fx
  ::show-next-stage
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_]]
    (if-let [next-stage-idx (get-next-stage-idx {:db            db
                                                 :activity-data activity-data})]
      {:dispatch [::show-flipbook-stage next-stage-idx]})))

(re-frame/reg-sub
  ::has-next-stage?
  :<- [::activity-stages-filtered]
  :<- [::current-stage]
  (fn [[stages current-stage-idx]]
    (-> (get-next-stage-idx {:stages            stages
                             :current-stage-idx current-stage-idx})
        (some?))))

(re-frame/reg-event-fx
  ::show-prev-stage
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_]]
    (if-let [prev-stage-idx (get-prev-stage-idx {:db            db
                                                 :activity-data activity-data})]
      {:dispatch [::show-flipbook-stage prev-stage-idx]})))

(re-frame/reg-sub
  ::has-prev-stage?
  :<- [::activity-stages-filtered]
  :<- [::current-stage]
  (fn [[stages current-stage-idx]]
    (-> (get-prev-stage-idx {:stages            stages
                             :current-stage-idx current-stage-idx})
        (some?))))
