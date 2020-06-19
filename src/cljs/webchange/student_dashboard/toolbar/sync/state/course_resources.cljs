(ns webchange.student-dashboard.toolbar.sync.state.course-resources
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [clojure.set :refer [difference]]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.db :as db]))

(defn- path-to-db
  [relative-path]
  (->> relative-path
       (concat [:course-resources])
       (db/path-to-db)))

(defn- course-resources
  [db]
  (get-in db (path-to-db [:data]) []))

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

(re-frame/reg-event-fx
  ::load-course-resources-success
  (fn [{:keys [db]} [_ response]]
    (let [data (reduce (fn [result {:keys [level-number lesson-number] :as lesson}]
                         (let [lesson-id (+ (* level-number 1000) lesson-number)]
                           (assoc result lesson-id (-> lesson
                                                       (assoc :selected? false)))))
                       {}
                       response)]
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

          action (if (get-in all-lessons [lesson-id :selected?]) :remove :add)
          diff (if (= action :add)
                 {:resources (get-lists-diff updated-resources-list current-resources-list)
                  :endpoints (get-lists-diff updated-endpoints-list current-endpoints-list)}
                 {:resources (get-lists-diff current-resources-list updated-resources-list)
                  :endpoints (get-lists-diff current-endpoints-list updated-endpoints-list)})]

      (println "::switch-lesson-resources" action (count (:resources diff)) (count (:endpoints diff)))
      (println "current" (count current-resources-list) (count current-endpoints-list))
      (println "updated" (count updated-resources-list) (count updated-endpoints-list))

      {:db       (assoc-in db (path-to-db [:data]) all-lessons-updated)
       :dispatch []})))

;[webchange.sw-utils.message :as sw]
;(sw/get-cached-resources "course")
;(sw/set-cached-scenes {:course    "course"
;                       :scenes    {:add []}
;                       :resources {:add    []
;                                   :remove []}})

