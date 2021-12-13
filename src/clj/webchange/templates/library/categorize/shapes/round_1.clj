(ns webchange.templates.library.categorize.shapes.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)
(def all-vars-in-actions round-1-template/all-vars-in-actions)

(def template (round-1-template/get-template
                {:boxes      [{:name     "circle"
                               :position {:x 1572 :y 777}
                               :src      "/raw/img/categorize-shapes/circle-box.png"}
                              {:name     "oval"
                               :position {:x 69 :y 777}
                               :src      "/raw/img/categorize-shapes/oval-box.png"}
                              {:name     "rectangle"
                               :position {:x 369 :y 777}
                               :src      "/raw/img/categorize-shapes/rectangle-box.png"}
                              {:name     "square"
                               :position {:x 670 :y 777}
                               :src      "/raw/img/categorize-shapes/square-box.png"}
                              {:name     "star"
                               :position {:x 971 :y 777}
                               :src      "/raw/img/categorize-shapes/star-box.png"}
                              {:name     "triangle"
                               :position {:x 1271 :y 777}
                               :src      "/raw/img/categorize-shapes/triangle-box.png"}]
                 :items      [{:target      "circle"
                               :position    {:x 710 :y 442}
                               :src         "/raw/img/categorize-shapes/circle.png"
                               :pick-dialog {:name   "oval-item"
                                             :phrase "oval"}}
                              {:target      "oval"
                               :position    {:x 925 :y 124}
                               :src         "/raw/img/categorize-shapes/oval.png"
                               :pick-dialog {:name   "oval-item"
                                             :phrase "oval"}}
                              {:target      "rectangle"
                               :position    {:x 617 :y 146}
                               :src         "/raw/img/categorize-shapes/rectangle.png"
                               :pick-dialog {:name   "rectangle-item"
                                             :phrase "rectangle"}}
                              {:target      "square"
                               :position    {:x 982 :y 360}
                               :src         "/raw/img/categorize-shapes/square.png"
                               :pick-dialog {:name   "square-item"
                                             :phrase "square"}}
                              {:target      "star"
                               :position    {:x 1214 :y 289}
                               :src         "/raw/img/categorize-shapes/star.png"
                               :pick-dialog {:name   "star-item"
                                             :phrase "star"}}
                              {:target      "triangle"
                               :position    {:x 424 :y 377}
                               :src         "/raw/img/categorize-shapes/triangle.png"
                               :pick-dialog {:name   "triangle-item"
                                             :phrase "triangle"}}]
                 :background {:src "/raw/img/categorize-shapes/background-white.png"}}))
