(ns webchange.editor-v2.graph-builder.graph.data.home.dialog--chant-concept)

(def data {:vaca-once-more                     {:data        {:phrase-text "Una vez mas!"}
                                                :path        [:vaca-once-more]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :home-group-3-times-action-copy-1-0
                                                                :name     "next"
                                                                :sequence :concept-chant
                                                                :handler  :home-group-3-times-action-copy-0-0}}}
           :home-group-3-times-action-copy-1-0 {:data        {:phrase-text "Ardilla, ardilla, ARDILLA!!"}
                                                :path        [:home-group-3-times-action 0]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :home-vaca-3-times-action
                                                                :name     "next"
                                                                :handler  :vaca-once-more}}}
           :home-vaca-3-times-action           {:data        {:phrase-text "Ardilla, ardilla, ARDILLA!!"}
                                                :path        [:home-vaca-3-times-action]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :vaca-say-3-times
                                                                :name     "next"
                                                                :handler  :home-group-3-times-action-copy-1-0}}}
           :vaca-say-3-times                   {:data        {:phrase-text "Bueno. Digalo tres veces"}
                                                :path        [:vaca-say-3-times]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :root
                                                                :name     "next"
                                                                :sequence :concept-chant
                                                                :handler  :home-vaca-3-times-action}}}
           :home-group-3-times-action-copy-0-0 {:data        {:phrase-text "Ardilla, ardilla, ARDILLA!!"}
                                                :path        [:home-group-3-times-action 0]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}})
