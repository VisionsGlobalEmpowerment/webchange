(ns webchange.lesson-builder.widgets.question-options.views
  (:require
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn question-options
  []
  (let [handle-add-question-click #(print "Add Question")]
    [:div.widget--question-options
     [options-list {:items [{:id       :image-add
                             :text     "Add Question"
                             :icon     "plus"
                             :on-click handle-add-question-click}]}]
     [draggable-list
      [draggable {:text    "Question 1"
                  :actions [{:icon     "edit"
                             :on-click #(print "edit")}
                            {:icon     "trash"
                             :on-click #(print "remove")}]}]
      [draggable {:text    "Question 2"
                  :actions [{:icon     "edit"
                             :on-click #(print "edit")}
                            {:icon     "trash"
                             :on-click #(print "remove")}]}]
      [draggable {:text    "Question 3"
                  :actions [{:icon     "edit"
                             :on-click #(print "edit")}
                            {:icon     "trash"
                             :on-click #(print "remove")}]}]
      [draggable {:text    "Question 4"
                  :actions [{:icon     "edit"
                             :on-click #(print "edit")}
                            {:icon     "trash"
                             :on-click #(print "remove")}]}]]]))
