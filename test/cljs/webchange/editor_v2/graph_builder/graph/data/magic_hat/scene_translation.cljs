(ns webchange.editor-v2.graph-builder.graph.data.magic-hat.scene-translation)

(def data {:mari-says-correct-answer {:path        [:mari-says-correct-answer]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :current-concept-chant    {:path        [:current-concept-chant]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :start                    {:path        [:start]
                                      :entity      :trigger
                                      :children    []
                                      :connections #{{:handler :intro
                                                      :name    "next"}}}
           :mari-says-wrong-answer   {:path        [:mari-says-wrong-answer]
                                      :entity      :action
                                      :children    [:mari-says-wrong-answer-0
                                                    :mari-says-wrong-answer-1
                                                    :mari-says-wrong-answer-2
                                                    :mari-says-wrong-answer-3
                                                    :mari-says-wrong-answer-4
                                                    :mari-says-wrong-answer-5
                                                    :mari-says-wrong-answer-6
                                                    :mari-says-wrong-answer-7
                                                    :mari-says-wrong-answer-8
                                                    :mari-says-wrong-answer-9
                                                    :mari-says-wrong-answer-10]
                                      :connections #{}}
           :box3                     {:path        [:box3]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-says-correct-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-chant
                                                      :name    "next"}
                                                     {:handler :mari-says-wrong-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-sound-x3
                                                      :name    "next"}}}
           :current-concept-sound-x3 {:path        [:current-concept-sound-x3]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :box1                     {:path        [:box1]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-says-correct-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-chant
                                                      :name    "next"}
                                                     {:handler :mari-says-wrong-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-sound-x3
                                                      :name    "next"}}}
           :intro                    {:path        [:intro]
                                      :entity      :action
                                      :children    [:mari-voice-welcome :mari-flies-to-hat :mari-init-wand :mari-voice-intro]
                                      :connections #{}}
           :box2                     {:path        [:box2]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :mari-says-correct-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-chant
                                                      :name    "next"}
                                                     {:handler :mari-says-wrong-answer
                                                      :name    "next"}
                                                     {:handler :current-concept-sound-x3
                                                      :name    "next"}}}})
