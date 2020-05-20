(ns webchange.editor-v2.graph-builder.graph.data.home.scene-translation)

(def data {:senora-vaca                        {:path        [:senora-vaca]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:previous :root :name "click" :handler :senora-vaca-audio-1}}}
           :senora-vaca-audio-touch-second-box {:path        [:senora-vaca-audio-touch-second-box]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}
           :concept-intro                      {:path        [:concept-intro]
                                                :entity      :action
                                                :children    [:vaca-this-is-var
                                                              :empty-small
                                                              :vaca-can-you-say
                                                              :vaca-question-var
                                                              :empty-small
                                                              :vaca-word-var
                                                              :empty-small-1
                                                              :group-word-var]
                                                :connections #{{:previous :box3
                                                                :name     "next"
                                                                :sequence :concept-intro
                                                                :handler  :concept-chant}
                                                               {:previous :box1
                                                                :name     "next"
                                                                :sequence :concept-intro
                                                                :handler  :concept-chant}
                                                               {:previous :box2
                                                                :name     "next"
                                                                :sequence :concept-intro
                                                                :handler  :concept-chant}}}
           :vaca-goodbye-var                   {:path        [:vaca-goodbye-var]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :concept-chant
                                                                :name     "next"
                                                                :sequence :introduce-word
                                                                :handler  :mari-finish}
                                                               {:previous :concept-chant
                                                                :name     "next"
                                                                :sequence :introduce-word
                                                                :handler  :senora-vaca-audio-touch-second-box}
                                                               {:previous :concept-chant
                                                                :name     "next"
                                                                :sequence :introduce-word
                                                                :handler  :senora-vaca-audio-touch-third-box}}}
           :senora-vaca-audio-2                {:path        [:senora-vaca-audio-2]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}
           :pick-wrong                         {:path        [:pick-wrong]
                                                :entity      :action
                                                :children    [:audio-wrong]
                                                :connections #{}}
           :mari-finish                        {:path        [:mari-finish]
                                                :entity      :action
                                                :children    [:mari-finish-0 :mari-finish-1 :mari-finish-2]
                                                :connections #{}}
           :concept-chant                      {:path        [:concept-chant]
                                                :entity      :action
                                                :children    [:empty-small
                                                              :vaca-say-3-times
                                                              :vaca-3-times-var
                                                              :empty-big
                                                              :group-3-times-var
                                                              :empty-small-2
                                                              :vaca-once-more
                                                              :empty-small
                                                              :group-3-times-var]
                                                :connections #{{:previous :concept-intro
                                                                :name     "next"
                                                                :sequence :concept-chant
                                                                :handler  :vaca-goodbye-var}}}
           :start                              {:path        [:start]
                                                :entity      :trigger
                                                :children    []
                                                :connections #{{:previous :root :name "start" :handler :senora-vaca-audio-1}}}
           :senora-vaca-audio-touch-third-box  {:path        [:senora-vaca-audio-touch-third-box]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}
           :box3                               {:path        [:box3]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:previous :root :name "click" :handler :concept-intro}
                                                               {:previous :root :name "click" :handler :pick-wrong}}}
           :senora-vaca-audio-1                {:path        [:senora-vaca-audio-1]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :start
                                                                :name     "next"
                                                                :sequence :intro
                                                                :handler  :senora-vaca-audio-2}
                                                               {:previous :senora-vaca
                                                                :name     "next"
                                                                :sequence :intro
                                                                :handler  :senora-vaca-audio-2}}}
           :box1                               {:path        [:box1]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:previous :root :name "click" :handler :concept-intro}
                                                               {:previous :root :name "click" :handler :pick-wrong}}}
           :box2                               {:path        [:box2]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:previous :root :name "click" :handler :concept-intro}
                                                               {:previous :root :name "click" :handler :pick-wrong}}}})
