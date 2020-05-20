(ns webchange.editor-v2.graph-builder.graph.phrases-graph--pinata-expected)

(def data {:start              {:path        [:start]
                      :entity      :trigger
                      :children    []
                      :connections #{{:previous :root
                                      :name     "start"
                                      :handler  :mari-voice-welcome}}}
 :mari-voice-welcome {:path        [:mari-voice-welcome]
                      :entity      :action
                      :children    []
                      :connections #{}}
 :box1               {:path        [:box1]
                      :entity      :object
                      :children    []
                      :connections #{{:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-wrong}
                                     {:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-correct}}}
 :box2               {:path        [:box2]
                      :entity      :object
                      :children    []
                      :connections #{{:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-wrong}
                                     {:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-correct}}}
 :box3               {:path        [:box3]
                      :entity      :object
                      :children    []
                      :connections #{{:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-wrong}
                                     {:previous :root
                                      :name     "drag-end"
                                      :handler  :mari-correct}}}
 :mari-correct       {:path        [:mari-correct]
                      :entity      :action
                      :children    [:mari-correct-0
                                    :mari-correct-1
                                    :mari-correct-2]
                      :connections #{{:previous :box1
                                      :name     "next"
                                      :sequence :mari-correct
                                      :handler  :mari-says-task}
                                     {:previous :box2
                                      :name     "next"
                                      :sequence :mari-correct
                                      :handler  :mari-says-task}
                                     {:previous :box3
                                      :name     "next"
                                      :sequence :mari-correct
                                      :handler  :mari-says-task}
                                     {:previous :mari-voice-act-3
                                      :name     "next"
                                      :sequence :mari-correct
                                      :handler  :mari-says-task}
                                     {:previous :mari-wrong
                                      :name     "next"
                                      :sequence :mari-correct
                                      :handler  :mari-says-task}}}
 :mari-wrong         {:path        [:mari-wrong]
                      :entity      :action
                      :children    [:mari-wrong-0
                                    :mari-wrong-1
                                    :mari-wrong-2]
                      :connections #{{:previous :box1
                                      :name     "next"
                                      :sequence :mari-wrong
                                      :handler  :mari-correct}
                                     {:previous :box2
                                      :name     "next"
                                      :sequence :mari-wrong
                                      :handler  :mari-correct}
                                     {:previous :box3
                                      :name     "next"
                                      :sequence :mari-wrong
                                      :handler  :mari-correct}}}
 :mari-says-task     {:path        [:mari-says-task]
                      :entity      :action
                      :children    [:mari-says-task-0
                                    :mari-says-task-1
                                    :mari-says-task-2
                                    :mari-says-task-3
                                    :mari-says-task-4
                                    :mari-says-task-5]
                      :connections #{}}})
