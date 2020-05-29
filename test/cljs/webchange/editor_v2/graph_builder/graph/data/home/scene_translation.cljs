(ns webchange.editor-v2.graph-builder.graph.data.home.scene-translation)

(def data {:senora-vaca                        {:path        [:senora-vaca]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:handler :senora-vaca-audio-1
                                                                :name    "next"}}}
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
                                                :connections #{{:handler :concept-chant
                                                                :name    "next"}}}
           :vaca-goodbye-var                   {:path        [:vaca-goodbye-var]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:handler :mari-finish
                                                                :name    "next"}
                                                               {:handler :senora-vaca-audio-touch-second-box
                                                                :name    "next"}
                                                               {:handler :senora-vaca-audio-touch-third-box
                                                                :name    "next"}}}
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
                                                :connections #{{:handler :vaca-goodbye-var
                                                                :name    "next"}}}
           :start                              {:path        [:start]
                                                :entity      :trigger
                                                :children    []
                                                :connections #{{:handler :senora-vaca-audio-1
                                                                :name    "next"}}}
           :senora-vaca-audio-touch-third-box  {:path        [:senora-vaca-audio-touch-third-box]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}
           :box3                               {:path        [:box3]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:handler :pick-wrong
                                                                :name    "next"}
                                                               {:handler :concept-intro
                                                                :name    "next"}}}
           :senora-vaca-audio-1                {:path        [:senora-vaca-audio-1]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:handler :senora-vaca-audio-2
                                                                :name    "next"}}}
           :box1                               {:path        [:box1]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:handler :pick-wrong
                                                                :name    "next"}
                                                               {:handler :concept-intro
                                                                :name    "next"}}}
           :box2                               {:path        [:box2]
                                                :entity      :object
                                                :children    []
                                                :connections #{{:handler :pick-wrong
                                                                :name    "next"}
                                                               {:handler :concept-intro
                                                                :name    "next"}}}}
  )
