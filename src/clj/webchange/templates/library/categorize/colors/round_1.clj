(ns webchange.templates.library.categorize.colors.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)
(def all-vars-in-actions round-1-template/all-vars-in-actions)

(def template (round-1-template/get-template
                {:boxes           [{:name     "yellow"
                                    :position {:x 75 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/yellow_box.png"}
                                   {:name     "blue"
                                    :position {:x 375 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/blue_box.png"}
                                   {:name     "red"
                                    :position {:x 675 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/red_box.png"}
                                   {:name     "purple"
                                    :position {:x 975 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/purple_box.png"}
                                   {:name     "orange"
                                    :position {:x 1275 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/orange_box.png"}
                                   {:name     "green"
                                    :position {:x 1575 :y 810 :scale 0.65}
                                    :src      "/raw/img/categorize/green_box.png"}]
                 :items           (->> [(->> [{:x 184 :y 228 :rotation -53}
                                              {:x 1406 :y 456 :rotation 125}
                                              {:x 1753 :y 144 :rotation 45}]
                                             (map (fn [position]
                                                    {:target      "blue"
                                                     :position    position
                                                     :src         "/raw/img/categorize/blue_crayons.png"
                                                     :pick-dialog {:name   "blue-color"
                                                                   :phrase "Color blue"}})))

                                        (->> [{:x 145 :y 521 :rotation -129}
                                              {:x 1071 :y 279 :rotation 75}
                                              {:x 1033 :y 558 :rotation -120}]
                                             (map (fn [position]
                                                    {:target      "yellow"
                                                     :position    position
                                                     :src         "/raw/img/categorize/yellow_crayons.png"
                                                     :pick-dialog {:name   "yellow-color"
                                                                   :phrase "Color yellow"}})))

                                        (->> [{:x 760 :y 64 :rotation 31}
                                              {:x 736 :y 387 :rotation -69}
                                              {:x 1097 :y 106 :rotation -52}]
                                             (map (fn [position]
                                                    {:target      "red"
                                                     :position    position
                                                     :src         "/raw/img/categorize/red_crayons.png"
                                                     :pick-dialog {:name   "red-color"
                                                                   :phrase "Color red"}})))

                                        (->> [{:x 134 :y 178 :rotation 37}
                                              {:x 1406 :y 456 :rotation 215}
                                              {:x 1753 :y 144 :rotation 135}]
                                             (map (fn [position]
                                                    {:target      "orange"
                                                     :position    position
                                                     :src         "/raw/img/categorize/orange_crayons.png"
                                                     :pick-dialog {:name   "orange-color"
                                                                   :phrase "Color orange"}})))

                                        (->> [{:x 145 :y 521 :rotation -39}
                                              {:x 1071 :y 279 :rotation 165}
                                              {:x 1033 :y 558 :rotation -30}]
                                             (map (fn [position]
                                                    {:target      "purple"
                                                     :position    position
                                                     :src         "/raw/img/categorize/purple_crayons.png"
                                                     :pick-dialog {:name   "purple-color"
                                                                   :phrase "Color purple"}})))

                                        (->> [{:x 760 :y 64 :rotation 121}
                                              {:x 736 :y 387 :rotation 25}
                                              {:x 1097 :y 106 :rotation 30}]
                                             (map (fn [position]
                                                    {:target      "green"
                                                     :position    position
                                                     :src         "/raw/img/categorize/green_crayons.png"
                                                     :pick-dialog {:name   "green-color"
                                                                   :phrase "Color green"}})))]
                                       (flatten))
                 :background      {:src "/raw/img/categorize/background.png"}
                 :generic-dialogs {:continue-sorting {:name   "continue-sorting"
                                                      :prompt "Continue sorting"}}
                 :tracks          {:items {:title "Round 1 - Colors"}}}))
