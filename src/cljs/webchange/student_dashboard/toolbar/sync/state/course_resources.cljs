(ns webchange.student-dashboard.toolbar.sync.state.course-resources
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [clojure.set :refer [difference]]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.db :as db]
    [webchange.sw-utils.state.resources :as sw-resources]))

(defn- path-to-db
  [relative-path]
  (->> relative-path
       (concat [:course-resources])
       (db/path-to-db)))

(defn- contains-in?
  [sub-list super-list]
  (-> (difference (set sub-list)
                  (set super-list))
      (empty?)))

(defn- course-resources
  [db]
  (let [course-lessons (get-in db (path-to-db [:data]) {})
        synced-resources (sw-resources/synced-game-resources db)
        synced-endpoints (sw-resources/synced-game-endpoints db)]
    (->> course-lessons
         (map (fn [[lesson-id {:keys [resources endpoints] :as lesson}]]
                [lesson-id (assoc lesson :selected? (and (contains-in? resources synced-resources)
                                                         (contains-in? endpoints synced-endpoints)))]))
         (into {}))))

(re-frame/reg-sub
  ::course-resources
  course-resources)

(re-frame/reg-sub
  ::course-resources-list
  (fn []
    [(re-frame/subscribe [::course-resources])])
  (fn [[resources-data]]
    (->> resources-data
         (map (fn [[id data]] (assoc data :id id)))
         (sort-by :id))))

(defn- load-status
  [db]
  (get-in db (path-to-db [:load-status]) :not-loaded))

(re-frame/reg-sub
  ::load-status
  load-status)

(re-frame/reg-sub
  ::loading?
  (fn [db]
    (->> (load-status db)
         (= :loading))))

(re-frame/reg-event-fx
  ::set-load-status
  (fn [{:keys [db]} [_ status]]
    {:db (assoc-in db (path-to-db [:load-status]) status)}))

(re-frame/reg-event-fx
  ::load-course-resources
  (fn [{:keys [db]} _]
    (let [status (load-status db)
          current-course (:current-course db)]
      (when (or (= status :not-loaded)
                (= status :failed))
        {:dispatch   [::set-load-status :loading]
         :http-xhrio {:method          :get
                      :uri             (str "/api/resources/game-app/" current-course "/scenes")
                      :format          (json-request-format)
                      :response-format (json-response-format {:keywords? true})
                      :on-success      [::load-course-resources-success]
                      :on-failure      [::load-course-resources-failed]}}))))

(defn- generate-lesson-id
  [{:keys [level-number lesson-number]}]
  (+ (* level-number 1000) lesson-number))

(re-frame/reg-event-fx
  ::load-course-resources-success
  (fn [{:keys [db]} [_ response]]
    (let [data (->> response
                    (map (fn [lesson] [(generate-lesson-id lesson) lesson]))
                    (into {}))]
      {:db       (assoc-in db (path-to-db [:data]) data)
       :dispatch [::set-load-status :loaded]})))

(re-frame/reg-event-fx
  ::load-course-resources-failed
  (fn [{:keys [db]} [_ _]]
    {:db       (assoc-in db (path-to-db [:loading]) false)
     :dispatch [::set-load-status :failed]}))

(defn- get-lists-diff
  [list-1 list-2]
  (-> (difference (set list-1)
                  (set list-2))
      (vec)))

(defn- get-selected-data
  [lessons key]
  (->> lessons
       (map second)
       (filter :selected?)
       (map key)
       (flatten)
       (distinct)))

(defn- get-selected-resources
  [lessons]
  (get-selected-data lessons :resources))

(defn- get-selected-endpoints
  [lessons]
  (get-selected-data lessons :endpoints))

(re-frame/reg-event-fx
  ::switch-lesson-resources
  (fn [{:keys [db]} [_ lesson-id]]
    (let [all-lessons (course-resources db)
          current-resources-list (get-selected-resources all-lessons)
          current-endpoints-list (get-selected-endpoints all-lessons)

          all-lessons-updated (update-in all-lessons [lesson-id :selected?] not)
          updated-resources-list (get-selected-resources all-lessons-updated)
          updated-endpoints-list (get-selected-endpoints all-lessons-updated)

          action (if (get-in all-lessons [lesson-id :selected?]) :remove :add)]
      {:dispatch (if (= action :add)
                   [::sw-resources/add-game-data {:resources (get-lists-diff updated-resources-list current-resources-list)
                                                  :endpoints (get-lists-diff updated-endpoints-list current-endpoints-list)}]
                   [::sw-resources/remove-game-data {:resources (get-lists-diff current-resources-list updated-resources-list)
                                                     :endpoints (get-lists-diff current-endpoints-list updated-endpoints-list)}])})))
