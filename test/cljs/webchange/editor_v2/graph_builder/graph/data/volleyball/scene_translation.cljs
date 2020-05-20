(ns webchange.editor-v2.graph-builder.graph.data.volleyball.scene-translation)

(def data {:word-ardilla-high    {:path        [:word-ardilla-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box4                 {:path        [:box4]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "click"
                                                  :handler  :mari-say-correct}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :mari-audio-wrong}}}
           :start                {:path        [:start]
                                  :entity      :trigger
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "start"
                                                  :handler  :mari-welcome-audio-1}
                                                 {:previous :root
                                                  :name     "start"
                                                  :handler  :word-oso-high}
                                                 {:previous :root
                                                  :name     "start"
                                                  :handler  :word-incendio-high}
                                                 {:previous :root
                                                  :name     "start"
                                                  :handler  :word-ardilla-high}}}
           :box3                 {:path        [:box3]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "click"
                                                  :handler  :mari-say-correct}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :mari-audio-wrong}}}
           :mari-welcome-audio-1 {:path        [:mari-welcome-audio-1]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :start
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-ardilla-high}
                                                 {:previous :throw-from-player4
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-oso-high}
                                                 {:previous :throw-from-player4
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-incendio-high}
                                                 {:previous :throw-from-player1
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-incendio-high}
                                                 {:previous :throw-from-player1
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-oso-high}
                                                 {:previous :start
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-oso-high}
                                                 {:previous :throw-from-player3
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-ardilla-high}
                                                 {:previous :throw-from-player2
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-incendio-high}
                                                 {:previous :throw-from-player2
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-oso-high}
                                                 {:previous :throw-from-player1
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-ardilla-high}
                                                 {:previous :start
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-incendio-high}
                                                 {:previous :throw-from-player4
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-ardilla-high}
                                                 {:previous :throw-from-player3
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-oso-high}
                                                 {:previous :throw-from-player3
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-incendio-high}
                                                 {:previous :throw-from-player2
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :word-ardilla-high}}}
           :word-incendio-high   {:path        [:word-incendio-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box1                 {:path        [:box1]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "click"
                                                  :handler  :mari-say-correct}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :mari-audio-wrong}}}
           :mari-audio-wrong     {:path        [:mari-audio-wrong]
                                  :entity      :action
                                  :children    [:mari-audio-wrong-0 :mari-audio-wrong-1]
                                  :connections #{}}
           :word-oso-high        {:path        [:word-oso-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box2                 {:path        [:box2]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "click"
                                                  :handler  :mari-say-correct}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :mari-audio-wrong}}}
           :mari-say-correct     {:path        [:mari-say-correct]
                                  :entity      :action
                                  :children    [:mari-say-correct-1 :mari-say-correct-2]
                                  :connections #{}}})
