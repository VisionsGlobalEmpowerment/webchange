(ns webchange.editor-v2.graph-builder.graph.data.cycling-letters.scene-translation)

(def data {:start                    {:path        [:start]
                                      :entity      :trigger
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "start"
                                                      :handler  :mari-welcome-audio}}}
           :mari-welcome-audio       {:path        [:mari-welcome-audio]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :start
                                                      :name     "next"
                                                      :sequence :start-scene
                                                      :handler  :current-concept-sound-x3}}}
           :current-concept-sound-x3 {:path        [:current-concept-sound-x3]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :box1                     {:path        [:box1]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :box2                     {:path        [:box2]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :box3                     {:path        [:box3]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :letter1                  {:path        [:letter1]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :letter2                  {:path        [:letter2]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :letter3                  {:path        [:letter3]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "click"
                                                      :handler  :mari-correct}
                                                     {:previous :root
                                                      :name     "click"
                                                      :handler  :mari-wrong}}}
           :mari-correct             {:path        [:mari-correct]
                                      :entity      :action
                                      :children    [:mari-correct-0 :mari-correct-1 :mari-correct-2]
                                      :connections #{{:previous :box1
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}
                                                     {:previous :letter1
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}
                                                     {:previous :letter3
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}
                                                     {:previous :box3
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}
                                                     {:previous :box2
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}
                                                     {:previous :letter2
                                                      :name     "next"
                                                      :sequence :mari-correct
                                                      :handler  :current-concept-sound-x3}}}
           :mari-wrong               {:path        [:mari-wrong]
                                      :entity      :action
                                      :children    [:mari-wrong-0 :mari-wrong-1 :mari-wrong-2]
                                      :connections #{}}})
