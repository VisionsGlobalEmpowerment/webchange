(ns webchange.lesson-builder.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.lesson-builder.state :as state]
    [webchange.state.state :as core-state]
    [webchange.utils.flipbook :as flipbook-utils]
    [webchange.lesson-builder.tools.image-add.state :as image-add-state]))

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
                                    {:idx     page-idx
                                     :title   (str "Page " page-idx)
                                     :preview (get-in page-data [:preview :url])}))))]
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
                     (some? right-page))))
       (vec)))

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

(re-frame/reg-event-fx
  ::set-show-generated-pages
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc db show-generated-pages-key value)
     :dispatch [::update-current-flipbook-stage]}))

;; stage selector

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
  ::update-current-flipbook-stage
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ {:keys [force?] :or {force? false}}]]
    (let [current-stage-idx (get-current-stage db)
          show-generated-pages? (get-show-generated-pages db)
          stages-idx (->> (get-activity-stages-filtered activity-data current-stage-idx show-generated-pages?)
                          (map :idx))
          closest-available-idx (get-closest-available-stage-idx current-stage-idx stages-idx)]
      (when (or (not= current-stage-idx closest-available-idx)
                force?)
        {:dispatch [::show-flipbook-stage closest-available-idx]}))))

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

(re-frame/reg-event-fx
  ::update-pages-preview
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ page-screenshots]]
    (let [book-name :book
          pages (->> (flipbook-utils/get-pages-data activity-data)
                     (map-indexed (fn [idx page-data]
                                    (cond-> page-data
                                            (contains? page-screenshots idx) (assoc :preview (get page-screenshots idx))))))]
      pages
      {:dispatch [::state/set-activity-data (assoc-in activity-data [:objects book-name :pages] pages)]})))

;; remove page

(re-frame/reg-event-fx
  ::remove-page
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ page-idx]]
    (let [spread-id (-> (flipbook-utils/get-page-data activity-data page-idx)
                        (get :spread-id))
          pages-to-remove (if (some? spread-id)
                            (->> (flipbook-utils/get-pages-data activity-data #(= spread-id (:spread-id %)))
                                 (map :idx)
                                 (sort)
                                 (reverse))                 ;; delete in index descending order
                            [page-idx])]
      {:dispatch-n [[::stage-state/set-stage-busy true]
                    [::remove-pages-sequence pages-to-remove]]})))

(re-frame/reg-event-fx
  ::remove-pages-sequence
  (fn [{:keys []} [_ [page-idx & rest-pages]]]
    {:dispatch [::state/call-activity-action
                {:action         "remove-page"
                 :data           {:page-number page-idx}
                 :common-action? false}
                {:on-success (if (empty? rest-pages)
                               [::remove-page-success]
                               [::remove-pages-sequence rest-pages])}]}))

(re-frame/reg-event-fx
  ::remove-page-success
  (fn [{:keys [_]} [_ {:keys [data]}]]
    {:dispatch-n [[::stage-state/set-stage-busy false]
                  [::stage-state/reset]                     ;; reset stage to update flipbook instance in interpreter
                  [::state/set-activity-data data]
                  [::update-current-flipbook-stage {:force? true}]]}))

(re-frame/reg-event-fx
  ::remove-page-failure
  (fn [{:keys [_]} [_]]
    {:dispatch [::stage-state/set-stage-busy false]}))

(re-frame/reg-event-fx
  ::add-page
  (fn [{:keys [_]} [_ page-data {:keys [on-success on-failure]}]]
    {:dispatch-n [[::stage-state/set-stage-busy true]
                  [::state/call-activity-action
                   (merge (if (some? page-data)
                            {:action "add-page"
                             :data   page-data}
                            {:action "add-empty-page"})
                          {:common-action? false})
                   {:on-success [::add-page-success on-success]
                    :on-failure [::add-page-failure on-failure]}]]}))

(re-frame/reg-event-fx
  ::add-page-success
  (fn [{:keys [_]} [_ on-success {:keys [data]}]]
    {:dispatch-n (cond-> [[::stage-state/set-stage-busy false]
                          [::state/set-activity-data data]
                          [::stage-state/reset]]            ;; reset stage to update flipbook instance in interpreter
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::add-page-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))

;; move page
(re-frame/reg-event-fx
  ::move-page
  (fn [{:keys [_]} [_ {:keys [from to]} {:keys [on-success on-failure]}]]
    {:dispatch-n [[::stage-state/set-stage-busy true]
                  [::state/call-activity-action
                   {:action "move-page"
                    :data   {:page-idx-from from
                             :page-idx-to to}
                    :common-action? false}
                   {:on-success [::move-page-success on-success]
                    :on-failure [::move-page-failure on-failure]}]]}))

(re-frame/reg-event-fx
  ::move-page-success
  (fn [{:keys [_]} [_ on-success {:keys [data]}]]
    {:dispatch-n (cond-> [[::stage-state/set-stage-busy false]
                          [::state/set-activity-data data]
                          [::stage-state/reset]]            ;; reset stage to update flipbook instance in interpreter
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::move-page-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))

;; db should be global to retrieve current-object
(re-frame/reg-event-fx
  ::add-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [db activity-data]} [_ {:keys [on-success on-failure]}]]
    (let [current-object (core-state/get-current-object db)
          page-number (flipbook-utils/page-object-name->page-number activity-data current-object)]
      {:dispatch-n [[::stage-state/set-stage-busy true]
                    [::state/call-activity-action
                     {:common-action? false
                      :action "add-text"
                      :data {:page-number page-number}}
                     {:on-success [::add-text-success on-success]
                      :on-failure [::add-text-failure on-failure]}]]})))

(re-frame/reg-event-fx
  ::add-text-success
  (fn [{:keys [_]} [_ on-success {:keys [data]}]]
    {:dispatch-n (cond-> [[::stage-state/set-stage-busy false]
                          [::state/set-activity-data data]
                          [::stage-state/reset]]            ;; reset stage to update flipbook instance in interpreter
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::add-text-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))

;; db should be global to retrieve current-object
(re-frame/reg-event-fx
  ::open-add-image
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [db activity-data]} [_]]
    (let [current-object (core-state/get-current-object db)
          page-number (flipbook-utils/page-object-name->page-number activity-data current-object)]
      {:dispatch-n [[::image-add-state/set-page-number page-number]
                    [:layout/open-tool :image-add]]})))
