(ns webchange.lesson-builder.widgets.effects-add.views
  (:require
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.components.fold.views :refer [fold]]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]))

(defn effects-add
  []
  [:div.widget--effects-add
   [fold {:title "Template Effects"}
    [draggable-list
     [draggable {:text "Effect 01"}]
     [draggable {:text "Effect 02"}]
     [draggable {:text "Effect 03"}]
     [draggable {:text "Effect 04"}]
     [draggable {:text "Effect 05"}]
     [draggable {:text "Effect 06"}]
     [draggable {:text "Effect 07"}]
     [draggable {:text "Effect 08"}]
     [draggable {:text "Effect 09"}]]]
   [fold {:title "Image Effects"}
    [draggable-list
     [draggable {:text "Effect 01"}]
     [draggable {:text "Effect 02"}]
     [draggable {:text "Effect 03"}]]]])
