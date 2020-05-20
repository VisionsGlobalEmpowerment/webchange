(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--stage-small)

(def data {:letter-intro-word-copy-0   {:data        {:phrase-text "ardilla"}
                                        :path        [:letter-intro-word]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-14
                                                        :name     "next"
                                                        :handler  :letter-intro-word-repeat}}}
           :stage-small-11             {:data        {:phrase-text "is the first sound in the word"}
                                        :path        [:stage-small 11]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-1
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :letter-intro-word-copy-1}}}
           :letter-intro-sound-copy-0  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-word-copy-1
                                                        :name     "next"
                                                        :handler  :stage-small-14}}}
           :letter-intro-letter-copy-1 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-1
                                                        :name     "next"
                                                        :handler  :stage-small-3}}}
           :letter-intro-sound-copy-2  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-7
                                                        :name     "next"
                                                        :handler  :letter-intro-sound-copy-1}}}
           :letter-intro-sound-copy-1  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-2
                                                        :name     "next"
                                                        :handler  :stage-small-11}}}
           :stage-small-1              {:data        {:phrase-text "This letter"}
                                        :path        [:stage-small 1]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :root
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :letter-intro-letter-copy-1}}}
           :letter-intro-word-repeat   {:data        {:phrase-text "Aaaaaardilla. Aaaaaardilla"}
                                        :path        [:letter-intro-word-repeat]
                                        :entity      :action
                                        :children    []
                                        :connections #{}}
           :stage-small-5              {:data        {:phrase-text "The letter"}
                                        :path        [:stage-small 5]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-3
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :letter-intro-letter-copy-0}}}
           :stage-small-14             {:data        {:phrase-text "is for"}
                                        :path        [:stage-small 14]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-0
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :letter-intro-word-copy-0}}}
           :stage-small-3              {:data        {:phrase-text "is small"}
                                        :path        [:stage-small 3]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-1
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :stage-small-5}}}
           :letter-intro-word-copy-1   {:data        {:phrase-text "ardilla"}
                                        :path        [:letter-intro-word]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-11
                                                        :name     "next"
                                                        :handler  :letter-intro-sound-copy-0}}}
           :stage-small-7              {:data        {:phrase-text "makes the sound"}
                                        :path        [:stage-small 7]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-0
                                                        :name     "next"
                                                        :sequence :stage-small
                                                        :handler  :letter-intro-sound-copy-2}}}
           :letter-intro-letter-copy-0 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :stage-small-5
                                                        :name     "next"
                                                        :handler  :stage-small-7}}}})
