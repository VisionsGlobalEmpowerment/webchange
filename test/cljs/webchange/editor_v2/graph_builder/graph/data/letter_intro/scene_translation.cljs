(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.scene-translation)

(def data {:senora-vaca             {:path        [:senora-vaca]
                                     :entity      :object
                                     :children    []
                                     :connections #{{:previous :root ;;extra connection
                                                     :name     "click"
                                                     :handler  :stage-intro}
                                                    {:previous :root
                                                     :name     "click"
                                                     :handler  :current-sound}}}
           :mari-voice-finish       {:path        [:mari-voice-finish]
                                     :entity      :action
                                     :children    []
                                     :connections #{}}
           :touch-small-letter      {:path        [:touch-small-letter]
                                     :entity      :action
                                     :children    [:touch-small-letter-0
                                                   :touch-small-letter-1
                                                   :touch-small-letter-2
                                                   :touch-small-letter-3]
                                     :connections #{}}
           :letter-small            {:path        [:letter-small]
                                     :entity      :object
                                     :children    []
                                     :connections #{{:previous :root
                                                     :name     "click"
                                                     :handler  :stage-big}}}
           :start                   {:path        [:start]
                                     :entity      :trigger
                                     :children    []
                                     :connections #{{:previous :root
                                                     :name     "start"
                                                     :handler  :stage-intro}
                                                    {:previous :root
                                                     :name     "start"
                                                     :handler  :current-sound}}}
           :letter-big              {:path        [:letter-big]
                                     :entity      :object
                                     :children    []
                                     :connections #{{:previous :root
                                                     :name     "click"
                                                     :handler  :mari-voice-finish}}}
           :stage-small             {:path        [:stage-small]
                                     :entity      :action
                                     :children    [:stage-small-0
                                                   :stage-small-1
                                                   :stage-small-2
                                                   :stage-small-3
                                                   :stage-small-4
                                                   :stage-small-5
                                                   :stage-small-6
                                                   :stage-small-7
                                                   :stage-small-8
                                                   :stage-small-9
                                                   :stage-small-10
                                                   :stage-small-11
                                                   :stage-small-12
                                                   :stage-small-13
                                                   :stage-small-14
                                                   :stage-small-15
                                                   :stage-small-16
                                                   :stage-small-17
                                                   :stage-small-18
                                                   :stage-small-19]
                                     :connections #{{:previous :vaca-voice-2-ways-write
                                                     :name     "next"
                                                     :sequence :stage-small
                                                     :handler  :touch-small-letter}}}
           :stage-intro             {:path        [:stage-intro]
                                     :entity      :action
                                     :children    [:stage-intro-0
                                                   :stage-intro-1
                                                   :stage-intro-2
                                                   :stage-intro-3]
                                     :connections #{{:previous :start
                                                     :name     "next"
                                                     :sequence :stage-intro
                                                     :handler  :current-sound}
                                                    {:previous :senora-vaca ;;extra connection
                                                     :name     "next"
                                                     :sequence :stage-intro
                                                     :handler  :current-sound}}}
           :stage-big               {:path        [:stage-big]
                                     :entity      :action
                                     :children    [:stage-big-0
                                                   :stage-big-1
                                                   :stage-big-2
                                                   :stage-big-3
                                                   :stage-big-4
                                                   :stage-big-5
                                                   :stage-big-6
                                                   :stage-big-7
                                                   :stage-big-8
                                                   :stage-big-9
                                                   :stage-big-10
                                                   :stage-big-11
                                                   :stage-big-12
                                                   :stage-big-13]
                                     :connections #{{:previous :letter-small
                                                     :name     "next"
                                                     :sequence :stage-big
                                                     :handler  :touch-big-letter}}}
           :touch-big-letter        {:path        [:touch-big-letter]
                                     :entity      :action
                                     :children    [:touch-big-letter-0
                                                   :touch-big-letter-1
                                                   :touch-big-letter-2
                                                   :touch-big-letter-3]
                                     :connections #{}}
           :vaca-voice-2-ways-write {:path        [:vaca-voice-2-ways-write]
                                     :entity      :action
                                     :children    [:vaca-voice-2-ways-write-0
                                                   :vaca-voice-2-ways-write-1
                                                   :vaca-voice-2-ways-write-2
                                                   :vaca-voice-2-ways-write-3
                                                   :vaca-voice-2-ways-write-4]
                                     :connections #{{:previous :current-sound
                                                     :name     "next"
                                                     :sequence :vaca-voice-2-ways-write
                                                     :handler  :stage-small}}}
           :current-sound           {:path        [:current-sound]
                                     :entity      :action
                                     :children    [:current-sound-0
                                                   :current-sound-1
                                                   :current-sound-2
                                                   :current-sound-3
                                                   :current-sound-4
                                                   :current-sound-5
                                                   :current-sound-6
                                                   :current-sound-7
                                                   :current-sound-8
                                                   :current-sound-9
                                                   :current-sound-10
                                                   :current-sound-11
                                                   :current-sound-12
                                                   :current-sound-13
                                                   :current-sound-14]
                                     :connections #{{:previous :senora-vaca
                                                     :name     "next"
                                                     :sequence :current-sound
                                                     :handler  :vaca-voice-2-ways-write}
                                                    {:previous :start ;;extra connection
                                                     :name     "next"
                                                     :sequence :current-sound
                                                     :handler  :vaca-voice-2-ways-write}
                                                    {:previous :stage-intro
                                                     :name     "next"
                                                     :sequence :current-sound
                                                     :handler  :vaca-voice-2-ways-write}}}})
