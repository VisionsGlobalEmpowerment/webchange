(ns webchange.templates.library.categorize.shapes.round-3
  (:require
    [webchange.templates.library.categorize.templates.round-3 :as round-3-template]))

(def params-object-names round-3-template/params-object-names)
(def var-object-names round-3-template/var-object-names)
(def var-action-names round-3-template/var-action-names)
(def all-vars-in-actions round-3-template/all-vars-in-actions)

(defn- word->item
  [{:keys [word x y]}]
  {:name        word
   :position    {:x x
                 :y y}
   :src         (str "/raw/img/categorize-shapes/" word "-group.png")
   :pick-dialog {:name   (str word "-item")
                 :phrase word}})

(def template (round-3-template/get-template
                {:boxes              [{:name     "triangle"
                                       :position {:x 196 :y 631}
                                       :src      "/raw/img/categorize-shapes/triangle-table.png"}
                                      {:name     "circle"
                                       :position {:x 1222 :y 650}
                                       :src      "/raw/img/categorize-shapes/circle-table.png"}
                                      {:name     "square"
                                       :position {:x 691 :y 665}
                                       :src      "/raw/img/categorize-shapes/square-table.png"}
                                      {:name     "star"
                                       :position {:x 1181 :y 835}
                                       :src      "/raw/img/categorize-shapes/star-box.png"}
                                      {:name     "rectangle"
                                       :position {:x 822 :y 835}
                                       :src      "/raw/img/categorize-shapes/rectangle-box.png"}
                                      {:name     "oval"
                                       :position {:x 463 :y 835}
                                       :src      "/raw/img/categorize-shapes/oval-box.png"}]
                 :items              [(word->item {:word "oval" :x 1685 :y 113})
                                      (word->item {:word "triangle" :x 410 :y 584})
                                      (word->item {:word "star" :x 830 :y 404})
                                      (word->item {:word "circle" :x 143 :y 921})
                                      (word->item {:word "rectangle" :x 123 :y 51})
                                      (word->item {:word "square" :x 512 :y 232})]
                 :tasks              [{:item "oval" :box "oval" :instruction "Oval in oval box"}
                                      {:item "triangle" :box "triangle" :instruction "Triangle on triangle table"}
                                      {:item "circle" :box "circle" :instruction "Circle on circle table"}
                                      {:item "star" :box "star" :instruction "Star in star box"}
                                      {:item "rectangle" :box "rectangle" :instruction "Rectangle in rectangle box"}
                                      {:item "square" :box "square" :instruction "Square on square table"}]
                 :background         {:background {:src "/raw/img/categorize-shapes/background-class.png"}
                                      :decoration {:src "/raw/img/categorize-shapes/decoration.png"}
                                      :surface    {:src "/raw/img/categorize-shapes/surface.png"}}
                 :librarian-position "between"}))
