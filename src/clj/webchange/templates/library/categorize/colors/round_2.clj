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
                {:boxes              [{:name     "orange"
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
                 :items              [(word->item {:word "blue" :x 46 :y 1073})
                                      (word->item {:word "blue" :x 592 :y 523})
                                      (word->item {:word "blue" :x 17 :y 166})
                                      (word->item {:word "orange" :x 746 :y 873})
                                      (word->item {:word "orange" :x 892 :y 423})
                                      (word->item {:word "orange" :x 317 :y 261})
                                      (word->item {:word "yellow" :x 764 :y 714})
                                      (word->item {:word "yellow" :x 1171 :y 149})
                                      (word->item {:word "yellow" :x 1618 :y 463})
                                      (word->item {:word "purple" :x 664 :y 564})
                                      (word->item {:word "purple" :x 1271 :y 259})
                                      (word->item {:word "purple" :x 1418 :y 333})
                                      (word->item {:word "red" :x 924 :y 523})
                                      (word->item {:word "red" :x 1618 :y 981})
                                      (word->item {:word "red" :x 1548 :y 187})
                                      (word->item {:word "green" :x 714 :y 233})
                                      (word->item {:word "green" :x 1418 :y 841})
                                      (word->item {:word "green" :x 1678 :y 387})]
                 :background         {:background {:src "/raw/img/categorize/01.png"},
                                      :decoration {:src "/raw/img/categorize/03.png"},
                                      :surface    {:src "/raw/img/categorize/02.png"}}
                 :generic-dialogs    {:finish-dialog {:name "finish-round-dialog"}}
                 :librarian-position "between"}))
