(ns webchange.templates.library.categorize.antonyms.round-3
  (:require
    [webchange.templates.library.categorize.templates.round-3 :as round-3-template]))

(def params-object-names round-3-template/params-object-names)
(def var-object-names round-3-template/var-object-names)
(def var-action-names round-3-template/var-action-names)
(def all-vars-in-actions round-3-template/all-vars-in-actions)

(defn- word->box
  [{:keys [word x]}]
  {:name     word
   :position {:x x :y 763}
   :src      (str "/raw/img/categorize-antonims/" word ".png")})

(defn- word->item
  [{:keys [word target x y]}]
  {:name        word
   :target      target
   :position    {:x     x
                 :y     y
                 :scale 0.65}
   :src         (str "/raw/img/categorize-antonims/" word ".png")
   :pick-dialog {:name   (str word "-item")
                 :phrase word}})

(def template (round-3-template/get-template
                {:boxes      [(word->box {:word "down" :x 1568})
                              (word->box {:word "day" :x 980})
                              (word->box {:word "front" :x 686})
                              (word->box {:word "in" :x 1274})
                              (word->box {:word "quiet" :x 393})
                              (word->box {:word "right" :x 99})]
                 :items      [(word->item {:word "left" :x 1635 :y 107})
                              (word->item {:word "up" :x 801 :y 481})
                              (word->item {:word "out" :x 1095 :y 223})
                              (word->item {:word "night" :x 1274 :y 481})
                              (word->item {:word "loud" :x 790 :y 160})
                              (word->item {:word "back" :x 415 :y 354})]
                 :tasks      [{:item "left" :box "right"}
                              {:item "loud" :box "quiet"}
                              {:item "out" :box "in"}
                              {:item "night" :box "day"}
                              {:item "back" :box "front"}
                              {:item "up" :box "down"}]
                 :background {:background {:src "/raw/img/categorize-antonims/background-class.png"}
                              :decoration {:src "/raw/img/categorize-antonims/decoration.png"}
                              :surface    {:src "/raw/img/categorize-antonims/surface.png"}}}))
