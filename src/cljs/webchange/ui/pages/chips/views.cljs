(ns webchange.ui.pages.chips.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(defn page
  []
  [:div#page--chips
   [layout {:title "Chips"}
    [:h2 "Static"]
    [panel {:class-name "chips-panel"}
     [ui/chip
      "Teachers"]
     [ui/chip {:counter 23}
      "Teachers"]
     [ui/chip {:icon "teachers"}
      "Teachers"]
     [ui/chip {:counter 23
               :icon    "students"}
      "Students"]]
    [:h2 "Clickable"]
    (r/with-let [visible? (r/atom true)
                 handle-click #(swap! visible? not)]
      [panel {:class-name "chips-panel"}
       [ui/chip {:icon     (if @visible? "visibility-on" "visibility-off")
                 :on-click handle-click}
        "Clickable"]
       [ui/chip {:icon     (if @visible? "visibility-on" "visibility-off")
                 :on-click handle-click
                 :state    "hover"}
        "Clickable"]
       [ui/chip {:icon     (if @visible? "visibility-on" "visibility-off")
                 :on-click handle-click
                 :state    "active"}
        "Clickable"]
       [ui/chip {:icon      (if @visible? "visibility-on" "visibility-off")
                 :on-click  handle-click
                 :disabled? true}
        "Clickable"]])]])
