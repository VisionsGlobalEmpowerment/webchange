(ns webchange.lesson-builder.tools.question-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.blocks.stage.second-stage.state :as second-stage]
    [webchange.question.preview :refer [get-scene-data]]
    [webchange.utils.scene-data :refer [get-scene-background]]))

(def path-to-db :lesson-builder/question-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; form data

(def form-data-key :form-data)

(defn- get-form-data
  [db]
  (get db form-data-key))

(defn- set-form-data
  [db value]
  (assoc db form-data-key value))

(defn- update-form-data
  [db data-patch]
  (update db form-data-key merge data-patch))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

;; question-type

(def question-type-key :question-type)

(re-frame/reg-sub
  ::question-type
  :<- [::form-data]
  #(get % question-type-key))

(re-frame/reg-sub
  ::question-type-options
  (fn []
    [{:text  "Multiple choice image"
      :value "multiple-choice-image"}
     {:text  "Multiple choice text"
      :value "multiple-choice-text"}
     {:text  "Thumbs up & thumbs down"
      :value "thumbs-up-n-down"}]))

(re-frame/reg-event-fx
  ::set-question-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (update-form-data db {question-type-key value})}))

;; events

(re-frame/reg-event-fx
  ::init-state
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ {:keys [question-id]}]]
    (let [question-params (get-in activity-data [:objects (keyword question-id) :metadata :params])]
      {:db       (set-form-data db question-params)
       :dispatch [::second-stage/init ::scene-data]})))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [db]} [_]]
    {:db         (dissoc db path-to-db)
     :dispatch-n [[::second-stage/reset]]}))

;; scene data

(re-frame/reg-sub
  ::scene-data
  :<- [::state/activity-data]
  :<- [::form-data]
  (fn [[activity-data form-data]]
    (let [[_ current-background] (get-scene-background activity-data)]
      (get-scene-data form-data {:background current-background}))))
