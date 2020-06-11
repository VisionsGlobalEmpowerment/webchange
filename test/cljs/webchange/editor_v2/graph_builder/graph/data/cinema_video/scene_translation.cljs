(ns webchange.editor-v2.graph-builder.graph.data.cinema-video.scene-translation)
(def data {:start                    {:path        [:start]
                                      :entity      :trigger
                                      :children    []
                                      :connections #{{:handler :vaca-voice-wonderful
                                                      :name    "next"}}}
           :vaca-voice-wonderful     {:path        [:vaca-voice-wonderful]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:handler :vaca-voice-lets-watch
                                                      :name    "next"}}}
           :vaca-voice-lets-watch    {:path        [:vaca-voice-lets-watch]
                                      :entity      :action
                                      :children    [:vaca-voice-lets-watch-0 :vaca-voice-lets-watch-1]
                                      :connections #{{:handler :mari-voice-touch
                                                      :name    "next"}}}
           :mari-voice-touch         {:path        [:mari-voice-touch]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :play-button              {:path        [:play-button]
                                      :entity      :object
                                      :children    []
                                      :connections #{{:handler :vaca-voice-very-good-1-1
                                                      :name    "next"}
                                                     {:handler :mari-voice-touch-again-1
                                                      :name    "next"}
                                                     {:handler :mari-voice-touch-again-2
                                                      :name    "next"}}}
           :mari-voice-touch-again-1 {:path        [:mari-voice-touch-again-1]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:handler :mari-voice-touch-again-2
                                                      :name    "next"}}}
           :vaca-voice-very-good-1-1 {:path        [:vaca-voice-very-good 1 1]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:handler :mari-voice-touch-again-1
                                                      :name    "next"}
                                                     {:handler :mari-voice-touch-again-2
                                                      :name    "next"}}}
           :mari-voice-touch-again-2 {:path        [:mari-voice-touch-again-2]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}})
