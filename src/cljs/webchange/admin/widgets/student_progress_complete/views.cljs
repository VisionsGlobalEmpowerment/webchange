(ns webchange.admin.widgets.student-progress-complete.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.student-progress-complete.state :as state]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.student-progress :as progress-spec]))

(defn student-progress-complete
  [{:keys [student-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? on-cancel on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/saving?])

          level-options @(re-frame/subscribe [::state/level-options])
          lesson-options @(re-frame/subscribe [::state/lesson-options])
          activity-options @(re-frame/subscribe [::state/activity-options])

          data @(re-frame/subscribe [::state/form-data])
          model {:level    {:label        "Level"
                            :type         :select
                            :options      level-options
                            :options-type "int"}
                 :lesson   {:label        "Lesson"
                            :type         :select
                            :options      lesson-options
                            :options-type "int"}
                 :activity {:label        "Activity"
                            :type         :select
                            :options      activity-options
                            :options-type "int"}}
          handle-save #(re-frame/dispatch [::state/save {:on-success on-save}])]
      [ui/form {:form-id   (-> (str "student--complete-progress" student-id)
                               (keyword))
                :data      data
                :model     model
                :spec      ::progress-spec/complete-student-progress
                :on-change #(re-frame/dispatch [::state/update-form-data %])
                :on-save   handle-save
                :on-cancel on-cancel
                :disabled? (not editable?)
                :loading?  loading?
                :saving?   saving?}])))
