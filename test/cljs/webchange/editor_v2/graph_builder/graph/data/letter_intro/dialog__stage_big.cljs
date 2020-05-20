(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--stage-big)

(def data {:stage-big-4                {:data        {:phrase-text "The letter"}
                                        :path        [:stage-big 4]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-1
                                                        :name     "next"
                                                        :sequence :stage-big
                                                        :handler  :letter-intro-letter-copy-1}}}
           :letter-intro-sound-copy-0  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-big-8
                                                        :name     "next"
                                                        :handler  :letter-intro-word-repeat}}}
           :letter-intro-letter-copy-1 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-big-4
                                                        :name     "next"
                                                        :handler  :stage-big-6}}}
           :stage-big-6                {:data        {:phrase-text "is big. The letter"}
                                        :path        [:stage-big 6]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-1
                                                        :name     "next"
                                                        :sequence :stage-big
                                                        :handler  :letter-intro-letter-copy-0}}}
           :letter-intro-sound-copy-1  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-big-2
                                                        :name     "next"
                                                        :handler  :stage-big-4}}}
           :stage-big-8                {:data        {:phrase-text "also makes the sound"}
                                        :path        [:stage-big 8]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-0
                                                        :name     "next"
                                                        :sequence :stage-big
                                                        :handler  :letter-intro-sound-copy-0}}}
           :letter-intro-word-repeat   {:data        {:phrase-text "Aaaaaardilla. Aaaaaardilla"}
                                        :path        [:letter-intro-word-repeat]
                                        :entity      :action
                                        :children    []
                                        :connections #{}}
           :stage-big-2                {:data        {:phrase-text "This is another way to write the sound"}
                                        :path        [:stage-big 2]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :root
                                                        :name     "next"
                                                        :sequence :stage-big
                                                        :handler  :letter-intro-sound-copy-1}}}
           :letter-intro-letter-copy-0 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-big-6
                                                        :name     "next"
                                                        :handler  :stage-big-8}}}})
