(ns webchange.student-dashboard.toolbar.sync.state.course-resources
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [day8.re-frame.http-fx]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.db :as db]
    [webchange.sw-utils.state.resources :as sw]
    [webchange.config :refer [api-url]]))

(defn- path-to-db
  [relative-path]
  (->> relative-path
       (concat [:course-resources])
       (db/path-to-db)))

(def course-lessons-path (path-to-db [:course-lessons]))
(def loading-status-path (path-to-db [:loading-status]))
(def selected-lessons-path (path-to-db [:selected-lessons]))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::reset-selected-lessons]
                       [::load-course-lessons])}))

(defn- load-status
  [db]
  (get-in db loading-status-path :not-loaded))

(re-frame/reg-sub ::loading-status load-status)
(re-frame/reg-sub ::loading? (fn [db] (->> (load-status db) (= :loading))))
(re-frame/reg-event-fx
  ::set-loading-status
  (fn [{:keys [db]} [_ status]]
    {:db (assoc-in db loading-status-path status)}))

(defn course-lessons [db] (get-in db course-lessons-path []))
(re-frame/reg-sub ::course-lessons course-lessons)

(defn selected-lessons [db] (get-in db selected-lessons-path {}))
(re-frame/reg-sub ::selected-lessons selected-lessons)
(re-frame/reg-event-fx
  ::set-selected-lessons
  (fn [{:keys [db]} [_ id value]]
    {:db (assoc-in db (concat selected-lessons-path [id]) value)}))
(re-frame/reg-event-fx
  ::remove-selected-lessons
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db selected-lessons-path dissoc id)}))
(re-frame/reg-event-fx
  ::reset-selected-lessons
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db selected-lessons-path {})}))

(defn sync-list->data
  [sync-list]
  (reduce (fn [result [lesson-id selected?]]
            (update result (if selected? :add :remove) conj lesson-id))
          {}
          sync-list))

(re-frame/reg-event-fx
  ::save-sync-list
  (fn [{:keys [db]} [_]]
    (let [sync-data (-> db selected-lessons sync-list->data)]
      {:dispatch [::sw/cache-lessons sync-data
                  ::reset-selected-lessons]})))

(re-frame/reg-sub
  ::course-lessons-list
  (fn []
    [(re-frame/subscribe [::course-lessons])
     (re-frame/subscribe [::selected-lessons])
     (re-frame/subscribe [::sw/cached-lessons])])
  (fn [[lessons-data selected-lessons cached-lessons]]
    (->> lessons-data
         (reduce (fn [lessons-list [lesson-id lesson-data]]
                   (let [cached? (->> cached-lessons (some #{lesson-id}) (boolean))
                         currently-selected? (->> lesson-id (get selected-lessons) (= true))
                         rejected? (->> lesson-id (get selected-lessons) (= false))]
                     (conj lessons-list (assoc lesson-data :selected? (and (or cached? currently-selected?)
                                                                           (not rejected?))))))
                 [])
         (sort-by :id))))

(re-frame/reg-event-fx
  ::select-lesson
  (fn [{:keys [db]} [_ id value]]
    (let [current-selected-lessons (selected-lessons db)]
      (if (contains? current-selected-lessons id)
        {:dispatch [::remove-selected-lessons id]}
        {:dispatch [::set-selected-lessons id value]}))))

(re-frame/reg-event-fx
  ::load-course-lessons
  (fn [{:keys [db]} _]
    (let [status (load-status db)
          current-course (:current-course db)]
      (when (not= status :loading)
        {:dispatch   [::set-loading-status :loading]
         :http-xhrio {:method          :get
                      :uri             (api-url "/resources/game-app/" current-course "/lessons")
                      :format          (json-request-format)
                      :response-format (json-response-format {:keywords? true})
                      :on-success      [::load-course-lessons-success]
                      :on-failure      [::load-course-lessons-failed]}}))))

(defn- lessons-list->map
  [lessons-list]
  (reduce (fn [result {:keys [id] :as lesson}]
            (assoc result id lesson))
          {}
          lessons-list))

(re-frame/reg-event-fx
  ::load-course-lessons-success
  (fn [{:keys [db]} [_ response]]
    {:db       (assoc-in db course-lessons-path (lessons-list->map response))
     :dispatch [::set-loading-status :loaded]}))

(re-frame/reg-event-fx
  ::load-course-lessons-failed
  (fn [_ [_ _]]
    {:dispatch [::set-loading-status :failed]}))
