(ns webchange.editor-v2.activity-form.generic.components.question.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]
    [webchange.state.state-activity :as state-activity]
    [webchange.question.get-question-data :refer [current-question-version default-question-data form->question-data]]

    [webchange.question.create :as question]
    [webchange.utils.scene-data :as scene-utils]
    ))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :add-question-modal])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(defn- get-state
  [db]
  (get-in db modal-state-path))

(defn- get-question-index
  [db]
  (-> (get-state db)
      (get :question-index)))

(re-frame/reg-sub
  ::state
  get-state)

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_ {:keys [action title save-button-text question-index]}]]
    {:db (assoc-in db modal-state-path {:open?            true
                                        :action           action
                                        :question-index   question-index
                                        :title            title
                                        :save-button-text save-button-text})}))

(re-frame/reg-event-fx
  ::open-add-question-window
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::open {:action           "add"
                           :title            "Add Question"
                           :save-button-text "Add"}]
                  [::set-form-data default-question-data]]}))

(re-frame/reg-event-fx
  ::open-edit-question-window
  (fn [{:keys [_]} [_ question-data]]
    (let [{:keys [index params]} (get question-data :metadata)]
      {:dispatch-n [[::open {:action           "edit"
                             :question-index   index
                             :title            "Edit Question"
                             :save-button-text "Save"}]
                    [::set-form-data params]]})))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path {:open? false})
     :dispatch [::state-stage/reset-stage]}))

(re-frame/reg-sub
  ::open?
  (fn []
    (re-frame/subscribe [::state]))
  (fn [state]
    (get state :open? false)))

;; Form data

(def form-data-path (path-to-db [:form-data]))

(defn get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

(re-frame/reg-event-fx
  ::set-form-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-data-path data)}))

(re-frame/reg-event-fx
  ::reset-form-data
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db form-data-path {})}))

(defn- get-scene-data
  [form-data]
  (let [background-src "/images/questions/background.png"
        empty-scene (-> scene-utils/empty-data
                        (update :assets conj {:url background-src :size 1 :type "image"})
                        (update :objects assoc :background {:type "background" :src background-src})
                        (update :scene-objects conj ["background"]))]
    (->> (question/create (form->question-data form-data current-question-version)
                          {:action-name "question-action" :object-name "question"}
                          {:visible?           true
                           :show-check-button? true})
         (question/add-to-scene empty-scene))))

(re-frame/reg-sub
  ::scene-data
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get-scene-data form-data)))

(re-frame/reg-event-fx
  ::update-form-data
  (fn [{:keys [db]} [_ data]]
    (let [form-data (get-form-data db)]
      {:db (->> data
                (reduce (fn [form-data {:keys [object-name object-data-patch]}]
                          (let [scene-data (get-scene-data form-data)
                                object-data (get-in scene-data [:objects object-name])
                                param-name (-> object-data
                                               (get-in [:metadata :question-form-param])
                                               (keyword))]
                            (update form-data param-name merge object-data-patch)))
                        form-data)
                (assoc-in db form-data-path))})))

(re-frame/reg-sub
  ::field-value
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data] [_ field-name]]
    (get form-data field-name)))

(re-frame/reg-event-fx
  ::set-field-value
  (fn [{:keys [db]} [_ field value]]
    {:db (update-in db form-data-path merge {field value})}))

;; Options

(re-frame/reg-sub
  ::task-type-options
  (fn []
    [(re-frame/subscribe [::field-value :question-type])])
  (fn [[question-type]]
    (let [omit-value (fn [list omit-value]
                       (filter (fn [{:keys [value]}]
                                 (not= value omit-value))
                               list))]
      (cond-> [{:text  "Text"
                :value "text"}
               {:text  "Image"
                :value "image"}
               {:text  "Text with image"
                :value "text-image"}
               {:text  "Voice-over only"
                :value "voice-over"}]
              (some #{question-type} ["multiple-choice-image"]) (omit-value "image")
              (some #{question-type} ["multiple-choice-image"]) (omit-value "text-image")))))

;; Save

(re-frame/reg-event-fx
  ::save-question
  (fn [{:keys [db]} [_]]
    (let [{:keys [action]} (get-state db)
          form-data (get-form-data db)]
      (case action
        "add" {:dispatch [::state-activity/call-activity-common-action
                          {:action :add-question
                           :data   {:question-page-object form-data
                                    :data-version         current-question-version}}
                          {:on-success [::close]}]}
        "edit" (let [question-index (get-question-index db)]
                 (if (number? question-index)
                   {:dispatch [::state-activity/call-activity-common-action
                               {:action :edit-question
                                :data   {:question-page-object form-data
                                         :question-index       question-index
                                         :data-version         current-question-version}}
                               {:on-success [::close]}]}
                   {:show-message "Question does't have index. Try to update template."}))))))

(re-frame.core/reg-fx
  :show-message
  (fn [message]
    (js/alert message)))
