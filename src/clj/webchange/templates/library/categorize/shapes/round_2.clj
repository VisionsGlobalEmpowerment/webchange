(ns webchange.templates.library.categorize.shapes.round-2
  (:require
    [webchange.templates.library.categorize.templates.round-2 :as round-2-template]))

(def params-object-names round-2-template/params-object-names)
(def var-object-names round-2-template/var-object-names)
(def var-action-names round-2-template/var-action-names)
(def all-vars-in-actions round-2-template/all-vars-in-actions)

(defn- word->item
  [{:keys [word x y]}]
  {:target      word
   :position    {:x x
                 :y y}
   :src         (str "/raw/img/categorize-shapes/" word "-group.png")
   :pick-dialog {:name   (str word "-item")
                 :phrase word}})

(def template (round-2-template/get-template
                {:boxes           [{:name     "circle"
                                    :position {:x 1222 :y 650}
                                    :src      "/raw/img/categorize-shapes/circle-table.png"}
                                   {:name     "square"
                                    :position {:x 691 :y 665}
                                    :src      "/raw/img/categorize-shapes/square-table.png"}
                                   {:name     "triangle"
                                    :position {:x 196 :y 631}
                                    :src      "/raw/img/categorize-shapes/triangle-table.png"}
                                   {:name     "oval"
                                    :position {:x 463 :y 835}
                                    :src      "/raw/img/categorize-shapes/oval-box.png"}
                                   {:name     "rectangle"
                                    :position {:x 822 :y 835}
                                    :src      "/raw/img/categorize-shapes/rectangle-box.png"}
                                   {:name     "star"
                                    :position {:x 1181 :y 835}
                                    :src      "/raw/img/categorize-shapes/star-box.png"}]
                 :items           [(word->item {:word "circle" :x 1661 :y 809})
                                   (word->item {:word "oval" :x 169 :y 49})
                                   (word->item {:word "rectangle" :x 59 :y 359})
                                   (word->item {:word "square" :x 1682 :y 136})
                                   (word->item {:word "star" :x 826 :y 402})
                                   (word->item {:word "triangle" :x 126 :y 936})]
                 :background      {:background {:src "/raw/img/categorize-shapes/background-class.png"}
                                   :decoration {:src "/raw/img/categorize-shapes/decoration.png"}
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
                                   :finish-dialog    {:name               "finish-round-dialog"
                                                      :phrase             "finish-answer"
                                                      :phrase-description "finish answer"
                                                      :prompt             "Finish dialog"}}}))
