(ns webchange.editor-v2.graph-builder.graph.phrases-graph--volleyball-letters-expected)

(def data {:box1               {:path        [:box1]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :box2               {:path        [:box2]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :box3               {:path        [:box3]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}}}
           :box4               {:path        [:box4]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :letter1            {:path        [:letter1]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}}}
           :letter2            {:path        [:letter2]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :letter3            {:path        [:letter3]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :letter4            {:path        [:letter4]
                                :entity      :object
                                :children    []
                                :connections #{{:previous :root
                                                :name     "click"
                                                :handler  :mari-correct}
                                               {:previous :root
                                                :name     "click"
                                                :handler  :mari-wrong}}}
           :mari-correct       {:path        [:mari-correct]
                                :entity      :action
                                :children    [:mari-correct-0 :mari-correct-1 :mari-correct-2]
                                :connections #{{:previous :letter2
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :box1
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :box4
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :box3
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter1
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :box2
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :box2
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter3
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter2
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :box1
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :box3
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :box4
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter1
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :letter3
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :letter4
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :voice-high-var}
                                               {:previous :letter4
                                                :name     "next"
                                                :sequence :mari-correct
                                                :handler  :mari-welcome-audio}}}
           :start              {:path        [:start]
                                :entity      :trigger
                                :children    []
                                :connections #{{:previous :root
                                                :name     "start"
                                                :handler  :voice-high-var}
                                               {:previous :root
                                                :name     "start"
                                                :handler  :mari-welcome-audio}}}
           :mari-wrong         {:path        [:mari-wrong]
                                :entity      :action
                                :children    [:mari-wrong-0 :mari-wrong-1 :mari-wrong-2]
                                :connections #{{:previous :box1
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter2
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :letter3
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :box1
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :letter4
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :box2
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :box4
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter1
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :letter4
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :box2
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :box3
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter1
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter2
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :mari-welcome-audio}
                                               {:previous :letter3
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :box4
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}
                                               {:previous :box3
                                                :name     "next"
                                                :sequence :mari-wrong
                                                :handler  :voice-high-var}}}
           :mari-welcome-audio {:path        [:mari-welcome-audio]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :start
                                                :name     "next"
                                                :sequence :start-scene
                                                :handler  :voice-high-var}
                                               {:previous :mari-correct
                                                :name     "next"
                                                :sequence :start-scene
                                                :handler  :voice-high-var}
                                               {:previous :mari-wrong
                                                :name     "next"
                                                :sequence :start-scene
                                                :handler  :voice-high-var}
                                               {:previous :throw-5
                                                :name     "next"
                                                :sequence :start-scene
                                                :handler  :voice-high-var}}}
           :voice-high-var     {:path        [:voice-high-var]
                                :entity      :action
                                :children    []
                                :connections #{}}})
