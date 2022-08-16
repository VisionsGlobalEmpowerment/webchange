(ns webchange.lesson-builder.tools.question-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.lesson-builder.blocks.menu.state :as menu]
    [webchange.lesson-builder.blocks.stage.second-stage.state :as second-stage]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.question.preview :refer [get-scene-data]]
    [webchange.question.get-question-data :refer [current-question-version default-question-data]]
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

(defn update-form-data
  [db key data-patch]
  (if (map? data-patch)
    (update-in db [form-data-key key] merge data-patch)
    (assoc-in db [form-data-key key] data-patch)))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

(re-frame/reg-sub
  ::form-field
  :<- [::form-data]
  (fn [form-data [_ field-name]]
    (get form-data field-name "")))

(re-frame/reg-event-fx
  ::set-form-field
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ field-name value]]
    {:db (update-form-data db field-name value)}))

;; current object

(def current-object-key :current-object)

(defn- get-current-object
  [db]
  (get db current-object-key))

(defn- set-current-object
  [db value]
  (assoc db current-object-key value))

(re-frame/reg-sub
  ::current-object
  :<- [path-to-db]
  get-current-object)

;;

(def question-info-key :question-info)

(defn- get-question-info
  [db]
  (get db question-info-key))

(defn- set-question-info
  [db value]
  (assoc db question-info-key value))

(re-frame/reg-sub
  ::question-info
  :<- [path-to-db]
  #(get-question-info %))

;; events

(re-frame/reg-event-fx
  ::init-state
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ {:keys [question-id]}]]
    (editor-state/register-select-object-handler "second" :show-question-object-form [::show-object-form])
    (let [{:keys [index params]} (get-in activity-data [:objects (keyword question-id) :metadata])]
      {:db       (-> db
                     (set-form-data (merge default-question-data params))
                     (set-question-info {:question-index index}))
       :dispatch [::second-stage/init ::activity-data]})))

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [db]} [_]]
    {:db       (dissoc db path-to-db)
     :dispatch [::second-stage/reset]}))

(re-frame/reg-event-fx
  ::show-object-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ option-object-name]]
    {:db       (set-current-object db option-object-name)
     :dispatch [::menu/open-component :question-option]}))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [_]} [_]]
    {:dispatch [::menu/history-back]}))

;; save

(def saving-key :saving?)

(defn- set-saving
  [db value]
  (assoc db saving-key value))

(re-frame/reg-sub
  ::saving?
  :<- [path-to-db]
  #(get % saving-key false))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ action]]
    (let [form-data (get-form-data db)
          {:keys [question-index]} (get-question-info db)]
      {:db       (set-saving db true)
       :dispatch (case action
                   :edit [::stage-actions/call-activity-action
                          {:action         :edit-question
                           :data           {:question-page-object form-data
                                            :question-index       question-index
                                            :data-version         current-question-version}
                           :common-action? true}
                          {:on-success [::save-success]
                           :on-failure [::save-failure]}]
                   :add [::stage-actions/call-activity-action
                         {:action         :add-question
                          :data           {:question-page-object form-data
                                           :data-version         current-question-version}
                          :common-action? true}
                         {:on-success [::save-success]
                          :on-failure [::save-failure]}])})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-saving db false)
     :dispatch [::menu/history-back]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-saving db false)}))

;; scene data

(re-frame/reg-sub
  ::activity-data
  :<- [::state/activity-data]
  :<- [::form-data]
  (fn [[activity-data form-data]]
    (let [[_ current-background] (get-scene-background activity-data)]
      (get-scene-data form-data {:background current-background}))))
