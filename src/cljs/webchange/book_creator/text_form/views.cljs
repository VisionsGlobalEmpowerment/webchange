(ns webchange.book-creator.text-form.views
  (:require
    [webchange.ui-framework.index :refer [button select text-area]]))

(defn text-form
  []
  [:div.text-form
   [:div.font-controls
    [select {:value               24
             :options-text-suffix "pt"
             :options             (->> [8 12 16 18 24 32 64 72 98]
                                       (map (fn [size] {:text size :value size})))}]]
   [text-area {:value "Some Text..."}]
   [:div.buttons-block
    [button {:variant  "contained"
             :color    "primary"
             :on-click #(print "Apply")}
     "Apply"]
    [button {:variant  "outlined"
             :on-click #(print "Delete")}
     "Delete"]]])
