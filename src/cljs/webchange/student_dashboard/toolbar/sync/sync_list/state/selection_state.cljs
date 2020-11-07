(ns webchange.student-dashboard.toolbar.sync.sync-list.state.selection-state
  (:require
    [clojure.set :refer [superset?]]
    [re-frame.core :as re-frame]
    [webchange.sw-utils.state.resources :as sw]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.db :as db]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.lessons-resources :as lessons-resources]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:user-selection])
       (db/path-to-db)))

;; User manual selection

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db []) {})}))

(defn- user-selection
  [db]
  (get-in db (path-to-db [])))

(re-frame/reg-sub ::user-selection user-selection)

(re-frame/reg-event-fx
  ::select
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db (path-to-db [id]) true)}))

(re-frame/reg-event-fx
  ::deselect
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db (path-to-db [id]) false)}))

;; Subscriptions

(defn- lesson-cached?
  [lesson cached-resources]
  (and (superset? (set (:resources cached-resources))
                  (set (:resources lesson)))
       (superset? (set (:endpoints cached-resources))
                  (set (:endpoints lesson)))))

(re-frame/reg-sub
  ::loading
  (fn []
    [(re-frame/subscribe [::lessons-resources/loading])
     (re-frame/subscribe [::sw/state-loading])])
  (fn [[resources-loading state-loading]]
    (or resources-loading state-loading)))

(re-frame/reg-sub
  ::lessons
  (fn []
    [(re-frame/subscribe [::lessons-resources/lessons-list])
     (re-frame/subscribe [::user-selection])
     (re-frame/subscribe [::sw/cached-resources])])
  (fn [[lessons user-selection cached-resources]]
    (map (fn [{:keys [id name] :as lesson}]
           (let [selected? (if (contains? user-selection id)
                             (get user-selection id)
                             (lesson-cached? lesson cached-resources))]
             {:id        id
              :name      name
              :selected? selected?}))
         lessons)))

(defn- flatten-resources
  [data]
  (let [f (fn [data] (->> data (flatten) (distinct) (remove empty?)))]
    (-> data
        (update :resources f)
        (update :endpoints f))))

(defn- get-cached-lessons
  [db]
  (let [lessons (lessons-resources/lessons-list db)
        cached-resources (sw/cached-resources db)]
    (reduce (fn [result {:keys [id] :as lesson}]
              (if (lesson-cached? lesson cached-resources)
                (assoc result id true)
                result))
            {}
            lessons)))

(defn selected-resources
  [db]
  (let [selection (reduce (fn [result [id selected?]]
                            (if selected?
                              (assoc result id true)
                              (dissoc result id)))
                          (get-cached-lessons db)
                          (user-selection db))
        lessons (lessons-resources/lessons-data db)]
    (->> selection
         (reduce (fn [result [id _]]
                   (-> result
                       (update :resources concat (get-in lessons [id :resources]))
                       (update :endpoints concat (get-in lessons [id :endpoints]))))
                 {:resources []
                  :endpoints []})
         (flatten-resources))))
