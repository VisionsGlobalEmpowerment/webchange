(ns webchange.demo-scenes.test)

(def test-scene
  {:assets        [{:url "/raw/img/park/slide/background.jpg" :type "image"}
                   {:url "/raw/audio/l2/a5/L2_A5_Mari.m4a" :type "audio"}]
   :audio         {:mari "/raw/audio/l2/a5/L2_A5_Mari.m4a"}
   :objects       {:background    {:type "background"
                                   :src  "/raw/img/park/slide/background.jpg"}
                   :box1          {:type       "animation"
                                   :name       "boxes"
                                   :skin       "qwestion"
                                   :scene-name "box1"
                                   :transition "box1"
                                   :x          710
                                   :y          236
                                   :width      671
                                   :height     633
                                   :scale      {:x 0.18 :y 0.18}
                                   :anim       "idle2"
                                   :speed      0.3
                                   :start      true
                                   :loop       true
                                   :actions    {:click {:type "action"
                                                        :description "click box"
                                                        :id "pick-box-1"
                                                        :on "click"
                                                        :options {:unique-tag "click"}}}}
                   :box2          {:type       "animation"
                                   :name       "boxes"
                                   :skin       "qwestion"
                                   :scene-name "box1"
                                   :transition "box1"
                                   :x          1010
                                   :y          236
                                   :width      671
                                   :height     633
                                   :scale      {:x 0.18 :y 0.18}
                                   :anim       "idle2"
                                   :speed      0.3
                                   :start      true
                                   :loop       true
                                   :actions    {:click {:type "action"
                                                        :description "click box 2"
                                                        :id "pick-box-2"
                                                        :on "click"}}}}
   :scene-objects [["background"] ["box1" "box2"]]
   :actions       {:start                  {:type "sequence"
                                            :data ["start-activity"
                                                   "clear-instruction"]}
                   :stop                   {:type "sequence" :data ["stop-activity"]}
                   :start-activity         {:type "start-activity" :id "slide"}
                   :stop-activity          {:type "stop-activity" :id "slide"}
                   :clear-instruction      {:type "remove-flows" :flow-tag "instruction"}

                   :pick-box-1             {:type "sequence-data"
                                            :tags ["q"]
                                            :options {:unique-tag "q"}
                                            :data [{:type "action" :id "mari-voice-wrong"}]}

                   :pick-box-2             {:type "sequence-data"
                                            :unique-tag "box-2"
                                            :data [{:type "action" :id "mari-voice-wrong-2"}]}

                   :mari-voice-wrong       {:type "audio" :id "mari" :start 29.581 :duration 3.120
                                            :tags ["voice"]
                                            :options {:unique-tag "voice"}
                                            :description "mari voice"}

                   :mari-voice-wrong-2       {:type "audio" :id "mari" :start 29.581 :duration 3.120
                                            :description "mari voice 2"}}
   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      "park"}})
