(ns webchange.templates.library.categorize.antonyms.round-1
  (:require
    [webchange.templates.library.categorize.templates.round-1 :as round-1-template]))

(def params-object-names round-1-template/params-object-names)
(def var-object-names round-1-template/var-object-names)
(def var-action-names round-1-template/var-action-names)
(def all-vars-in-actions round-1-template/all-vars-in-actions)

(def template (round-1-template/get-template
                {:boxes      [{:name     "right"
                               :position {:x 99 :y 763}
                               :src      "/raw/img/categorize-antonims/right.png"}
                              {:name     "front"
                               :position {:x 686 :y 763}
                               :src      "/raw/img/categorize-antonims/front.png"}
                              {:name     "down"
                               :position {:x 1568 :y 763}
                               :src      "/raw/img/categorize-antonims/down.png"}
                              {:name     "quiet"
                               :position {:x 393 :y 763}
                               :src      "/raw/img/categorize-antonims/quiet.png"}
                              {:name     "day"
                               :position {:x 980 :y 763}
                               :src      "/raw/img/categorize-antonims/day.png"}
                              {:name     "in"
                               :position {:x 1274 :y 763}
                               :src      "/raw/img/categorize-antonims/in.png"}]
                 :items      [{:target         "right"
                               :position       {:x 448 :y 256}
                               :src            "/raw/img/categorize-antonims/left.png"
                               :pick-dialog    {:name   "left-item"
                                                :phrase "left"}
                               :correct-dialog {:name   "left-correct"
                                                :phrase "left correct"}}
                              {:target         "front"
                               :position       {:x 701 :y 393}
                               :src            "/raw/img/categorize-antonims/back.png"
                               :pick-dialog    {:name   "back-item"
                                                :phrase "back"}
                               :correct-dialog {:name   "back-correct"
                                                :phrase "back correct"}}
                              {:target         "down"
                               :position       {:x 1035 :y 383}
                               :src            "/raw/img/categorize-antonims/up.png"
                               :pick-dialog    {:name   "up-item"
                                                :phrase "up"}
                               :correct-dialog {:name   "up-correct"
                                                :phrase "up correct"}}
                              {:target         "quiet"
                               :position       {:x 701 :y 89}
                               :src            "/raw/img/categorize-antonims/loud.png"
                               :pick-dialog    {:name   "loud-item"
                                                :phrase "loud"}
                               :correct-dialog {:name   "loud-correct"
                                                :phrase "loud correct"}}
                              {:target         "day"
                               :position       {:x 1121 :y 105}
                               :src            "/raw/img/categorize-antonims/night.png"
                               :pick-dialog    {:name   "night-item"
                                                :phrase "night"}
                               :correct-dialog {:name   "night-correct"
                                                :phrase "night correct"}}
                              {:target         "in"
                               :position       {:x 863 :y 243}
                               :src            "/raw/img/categorize-antonims/out.png"
                               :pick-dialog    {:name   "out-item"
                                                :phrase "out"}
                               :correct-dialog {:name   "out-correct"
                                                :phrase "out correct"}}]
                 :background {:src "/raw/img/categorize-antonims/background.png"}}))
