(ns webchange.templates.library.categorize.synonyms.round-3
  (:require
    [webchange.templates.library.categorize.templates.round-3 :as round-3-template]))

(def params-object-names round-3-template/params-object-names)
(def var-object-names round-3-template/var-object-names)
(def var-action-names round-3-template/var-action-names)
(def all-vars-in-actions round-3-template/all-vars-in-actions)

(defn- word->box
  [{:keys [word x]}]
  {:name     word
   :position {:x x :y 762}
   :src      (str "/raw/img/categorize-synonyms/" word ".png")})

(defn- word->item
  [{:keys [word target x y]}]
  {:name        word
   :target      target
   :position    {:x     x
                 :y     y
                 :scale 0.65}
   :src         (str "/raw/img/categorize-synonyms/" word ".png")
   :pick-dialog {:name   (str word "-item")
                 :phrase word}})

(def template (round-3-template/get-template
                {:boxes      [(word->box {:word "big" :x 392})
                              (word->box {:word "scared" :x 686})
                              (word->box {:word "child" :x 1274})
                              (word->box {:word "happy" :x 99})
                              (word->box {:word "trash" :x 1568})
                              (word->box {:word "cold" :x 980})]
                 :items      [(word->item {:word "chilly" :target "cold" :x 768 :y 481})
                              (word->item {:word "large" :target "big" :x 415 :y 355})
                              (word->item {:word "glad" :target "happy" :x 1674 :y 434})
                              (word->item {:word "afraid" :target "scared" :x 1636 :y 107})
                              (word->item {:word "garbage" :target "trash" :x 1388 :y 561})
                              (word->item {:word "kid" :target "child" :x 1094 :y 223})]
                 :tasks      [{:item "chilly" :box "cold"}
                              {:item "garbage" :box "trash"}
                              {:item "glad" :box "happy"}
                              {:item "afraid" :box "scared"}
                              {:item "kid" :box "child"}
                              {:item "large" :box "big"}]
                 :background {:background {:src "/raw/img/categorize-shapes/background-class.png"}
                              :decoration {:src "/raw/img/categorize-shapes/decoration.png"}
                              :surface    {:src "/raw/img/categorize-shapes/surface.png"}}}))
