(ns webchange.templates.library.categorize.colors.round-3
  (:require
    [webchange.templates.library.categorize.templates.round-3 :as round-3-template]))

(def params-object-names round-3-template/params-object-names)
(def var-object-names round-3-template/var-object-names)
(def var-action-names round-3-template/var-action-names)
(def all-vars-in-actions round-3-template/all-vars-in-actions)

(defn- word->item
  [{:keys [word x y]}]
  {:name        word
   :target      word
   :position    {:x        x
                 :y        y
                 :rotation -90
                 :scale    0.35}
   :src         (str "/raw/img/categorize/" word "_crayons.png")
   :pick-dialog {:name   (str word "-color")
                 :phrase (str "Color " word)}})

(def template (round-3-template/get-template
                {:boxes      [{:name     "purple"
                               :position {:x 1120 :y 652}
                               :src      "/raw/img/categorize/purple_table.png"}
                              {:name     "green"
                               :position {:x 330 :y 667}
                               :src      "/raw/img/categorize/green_table.png"}
                              {:name     "orange"
                               :position {:x 745 :y 773}
                               :src      "/raw/img/categorize/orange_table.png"}
                              {:name     "yellow"
                               :position {:x 943 :y 628}
                               :src      "/raw/img/categorize/yellow_box_small.png"}
                              {:name     "blue"
                               :position {:x 1352 :y 490}
                               :src      "/raw/img/categorize/blue_box_small.png"}
                              {:name     "red"
                               :position {:x 500 :y 506}
                               :src      "/raw/img/categorize/red_box_small.png"}]
                 :items      [(word->item {:word "red" :x 776 :y 501})
                              (word->item {:word "purple" :x 60 :y 1054})
                              (word->item {:word "yellow" :x 154 :y 139})
                              (word->item {:word "green" :x 19 :y 263})
                              (word->item {:word "blue" :x 1611 :y 440})
                              (word->item {:word "orange" :x 1495 :y 914})]
                 :tasks      [{:item "red" :box "red" :instruction "Put the red crayon in its crayon box."}
                              {:item "purple" :box "purple" :instruction "Put the purple crayon on its table."}
                              {:item "yellow" :box "yellow" :instruction "Put the yellow crayon in its crayon box."}
                              {:item "green" :box "green" :instruction "Put the green crayon on its table."}
                              {:item "blue" :box "blue" :instruction "Put the blue crayon in its crayon box."}
                              {:item "orange" :box "orange" :instruction "Put the orange crayon on its table."}]
                 :background {:background {:src "/raw/img/categorize/background-3.png"}
                              :surface    {:src "/raw/img/categorize/02.png"}
                              :decoration {:src "/raw/img/categorize/03.png"}}}))
