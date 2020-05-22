(ns webchange.editor-v2.graph-builder.graph.data.see-saw.scene-translation)

(def data {:mari-finish        {:path        [:mari-finish]
                                :entity      :action
                                :children    [:mari-finish-0 :mari-finish-1]
                                :connections #{}}
           :start              {:path        [:start]
                                :entity      :trigger
                                :children    []
                                :connections #{{:handler :mari-welcome-audio
                                                :name    "next"}}}
           :try-another        {:path        [:try-another]
                                :entity      :action
                                :children    []
                                :connections #{{:handler :mari-move-to-start
                                                :name    "next"}}}
           :mari-welcome-audio {:path        [:mari-welcome-audio]
                                :entity      :action
                                :children    []
                                :connections #{{:handler :mari-move-to-start
                                                :name    "next"}}}
           :box3               {:path        [:box3]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :introduce-concept
                                                :name    "next"}}}
           :box1               {:path        [:box1]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :introduce-concept
                                                :name    "next"}}}
           :mari-move-to-start {:path        [:mari-move-to-start]
                                :entity      :action
                                :children    [:mari-move-to-start-0 :mari-move-to-start-1]
                                :connections #{}}
           :box2               {:path        [:box2]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :introduce-concept
                                                :name    "next"}}}
           :introduce-concept  {:path        [:introduce-concept]
                                :entity      :action
                                :children    [:go-down-1 :go-up-1 :go-down-2 :go-up-2]
                                :connections #{{:handler :try-another
                                                :name    "next"}
                                               {:handler :mari-finish
                                                :name    "next"}}}})
