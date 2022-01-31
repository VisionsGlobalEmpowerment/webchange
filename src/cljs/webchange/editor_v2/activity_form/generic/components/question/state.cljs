(ns webchange.editor-v2.activity-form.generic.components.question.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]
    [webchange.state.state-activity :as state-activity]
    [webchange.question.get-question-data :refer [current-question-version default-question-data form->question-data]]
    [webchange.question.create :as question]
    [webchange.utils.scene-data :as scene-utils]))

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
    {:dispatch-n [[::state-stage/hide-stage]
                  [::open {:action           "add"
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
                          {:visible?                   true
                           :show-check-button?         true
                           :highlight-correct-options? true})
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

(defn- get-field-value
  [db field-name]
  (-> (get-form-data db)
      (get field-name)))

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

;; Task

(def task-field-name :task-type)

(defn- get-current-task
  [db]
  (get-field-value db task-field-name))

(re-frame/reg-sub
  ::current-task-type
  (fn []
    (re-frame/subscribe [::field-value task-field-name]))
  (fn [current-value]
    current-value))

(re-frame/reg-event-fx
  ::set-task-type
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::set-field-value task-field-name value]}))

(defn- get-task-type-options
  ([question-type]
   (get-task-type-options question-type ""))
  ([question-type current-question-type-name]
   (let [task-types [{:value          "text"
                      :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]
                      :text           "Text"}
                     {:value          "image"
                      :question-types ["multiple-choice-text" "thumbs-up-n-down"]
                      :text           "Image"}
                     {:value          "text-image"
                      :question-types ["multiple-choice-text" "thumbs-up-n-down"]
                      :text           "Text with image"}
                     {:value          "voice-over"
                      :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]
                      :text           "Voice-over only"}]]
     (->> task-types
          (map (fn [{:keys [text value question-types]}]
                 (let [disabled? (->> question-types (some #{question-type}) (not))]
                   (cond-> {:text  text
                            :value value}
                           disabled? (merge {:disabled? true
                                             :title     (str "Not available for question type '" current-question-type-name "'.")})))))))))

(defn- get-default-task-type-for-question
  [question-type current-task]
  (let [question-options (get-task-type-options question-type)
        current-task-disabled? (some (fn [{:keys [value disabled?]}]
                                       (and (= value current-task) disabled?))
                                     question-options)]
    (if current-task-disabled?
      (some (fn [{:keys [disabled? value]}]
              (and (not disabled?)
                   value))
            question-options)
      current-task)))

(re-frame/reg-sub
  ::task-type-options
  (fn []
    [(re-frame/subscribe [::field-value :question-type])
     (re-frame/subscribe [::current-question-type-name])])
  (fn [[question-type current-question-type-name]]
    (let [task-types [{:value          "text"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]
                       :text           "Text"}
                      {:value          "image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]
                       :text           "Image"}
                      {:value          "text-image"
                       :question-types ["multiple-choice-text" "thumbs-up-n-down"]
                       :text           "Text with image"}
                      {:value          "voice-over"
                       :question-types ["multiple-choice-image" "multiple-choice-text" "thumbs-up-n-down"]
                       :text           "Voice-over only"}]]
      (->> task-types
           (map (fn [{:keys [text value question-types]}]
                  (let [disabled? (->> question-types (some #{question-type}) (not))]
                    (cond-> {:text  text
                             :value value}
                            disabled? (merge {:disabled? true
                                              :title     (str "Not available for question type '" current-question-type-name "'.")})))))))))

;; Question type

(def question-field-name :question-type)

(re-frame/reg-sub
  ::current-question-type
  (fn []
    (re-frame/subscribe [::field-value question-field-name]))
  (fn [current-value]
    current-value))

(re-frame/reg-sub
  ::current-question-type-name
  (fn []
    [(re-frame/subscribe [::current-question-type])
     (re-frame/subscribe [::question-type-options])])
  (fn [[question-type question-type-options]]
    (some (fn [{:keys [text value]}]
            (and (= value question-type)
                 text))
          question-type-options)))

(re-frame/reg-event-fx
  ::set-question-type
  (fn [{:keys [db]} [_ value]]
    (let [current-task (get-current-task db)]
      {:dispatch-n [[::set-field-value question-field-name value]
                    [::set-task-type (get-default-task-type-for-question value current-task)]]})))

(re-frame/reg-sub
  ::question-type-options
  (fn []
    [{:text  "Multiple choice image"
      :value "multiple-choice-image"}
     {:text  "Multiple choice text"
      :value "multiple-choice-text"}
     {:text  "Thumbs up & thumbs down"
      :value "thumbs-up-n-down"}]))

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
