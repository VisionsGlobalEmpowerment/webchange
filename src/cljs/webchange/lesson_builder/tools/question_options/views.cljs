(ns webchange.lesson-builder.tools.question-options.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]
    [webchange.lesson-builder.tools.question-options.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn- question-option
  [{:keys [name action]}]
  (let [removing? @(re-frame/subscribe [::state/removing? action])
        handle-edit-click #(re-frame/dispatch [::state/edit-question action])
        handle-remove-click #(re-frame/dispatch [::state/remove-question action])]
    [draggable {:text    name
                :action  "add-action"
                :data    {:action-id action}
                :actions [{:icon      "edit"
                           :on-click  handle-edit-click
                           :disabled? removing?
                           :title     "Edit question"}
                          {:icon     "trash"
                           :on-click handle-remove-click
                           :loading? removing?
                           :title    "Remove question"}]}]))

(defn question-options
  []
  (let [questions @(re-frame/subscribe [::state/question-options])
        handle-add-question-click #(re-frame/dispatch [::state/add-question])]
    [:div.widget--question-options
     [options-list {:items [{:id       :image-add
                             :text     "Add Question"
                             :icon     "plus"
                             :on-click handle-add-question-click}]}]
     (when-not (empty? questions)
       [draggable-list {}
        (for [{:keys [action] :as data} questions]
          ^{:key action}
          [question-option data])])]))
