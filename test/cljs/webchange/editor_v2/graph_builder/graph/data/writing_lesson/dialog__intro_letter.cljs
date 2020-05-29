(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--intro-letter)

(def data {:pronounce-letter-3         {:data        {:phrase-text "The letter"}
                                        :path        [:pronounce-letter 3]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-1
                                                        :name     "next"
                                                        :sequence :pronounce-letter
                                                        :handler  :letter-intro-letter-copy-0}}}
           :letter-intro-sound-copy-0  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{}}
           :letter-intro-letter-copy-1 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :pronounce-letter-0
                                                        :name     "next"
                                                        :handler  :pronounce-letter-3}}}
           :vaca-voice-sound-look      {:data        {:phrase-text "This is what the sound looks like."}
                                        :path        [:vaca-voice-sound-look]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :root
                                                        :name     "next"
                                                        :sequence :introduce-letter
                                                        :handler  :pronounce-letter-0}}}
           :letter-intro-sound-copy-2  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :pronounce-letter-8
                                                        :name     "next"
                                                        :handler  :letter-intro-sound-copy-1}}}
           :letter-intro-sound-copy-1  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-2
                                                        :name     "next"
                                                        :handler  :letter-intro-sound-copy-0}}}
           :letter-intro-sound-copy-3  {:data        {:phrase-text "Aaaaa..."}
                                        :path        [:letter-intro-sound]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :pronounce-letter-5
                                                        :name     "next"
                                                        :handler  :pronounce-letter-8}}}
           :pronounce-letter-0         {:data        {:phrase-text "This is the letter"}
                                        :path        [:pronounce-letter 0]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :vaca-voice-sound-look
                                                        :name     "next"
                                                        :sequence :pronounce-letter
                                                        :handler  :letter-intro-letter-copy-1}}}
           :pronounce-letter-5         {:data        {:phrase-text "makes the sound"}
                                        :path        [:pronounce-letter 5]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-0
                                                        :name     "next"
                                                        :sequence :pronounce-letter
                                                        :handler  :letter-intro-sound-copy-3}}}
           :letter-intro-letter-copy-0 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :pronounce-letter-3
                                                        :name     "next"
                                                        :handler  :pronounce-letter-5}}}
           :pronounce-letter-8         {:data        {:phrase-text "Can you say it with me 3 times?"}
                                        :path        [:pronounce-letter 8]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-sound-copy-3
                                                        :name     "next"
                                                        :sequence :pronounce-letter
                                                        :handler  :letter-intro-sound-copy-2}}}})
