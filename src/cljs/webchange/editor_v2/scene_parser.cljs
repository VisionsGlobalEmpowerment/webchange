(ns webchange.editor-v2.scene-parser)

(defn get-scene-data
  [scene-id]
  {:objects {:global {:outs [{:event   :start
                              :handler :start-background-music}
                             {:event   :start
                              :handler :intro}
                             {:event   :back
                              :handler :stop-activity}]}
             :box1   {:outs [{:event   :click
                              :handler :click-on-box1}]}
             :box2   {:outs [{:event   :click
                              :handler :click-on-box2}]}
             :box3   {:outs [{:event   :click
                              :handler :click-on-box3}]}}
   :actions {:start-background-music {:type "audio"}
             :intro                  {:type "sequence"
                                      :outs [{:event   :next
                                              :handler :start-activity}]}
             :stop-activity          {:type "stop-activity"}
             :click-on-box1          {:type "test-var-scalar"
                                      :outs [{:event   :success
                                              :handler :first-word}
                                             {:event   :fail
                                              :handler :pick-wrong}]}
             :click-on-box2          {:type "test-var-scalar"
                                      :outs [{:event   :success
                                              :handler :second-word}
                                             {:event   :fail
                                              :handler :pick-wrong}]}
             :click-on-box3          {:type "test-var-scalar"
                                      :outs [{:event   :success
                                              :handler :third-word}
                                             {:event   :fail
                                              :handler :pick-wrong}]}
             :start-activity         {:type "start-activity"}
             :first-word             {:type "sequence"
                                      :outs [{:event   :next
                                              :handler :introduce-word}]}
             :second-word            {:type "sequence"
                                      :outs [{:event   :next
                                              :handler :introduce-word}]}
             :third-word             {:type "sequence"
                                      :outs [{:event   :next
                                              :handler :introduce-word}]}
             :introduce-word         {:type "empty"}
             :pick-wrong             {:type "audio"}}})
