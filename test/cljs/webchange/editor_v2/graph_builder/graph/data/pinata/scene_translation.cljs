(ns webchange.editor-v2.graph-builder.graph.data.pinata.scene-translation)

(def data {:start              {:path        [:start]
                                :entity      :trigger
                                :children    []
                                :connections #{{:handler :mari-voice-welcome
                                                :name    "next"}}}
           :mari-voice-welcome {:path        [:mari-voice-welcome]
                                :entity      :action
                                :children    []
                                :connections #{}}
           :box1               {:path        [:box1]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :mari-correct
                                                :name    "next"}
                                               {:handler :mari-wrong
                                                :name    "next"}}}
           :box2               {:path        [:box2]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :mari-correct
                                                :name    "next"}
                                               {:handler :mari-wrong
                                                :name    "next"}}}
           :box3               {:path        [:box3]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :mari-correct
                                                :name    "next"}
                                               {:handler :mari-wrong
                                                :name    "next"}}}
           :mari-correct       {:path        [:mari-correct]
                                :entity      :action
                                :children    [:mari-correct-0 :mari-correct-1 :mari-correct-2]
                                :connections #{{:handler :mari-says-task
                                                :name    "next"}}}
           :mari-wrong         {:path        [:mari-wrong]
                                :entity      :action
                                :children    [:mari-wrong-0 :mari-wrong-1 :mari-wrong-2]
                                :connections #{{:handler :mari-correct
                                                :name    "next"}}}
           :mari-says-task     {:path        [:mari-says-task]
                                :entity      :action
                                :children    [:mari-says-task-0
                                              :mari-says-task-1
                                              :mari-says-task-2
                                              :mari-says-task-3
                                              :mari-says-task-4
                                              :mari-says-task-5]
                                :connections #{}}})
