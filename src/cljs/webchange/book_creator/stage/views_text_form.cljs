(ns webchange.book-creator.stage.views-text-form
  (:require
    [webchange.ui-framework.index :refer [button icon select text-area]]))

(defn text-form
  []
  [:div.text-form
   [:div.font-controls
    [:div.control-block
     [icon {:icon "font-family"}]
     [select {:value               24
              :options-text-suffix "pt"
              :options             (->> ["Roboto"]
                                        (map (fn [size] {:text size :value size})))
              :class-name          "font-family-selector"}]]
    [:div.control-block
     [icon {:icon "font-size"}]
     [select {:value               24
              :options-text-suffix "pt"
              :options             (->> [8 12 16 18 24 32 64 72 98]
                                        (map (fn [size] {:text size :value size})))
              :class-name          "font-size-selector"}]]]
   [text-area {:value "Some Text..."}]
   [:div.buttons-block
    [button {:variant  "contained"
             :color    "primary"
             :on-click #(print "Apply")}
     "Apply"]
    [button {:variant  "outlined"
             :on-click #(print "Delete")}
     "Delete"]]])
