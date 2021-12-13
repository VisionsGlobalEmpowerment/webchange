(ns webchange.templates.library.categorize.colors.round-2
  (:require
    [webchange.templates.library.categorize.templates.round-2 :as round-2-template]))

(def params-object-names round-2-template/params-object-names)
(def var-object-names round-2-template/var-object-names)
(def var-action-names round-2-template/var-action-names)
(def all-vars-in-actions round-2-template/all-vars-in-actions)

(defn- word->item
  [{:keys [word x y]}]
  {:target      word
   :position    {:x        x
                 :y        y
                 :rotation -90
                 :scale    0.35}
   :src         (str "/raw/img/categorize/" word "_crayons.png")
   :pick-dialog {:name   (str word "-color")
                 :phrase (str "Color " word)}})

(def template (round-2-template/get-template
                {:boxes           [{:name     "orange"
                                    :position {:x 1120 :y 652}
                                    :src      "/raw/img/categorize/orange_table.png"}
                                   {:name     "green"
                                    :position {:x 330 :y 667}
                                    :src      "/raw/img/categorize/green_table.png"}
                                   {:name     "purple"
                                    :position {:x 745 :y 773}
                                    :src      "/raw/img/categorize/purple_table.png"}
                                   {:name     "yellow"
                                    :position {:x 500 :y 506}
                                    :src      "/raw/img/categorize/yellow_box_small.png"}
                                   {:name     "blue"
                                    :position {:x 943 :y 628}
                                    :src      "/raw/img/categorize/blue_box_small.png"}
                                   {:name     "red"
                                    :position {:x 1352 :y 490}
                                    :src      "/raw/img/categorize/red_box_small.png"}]
                 :items           [(word->item {:word "blue" :x 46 :y 1050})
                                   (word->item {:word "blue" :x 592 :y 500})
                                   (word->item {:word "blue" :x 17 :y 143})
                                   (word->item {:word "orange" :x 746 :y 850})
                                   (word->item {:word "orange" :x 892 :y 400})
                                   (word->item {:word "orange" :x 317 :y 238})
                                   (word->item {:word "yellow" :x 764 :y 691})
                                   (word->item {:word "yellow" :x 1171 :y 126})
                                   (word->item {:word "yellow" :x 1618 :y 440})
                                   (word->item {:word "purple" :x 664 :y 541})
                                   (word->item {:word "purple" :x 1271 :y 236})
                                   (word->item {:word "purple" :x 1418 :y 310})
                                   (word->item {:word "red" :x 924 :y 500})
                                   (word->item {:word "red" :x 1618 :y 958})
                                   (word->item {:word "red" :x 1548 :y 164})
                                   (word->item {:word "green" :x 714 :y 210})
                                   (word->item {:word "green" :x 1418 :y 818})
                                   (word->item {:word "green" :x 1678 :y 364})]
                 :background      {:background {:src "/raw/img/categorize/01.png"},
                                   :decoration {:src "/raw/img/categorize/03.png"},
                                   :surface    {:src "/raw/img/categorize/02.png"}}
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
                                   :finish-dialog    {:name               "finish-round-dialog"
                                                      :phrase             "finish-answer"
                                                      :phrase-description "finish answer"
                                                      :prompt             "Finish dialog"}}}))
