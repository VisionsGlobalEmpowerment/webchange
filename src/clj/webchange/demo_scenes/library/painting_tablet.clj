(ns webchange.demo-scenes.library.painting-tablet)

(def painting-tablet-scene
  {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg", :size 10 :type "image"}]
   :objects       {:background     {:type       "background",
                                    :scene-name "background",
                                    :src        "/raw/img/library/painting-tablet/background.jpg"}
                   :painting-area  {:scene-name "painting-area"
                                    :type       "painting-area"
                                    :var-name   "current-painting"
                                    :x          0
                                    :y          0
                                    :width      1920
                                    :height     1080
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :states     {:default {}}}
                   :colors-palette {:scene-name "colors-palette"
                                    :type       "colors-palette"
                                    :var-name   "current-color"
                                    :x          1680
                                    :y          0
                                    :width      180
                                    :height     1080
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :colors     ["#4479bb" "#92bd4a" "#ed91aa" "#fdc531" "#010101"]
                                    :actions    {:change {:type             "action"
                                                          :id               "set-current-color"
                                                          :on               "change"
                                                          :pick-event-param "color"}}}}
   :scene-objects [["background"] ["painting-area"] ["colors-palette"]],
   :actions       {:clear-instruction {:type        "remove-flows"
                                       :description "Remove flows"
                                       :flow-tag    "instruction"}
                   :start-activity    {:type        "sequence"
                                       :description "Initial action"
                                       :data        ["clear-instruction"]}
                   :finish-activity   {:type        "sequence-data"
                                       :description "Finishing action"
                                       :data        [{:type     "set-progress" :var-name "painting"
                                                      :from-var [{:var-name "current-painting" :action-property "var-value"}]}
                                                     {:type "finish-activity" :id "hide-n-seek"}
                                                     {:type "scene" :scene-id "map"}]}
                   :set-current-color {:type        "state"
                                       :target      "painting-area"
                                       :id          "default"
                                       :from-params [{:action-property "value" :param-property "color"}]
                                       }}
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev      "map"}})
