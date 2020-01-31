(ns webchange.editor-v2.graph-builder.graph.phrases-graph--cycling-expected)

(def data {:mari-says-correct    {:path        [:mari-says-correct]
                                  :entity      :action
                                  :children    [:mari-says-correct-0 :mari-says-correct-1]
                                  :connections #{}}
           :mari-says-wrong      {:path        [:mari-says-wrong]
                                  :entity      :action
                                  :children    [:mari-says-wrong-0 :mari-says-wrong-1]
                                  :connections #{}}
           :start                {:path        [:start]
                                  :entity      :trigger
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "start"
                                                  :handler  :mari-welcome-audio-1}}}
           :mari-welcome-audio-1 {:path        [:mari-welcome-audio-1]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :start
                                                  :name     "next"
                                                  :sequence :start-scene
                                                  :handler  :voice-high-var}}}
           :voice-high-var       {:path        [:voice-high-var]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box2                 {:path        [:box2]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "click"
                                                  :handler  :mari-says-correct}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :mari-says-wrong}
                                                 {:previous :root
                                                  :name     "click"
                                                  :handler  :voice-high-var}}}})
