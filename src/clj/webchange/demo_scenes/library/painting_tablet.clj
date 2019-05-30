(ns webchange.demo-scenes.library.painting-tablet)

(def painting-tablet-scene
  {:assets        [{:url "/raw/img/library/painting-tablet/background.png", :size 10 :type "image"}
                   {:url "/raw/img/feria/back.png", :size 10 :type "image"}],
   :objects       {:background    {:type       "background",
                                   :scene-name "background",
                                   :src        "/raw/img/library/painting-tablet/background.png"}
                   :painting-area {:scene-name "painting-area"
                                   :type       "painting-area"
                                   :var-name   "current-painting"
                                   :x          115
                                   :y          130
                                   :width      1689
                                   :height     785
                                   :scale-x    1
                                   :scale-y    1
                                   :rotation   0}
                   :back          {:scene-name "back"
                                   :type       "image"
                                   :x          1675
                                   :y          875
                                   :width      240
                                   :height     230
                                   :scale-x    1
                                   :scale-y    1
                                   :rotation   0
                                   :src        "/raw/img/feria/back.png"
                                   :actions    {:click {:type "action"
                                                        :id   "finish-activity"
                                                        :on   "click"}}}}
   :scene-objects [["background"] ["painting-area"] ["back"]],
   :actions       {:clear-instruction {:type        "remove-flows"
                                       :description "Remove flows"
                                       :flow-tag    "instruction"}
                   :start-activity    {:type        "sequence"
                                       :description "Initial action"
                                       :data        ["clear-instruction"]}
                   :finish-activity   {:type        "sequence-data"
                                       :description "Finishing action"
                                       :data        [{:type "set-progress" :var-name "painting"
                                                      :from-var [{:var-name "current-painting" :action-property "var-value"}]}
                                                     {:type "finish-activity" :id "hide-n-seek"}
                                                     {:type "scene" :scene-id "map"}]}},
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev      "map"}})
