(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--intro-picture)

(def data {:letter-intro-letter       {:data        {:phrase-text "A"}
                                       :path        [:letter-intro-letter]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :vaca-asks-sound-0
                                                       :name     "next"
                                                       :handler  :letter-intro-word-copy-2}}}
           :letter-intro-word-copy-0  {:data        {:phrase-text "ardilla"}
                                       :path        [:letter-intro-word]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :letter-intro-sound-copy-0 {:data        {:phrase-text "Aaaaa..."}
                                       :path        [:letter-intro-sound]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :letter-intro-sound-copy-1
                                                       :name     "next"
                                                       :handler  :letter-intro-word-copy-1}}}
           :letter-intro-sound-copy-2 {:data        {:phrase-text "Aaaaa..."}
                                       :path        [:letter-intro-sound]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :vaca-letter-pronouncing-1
                                                       :name     "next"
                                                       :handler  :letter-intro-sound-copy-1}}}
           :letter-intro-sound-copy-1 {:data        {:phrase-text "Aaaaa..."}
                                       :path        [:letter-intro-sound]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :letter-intro-sound-copy-2
                                                       :name     "next"
                                                       :handler  :letter-intro-sound-copy-0}}}
           :vaca-asks-sound-0         {:data        {:phrase-text "Now, pay attention friends. Did you know that each picture begins with a sound"}
                                       :path        [:vaca-asks-sound 0]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :root
                                                       :name     "next"
                                                       :sequence :vaca-asks-sound
                                                       :handler  :letter-intro-letter}}}
           :vaca-letter-pronouncing-1 {:data        {:phrase-text "starts with sound"}
                                       :path        [:vaca-letter-pronouncing 1]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :letter-intro-word-copy-2
                                                       :name     "next"
                                                       :sequence :vaca-letter-pronouncing
                                                       :handler  :letter-intro-sound-copy-2}}}
           :letter-intro-word-copy-2  {:data        {:phrase-text "ardilla"}
                                       :path        [:letter-intro-word]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :letter-intro-letter
                                                       :name     "next"
                                                       :handler  :vaca-letter-pronouncing-1}}}
           :letter-intro-word-copy-1  {:data        {:phrase-text "ardilla"}
                                       :path        [:letter-intro-word]
                                       :entity      :action
                                       :children    []
                                       :connections #{{:previous :letter-intro-sound-copy-0
                                                       :name     "next"
                                                       :handler  :letter-intro-word-copy-0}}}})
