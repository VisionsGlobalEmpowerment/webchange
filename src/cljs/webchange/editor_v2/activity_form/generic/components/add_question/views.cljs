(ns webchange.editor-v2.activity-form.generic.components.add-question.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.generic.components.add-question.state :as state]
    [webchange.editor-v2.wizard.activity-template.question.views :refer [question-option]]
    [webchange.ui-framework.components.index :refer [button dialog]]))

(defn open-add-question-window
  []
  (re-frame/dispatch [::state/open]))

(defn- add-question-form
  [{:keys [data]}]
  (r/with-let [_ (reset! data {})]
    [question-option {:key  :data
                      :data data}]))

(defn add-question-window
  []
  (r/with-let [data (r/atom {})]
    (let [open? @(re-frame/subscribe [::state/open?])
          handle-add #(re-frame/dispatch [::state/add-question @data])
          handle-close #(re-frame/dispatch [::state/close])]
      (when open?
        [dialog {:open?    open?
                 :on-close handle-close
                 :title    "Add Question"
                 :actions  [[button {:on-click handle-add
                                     :size     "big"}
                             "Add"]
                            [button {:on-click handle-close
                                     :color    "default"
                                     :variant  "outlined"
                                     :size     "big"}
                             "Cancel"]]}
         [add-question-form {:data data}]]))))
