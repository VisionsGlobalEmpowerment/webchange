(ns webchange.demo-scenes.library.painting-tablet)

(def painting-tablet-scene
  {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/library/painting-tablet/brush.png", :size 10 :type "image"}
                   {:url "/raw/img/library/painting-tablet/felt-tip.png", :size 10 :type "image"}
                   {:url "/raw/img/library/painting-tablet/pencil.png", :size 10 :type "image"}
                   {:url "/raw/img/library/painting-tablet/eraser.png", :size 10 :type "image"}
                   {:url "/raw/img/library/painting-tablet/back.png", :size 10 :type "image"}]
   :objects       {:background     {:type       "background",
                                    :scene-name "background",
                                    :src        "/raw/img/library/painting-tablet/background.jpg"}
                   :painting-area  {:scene-name "painting-area"
                                    :type       "painting-area"
                                    :var-name   "painting-tablet-image"
                                    :x          0
                                    :y          0
                                    :width      1920
                                    :height     1080
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :states     {:default {}}}
                   :felt-tip       {:type       "image"
                                    :scene-name "felt-tip"
                                    :x          -316
                                    :y          213
                                    :width      590
                                    :height     150
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :src        "/raw/img/library/painting-tablet/felt-tip.png"
                                    :states     {:active   {:x -216}
                                                 :inactive {:x -316}}
                                    :actions    {:click {:type   "action"
                                                         :id     "set-current-tool"
                                                         :params {:tool "felt-tip"}
                                                         :on     "click"}}}
                   :brush          {:type       "image"
                                    :scene-name "brush"
                                    :x          -280
                                    :y          410
                                    :width      540
                                    :height     140
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :src        "/raw/img/library/painting-tablet/brush.png"
                                    :states     {:active   {:x -180}
                                                 :inactive {:x -280}}
                                    :actions    {:click {:type   "action"
                                                         :id     "set-current-tool"
                                                         :params {:tool "brush"}
                                                         :on     "click"}}}
                   :pencil         {:type       "image"
                                    :scene-name "pencil"
                                    :x          -283
                                    :y          602
                                    :width      550
                                    :height     135
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :src        "/raw/img/library/painting-tablet/pencil.png"
                                    :states     {:active   {:x -183}
                                                 :inactive {:x -283}}
                                    :actions    {:click {:type   "action"
                                                         :id     "set-current-tool"
                                                         :params {:tool "pencil"}
                                                         :on     "click"}}}
                   :eraser         {:type       "image"
                                    :scene-name "eraser"
                                    :x          -118
                                    :y          794
                                    :width      380
                                    :height     175
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :src        "/raw/img/library/painting-tablet/eraser.png"
                                    :states     {:active   {:x -18}
                                                 :inactive {:x -118}}
                                    :actions    {:click {:type   "action"
                                                         :id     "set-current-tool"
                                                         :params {:tool "eraser"}
                                                         :on     "click"}}}
                   :back           {:type       "image"
                                    :scene-name "back"
                                    :x          50
                                    :y          50
                                    :width      100
                                    :height     100
                                    :scale-x    1
                                    :scale-y    1
                                    :rotation   0
                                    :src        "/raw/img/library/painting-tablet/back.png"
                                    :actions    {:click {:type "action"
                                                         :id   "finish-activity"
                                                         :on   "click"}}}
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
   :scene-objects [["background"] ["painting-area"] ["felt-tip" "brush" "pencil" "eraser" "colors-palette"] ["back"]]
   :actions       {:clear-instruction {:type        "remove-flows"
                                       :description "Remove flows"
                                       :flow-tag    "instruction"}
                   :start-activity    {:type        "sequence"
                                       :description "Initial action"
                                       :data        ["clear-instruction"
                                                     "reset-tools"
                                                     "init-current-tool"]}
                   :finish-activity   {:type        "sequence-data"
                                       :description "Finishing action"
                                       :data        [{:type "finish-activity" :id "painting-tablet"}
                                                     {:type "scene" :scene-id "library"}]}
                   :reset-tools       {:type "parallel"
                                       :data [{:type "state" :id "inactive" :target "felt-tip"}
                                              {:type "state" :id "inactive" :target "brush"}
                                              {:type "state" :id "inactive" :target "pencil"}
                                              {:type "state" :id "inactive" :target "eraser"}]}
                   :init-current-tool {:type   "action" :id "set-current-tool"
                                       :params {:tool "felt-tip"}}
                   :set-current-tool  {:type "sequence-data"
                                       :data [{:type        "state"
                                               :target      "painting-area"
                                               :id          "default"
                                               :from-params [{:action-property "value" :param-property "tool"}]}
                                              {:type     "state"
                                               :id       "inactive"
                                               :from-var [{:var-name "current-tool" :action-property "target"}]}
                                              {:type        "state"
                                               :id          "active"
                                               :from-params [{:action-property "target" :param-property "tool"}]}
                                              {:type        "set-variable" :var-name "current-tool"
                                               :from-params [{:action-property "var-value" :param-property "tool"}]}]}
                   :set-current-color {:type        "state"
                                       :target      "painting-area"
                                       :id          "default"
                                       :from-params [{:action-property "value" :param-property "color"}]}}
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev      "map"}})
