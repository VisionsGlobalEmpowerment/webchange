(ns webchange.editor-v2.graph-builder.graph.phrases-graph--cinema-video-expected)

(def data {:start                 {:path        [:start]
                                   :entity      :trigger
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "start"
                                                   :handler  :vaca-voice-wonderful}}}
           :vaca-voice-wonderful  {:path        [:vaca-voice-wonderful]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :start
                                                   :name     "next"
                                                   :sequence :start-scene
                                                   :handler  :vaca-voice-lets-watch}}}
           :vaca-voice-lets-watch {:path        [:vaca-voice-lets-watch]
                                   :entity      :action
                                   :children    [:vaca-voice-lets-watch-0
                                                 :vaca-voice-lets-watch-1]
                                   :connections #{{:previous :vaca-voice-wonderful
                                                   :name     "next"
                                                   :sequence :vaca-voice-lets-watch
                                                   :handler  :mari-voice-touch}}}
           :mari-voice-touch      {:path        [:mari-voice-touch]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :play-button           {:path        [:play-button]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "click"
                                                   :handler  :play-video-finish}}}
           :play-video-finish     {:path        [:play-video-finish]
                                   :entity      :action
                                   :children    [:play-video-finish-0
                                                 :play-video-finish-1
                                                 :play-video-finish-2
                                                 :play-video-finish-3
                                                 :play-video-finish-4]
                                   :connections #{}}})
