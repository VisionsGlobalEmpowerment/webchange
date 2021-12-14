(ns webchange.templates.library.categorize.antonyms.round-2
  (:require
    [webchange.templates.library.categorize.templates.round-2 :as round-2-template]))

(def params-object-names round-2-template/params-object-names)
(def var-object-names round-2-template/var-object-names)
(def var-action-names round-2-template/var-action-names)
(def all-vars-in-actions round-2-template/all-vars-in-actions)

(defn- word->box
  [{:keys [word x]}]
  {:name     word
   :position {:x x :y 763}
   :src      (str "/raw/img/categorize-antonims/" word ".png")})

(defn- word->item
  [{:keys [word target x y]}]
  {:target         target
   :position       {:x     x
                    :y     y
                    :scale 0.65}
   :src            (str "/raw/img/categorize-antonims/" word ".png")
   :pick-dialog    {:name   (str word "-item")
                    :phrase word}
   :correct-dialog {:name   (str word "-correct")
                    :phrase (str word " correct")}})

(def template (round-2-template/get-template
                {:boxes           [(word->box {:word "right" :x 99})
                                   (word->box {:word "front" :x 686})
                                   (word->box {:word "down" :x 1568})
                                   (word->box {:word "quiet" :x 393})
                                   (word->box {:word "day" :x 980})
                                   (word->box {:word "in" :x 1274})]
                 :items           [(word->item {:word "left" :target "right" :x 1635 :y 107})
                                   (word->item {:word "back" :target "front" :x 415 :y 354})
                                   (word->item {:word "up" :target "down" :x 801 :y 481})
                                   (word->item {:word "loud" :target "quiet" :x 790 :y 160})
                                   (word->item {:word "night" :target "day" :x 1274 :y 481})
                                   (word->item {:word "out" :target "in" :x 1095 :y 223})]
                 :background      {:background {:src "/raw/img/categorize-antonims/background-class.png"}
                                   :decoration {:src "/raw/img/categorize-antonims/decoration.png"}
                                   :surface    {:src "/raw/img/categorize-antonims/surface.png"}}
                 :generic-dialogs {:finish-dialog {:name "finish-round-dialog"}}}))
