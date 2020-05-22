(ns webchange.editor-v2.graph-builder.graph.data.cinema.scene-translation)

(def data {:start                     {:path        [:start]
                                       :entity      :trigger
                                       :children    []
                                       :connections #{{:handler :intro
                                                       :name    "next"}}}
           :intro                     {:path        [:intro]
                                       :entity      :action
                                       :children    [:intro-0
                                                     :intro-1
                                                     :intro-2
                                                     :intro-3
                                                     :intro-4
                                                     :intro-5
                                                     :intro-6
                                                     :intro-7
                                                     :intro-8
                                                     :intro-9
                                                     :intro-10
                                                     :intro-11
                                                     :intro-12
                                                     :intro-13
                                                     :intro-14
                                                     :intro-15
                                                     :intro-16
                                                     :intro-17]
                                       :connections #{{:handler :vaca-voice-finish
                                                       :name    "next"}
                                                      {:handler :vaca-voice-one-more-round
                                                       :name    "next"}
                                                      {:handler :vaca-voice-next
                                                       :name    "next"}}}
           :vaca-voice-next           {:path        [:vaca-voice-next]
                                       :entity      :action
                                       :children    [:vaca-voice-next-1 :vaca-voice-next-2]
                                       :connections #{{:handler :chant-current-letter
                                                       :name    "next"}}}
           :chant-current-letter      {:path        [:chant-current-letter]
                                       :entity      :action
                                       :children    [:chant-current-letter-0
                                                     :chant-current-letter-1
                                                     :chant-current-letter-2
                                                     :chant-current-letter-3
                                                     :chant-current-letter-4
                                                     :chant-current-letter-5
                                                     :chant-current-letter-6]
                                       :connections #{}}
           :vaca-voice-finish         {:path        [:vaca-voice-finish]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :vaca-voice-one-more-round {:path        [:vaca-voice-one-more-round]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}})
