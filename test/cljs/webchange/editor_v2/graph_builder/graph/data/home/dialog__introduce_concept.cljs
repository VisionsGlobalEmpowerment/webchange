(ns webchange.editor-v2.graph-builder.graph.data.home.dialog--introduce-concept)

(def data {:vaca-can-you-say          {:data        {:phrase-text "Puedes deciry"}
                                       :path        [:vaca-can-you-say]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :home-vaca-this-is-action
                                                       :name     "next"
                                                       :sequence :concept-intro
                                                       :handler  :home-vaca-question-action}}}
           :home-vaca-word-action     {:data        {:phrase-text "Ardilla!"}
                                       :path        [:home-vaca-word-action]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :home-vaca-question-action
                                                       :name     "next"
                                                       :handler  :home-group-word-action-0}
                                                      {:previous :home-vaca-question-action
                                                       :name     "next"
                                                       :handler  :home-group-word-action-1}}}
           :home-group-word-action-0  {:data        {:phrase-text "Ardilla!"}
                                       :path        [:home-group-word-action 0]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :home-group-word-action-1  {:data        {:phrase-text "Ardilla!"}
                                       :path        [:home-group-word-action 1]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :home-vaca-this-is-action  {:data        {:phrase-text "Este es un ardilla"}
                                       :path        [:home-vaca-this-is-action]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :root
                                                       :name     "next"
                                                       :handler  :vaca-can-you-say}}}
           :home-vaca-question-action {:data        {:phrase-text "Ardilla"}
                                       :path        [:home-vaca-question-action]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :vaca-can-you-say
                                                       :name     "next"
                                                       :handler  :home-vaca-word-action}}}})
