(ns webchange.editor-v2.graph-builder.graph.data.cycling-letters.scene-translation)

(def data {:mari-correct             {:path        [:mari-correct]
                                      :entity      :action
                                      :children    [:mari-correct-0 :mari-correct-1 :mari-correct-2]
                                      :connections #{{:handler :current-concept-sound-x3
                                                      :name    "next"}}}
           :letter2                  {:path        [:letter2]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}
           :start                    {:path        [:start]
                                      :entity      :trigger
                                      :children    []
                                      :connections #{{:handler :mari-welcome-audio
                                                      :name    "next"}}}
           :mari-wrong               {:path        [:mari-wrong]
                                      :entity      :action
                                      :children    [:mari-wrong-0 :mari-wrong-1 :mari-wrong-2]
                                      :connections #{}}
           :mari-welcome-audio       {:path        [:mari-welcome-audio]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:handler :current-concept-sound-x3
                                                      :name    "next"}}}
           :box3                     {:path        [:box3]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}
           :letter3                  {:path        [:letter3]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}
           :current-concept-sound-x3 {:path        [:current-concept-sound-x3]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :letter1                  {:path        [:letter1]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}
           :box1                     {:path        [:box1]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}
           :box2                     {:path        [:box2]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-correct
                                                      :name    "next"}
                                                     {:handler :mari-wrong
                                                      :name    "next"}}}})
