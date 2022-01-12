(ns webchange.editor-v2.activity-form.generic.components.question.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.question.state :as state]
    [webchange.editor-v2.activity-form.generic.components.question.views-form :refer [question-form]]
    [webchange.ui-framework.components.index :refer [button dialog]]))

(defn open-add-question-window
  []
  (re-frame/dispatch [::state/open-add-question-window]))

(defn question-window
  []
  (let [open? @(re-frame/subscribe [::state/open?])
        handle-save #(re-frame/dispatch [::state/save-question])
        handle-close #(re-frame/dispatch [::state/close])
        {:keys [title save-button-text]} @(re-frame/subscribe [::state/state])]
    (when open?
      [dialog {:open?    open?
               :on-close handle-close
               :title    title
               :actions  [[button {:on-click handle-save
                                   :size     "big"}
                           save-button-text]
                          [button {:on-click handle-close
                                   :color    "default"
                                   :variant  "outlined"
                                   :size     "big"}
                           "Cancel"]]}
       [question-form]])))
