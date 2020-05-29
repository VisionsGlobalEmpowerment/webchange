(ns webchange.editor-v2.graph-builder.graph.data.cinema-video.scene-translation)
(def data {:start                 {:path        [:start]
                                   :entity      :trigger
                                   :children    []
                                   :connections #{{:handler :vaca-voice-wonderful
                                                   :name    "next"}}}
           :vaca-voice-wonderful  {:path        [:vaca-voice-wonderful]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:handler :vaca-voice-lets-watch
                                                   :name    "next"}}}
           :vaca-voice-lets-watch {:path        [:vaca-voice-lets-watch]
                                   :entity      :action
                                   :children    [:vaca-voice-lets-watch-0 :vaca-voice-lets-watch-1]
                                   :connections #{{:handler :mari-voice-touch
                                                   :name    "next"}}}
           :mari-voice-touch      {:path        [:mari-voice-touch]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :play-button           {:path        [:play-button]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :play-video-finish
                                                   :name    "next"}}}
           :play-video-finish     {:path        [:play-video-finish]
                                   :entity      :action
                                   :children    [:play-video-finish-0
                                                 :play-video-finish-1
                                                 :play-video-finish-2
                                                 :play-video-finish-3
                                                 :play-video-finish-4]
                                   :connections #{}}})
