(ns webchange.templates.library.categorize.shapes.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)
(def all-vars-in-actions round-1-template/all-vars-in-actions)

(defn- word->box
  [{:keys [word x y]}]
  {:name     word
   :position {:x x :y y}
   :src      (str "/raw/img/categorize-shapes/" word "-box.png")})

(defn- word->item
  [{:keys [word x y]}]
  {:target      word
   :position    {:x x :y y}
   :src         (str "/raw/img/categorize-shapes/" word ".png")
   :pick-dialog {:name   (str word "-item")
                 :phrase word}})

(def template (round-1-template/get-template
                {:boxes      [(word->box {:word "circle" :x 1572 :y 777})
                              (word->box {:word "oval" :x 69 :y 777})
                              (word->box {:word "rectangle" :x 369 :y 777})
                              (word->box {:word "square" :x 670 :y 777})
                              (word->box {:word "star" :x 971 :y 777})
                              (word->box {:word "triangle" :x 1271 :y 777})]
                 :items      [(word->item {:word "circle" :x 710 :y 442})
                              (word->item {:word "oval" :x 925 :y 124})
                              (word->item {:word "rectangle" :x 617 :y 146})
                              (word->item {:word "square" :x 982 :y 360})
                              (word->item {:word "star" :x 1214 :y 289})
                              (word->item {:word "triangle" :x 424 :y 377})]
                 :background {:src "/raw/img/categorize-shapes/background-white.png"}}))
