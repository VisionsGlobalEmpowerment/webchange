(ns webchange.lesson-builder.tools.question-options.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]
    [webchange.lesson-builder.tools.question-options.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn question-options
  []
  (let [questions @(re-frame/subscribe [::state/question-options])
        handle-add-question-click #(print "Add Question")]
    [:div.widget--question-options
     [options-list {:items [{:id       :image-add
                             :text     "Add Question"
                             :icon     "plus"
                             :on-click handle-add-question-click}]}]
     (when-not (empty? questions)
       [draggable-list {}
        (for [{:keys [name action]} questions]
          [draggable {:text    name
                      :action  "add-action"
                      :data    {:action-id action}
                      :actions [#_{:icon     "edit"
                                   :on-click #(print "edit")}
                                #_{:icon     "trash"
                                   :on-click #(print "remove")}]}])])]))
