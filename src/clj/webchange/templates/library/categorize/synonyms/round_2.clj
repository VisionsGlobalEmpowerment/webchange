(ns webchange.templates.library.categorize.synonyms.round-2
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
   :src      (str "/raw/img/categorize-synonyms/" word ".png")})

(defn- word->item
  [{:keys [word target x y item]}]
  (let [item-name (or item word)]
    {:target         target
     :position       {:x     x
                      :y     y
                      :scale {:x 0.65 :y 0.65}}
     :src            (str "/raw/img/categorize-synonyms/" word ".png")
     :pick-dialog    {:name   (str item-name "-item")
                      :phrase word}
     :correct-dialog {:name   (str item-name "-correct")
                      :phrase (str word " correct")}}))

(def template (round-2-template/get-template
                {:boxes           [(word->box {:word "chilly" :x 980})
                                   (word->box {:word "kid" :x 1274})
                                   (word->box {:word "big" :x 393})
                                   (word->box {:word "trash" :x 1568})
                                   (word->box {:word "afraid" :x 686})
                                   (word->box {:word "glad" :x 99})]
                 :items           [(word->item {:word "cold" :target "chilly" :x 701 :y 89})
                                   (word->item {:word "child" :target "kid" :x 863 :y 243})
                                   (word->item {:word "large" :target "big" :x 448 :y 256})
                                   (word->item {:word "garbage" :target "trash" :x 1035 :y 383 :item "trash"})
                                   (word->item {:word "scared" :target "afraid" :x 1121 :y 105})
                                   (word->item {:word "happy" :target "glad" :x 701 :y 393})]
                 :background      {:background {:src "/raw/img/categorize-shapes/background-class.png"},
                                   :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                   :surface    {:src "/raw/img/categorize-shapes/surface.png"}}
                 :generic-dialogs {:intro            {:name               "intro"
                                                      :prompt             "Start dialog"
                                                      :phrase             "intro"
                                                      :phrase-description "Introduce task"}
                                   :tap-instructions {:name   "tap-instructions"
                                                      :phrase "Tap instructions"}
                                   :correct-answer   {:name               "correct-answer"
                                                      :phrase             "correct-answer"
                                                      :phrase-description "correct answer"
                                                      :prompt             "Correct answer"}
                                   :wrong-answer     {:name               "wrong-answer"
                                                      :phrase             "wrong-answer"
                                                      :phrase-description "wrong answer"
                                                      :prompt             "Wrong answer"}
                                   :finish-dialog    {:name               "finish-dialog"
                                                      :phrase             "finish-answer"
                                                      :phrase-description "finish answer"
                                                      :prompt             "Finish dialog"}}}))
