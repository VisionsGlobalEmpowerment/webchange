(ns webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--poem)

(def data {:poem-story-6  {:data        {:phrase-text nil}
                           :path        [:poem-story 6]
                           :entity      :action
                           :children    []
                           :connections #{{:previous :poem-story-4
                                           :name     "next"
                                           :sequence :poem-story
                                           :handler  :poem-story-9}}}
           :poem-story-11 {:data        {:phrase-text nil}
                           :path        [:poem-story 11]
                           :entity      :action
                           :children    []
                           :connections #{}}
           :poem-story-9  {:data        {:phrase-text nil}
                           :path        [:poem-story 9]
                           :entity      :action
                           :children    []
                           :connections #{{:previous :poem-story-6
                                           :name     "next"
                                           :sequence :poem-story
                                           :handler  :poem-story-11}}}
           :poem-story-1  {:data        {:phrase-text nil}
                           :path        [:poem-story 1]
                           :entity      :action
                           :children    []
                           :connections #{{:previous :root
                                           :name     "next"
                                           :sequence :poem-story
                                           :handler  :poem-story-4}}}
           :poem-story-4  {:data        {:phrase-text nil}
                           :path        [:poem-story 4]
                           :entity      :action
                           :children    []
                           :connections #{{:previous :poem-story-1
                                           :name     "next"
                                           :sequence :poem-story
                                           :handler  :poem-story-6}}}})
