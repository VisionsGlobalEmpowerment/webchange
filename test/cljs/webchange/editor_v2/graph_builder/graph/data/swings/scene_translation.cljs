(ns webchange.editor-v2.graph-builder.graph.data.swings.scene-translation)

(def data {:mari-finish        {:path        [:mari-finish]
                                :entity      :action
                                :children    [:mari-finish-0 :mari-finish-1]
                                :connections #{}}
           :welcome-audio      {:path        [:welcome-audio]
                                :entity      :action
                                :children    [:vera-welcome-audio-1
                                              :mari-welcome-audio-2
                                              :vera-welcome-audio-3
                                              :mari-welcome-audio-4
                                              :rock-welcome-audio-5
                                              :mari-welcome-audio-6
                                              :rock-welcome-audio-7
                                              :vera-welcome-audio-8
                                              :rock-welcome-audio-9
                                              :mari-welcome-audio-10]
                                :connections #{{:handler :mari-move-to-start
                                                :name    "next"}}}
           :start              {:path        [:start]
                                :entity      :trigger
                                :children    []
                                :connections #{{:handler :welcome-audio
                                                :name    "next"}}}
           :try-another        {:path        [:try-another]
                                :entity      :action
                                :children    []
                                :connections #{}}
           :dialog-var         {:path        [:dialog-var]
                                :entity      :action
                                :children    []
                                :connections #{{:handler :try-another
                                                :name    "next"}
                                               {:handler :mari-finish
                                                :name    "next"}}}
           :box3               {:path        [:box3]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :dialog-var
                                                :name    "next"}}}
           :box1               {:path        [:box1]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :dialog-var
                                                :name    "next"}}}
           :mari-move-to-start {:path        [:mari-move-to-start]
                                :entity      :action
                                :children    [:mari-move-to-start-0 :mari-move-to-start-1]
                                :connections #{}}
           :box2               {:path        [:box2]
                                :entity      :object
                                :children    []
                                :connections #{{:handler :dialog-var
                                                :name    "next"}}}}
  )
