(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--two-ways)

(def data {:vaca-voice-2-ways-write-4  {:data        {:phrase-text "a big letter"}
                                        :path        [:vaca-voice-2-ways-write 4]
                                        :entity      :action
                                        :children    []
                                        :connections #{}}
           :letter-intro-letter-copy-1 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :vaca-voice-2-ways-write-0
                                                        :name     "next"
                                                        :handler  :vaca-voice-2-ways-write-2}}}
           :vaca-voice-2-ways-write-0  {:data        {:phrase-text "Good! There are 2 ways to write this sound. You can write it with "}
                                        :path        [:vaca-voice-2-ways-write 0]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :root
                                                        :name     "next"
                                                        :sequence :vaca-voice-2-ways-write
                                                        :handler  :letter-intro-letter-copy-1}}}
           :letter-intro-letter-copy-0 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :vaca-voice-2-ways-write-2
                                                        :name     "next"
                                                        :handler  :vaca-voice-2-ways-write-4}}}
           :vaca-voice-2-ways-write-2  {:data        {:phrase-text "a small letter or"}
                                        :path        [:vaca-voice-2-ways-write 2]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-1
                                                        :name     "next"
                                                        :sequence :vaca-voice-2-ways-write
                                                        :handler  :letter-intro-letter-copy-0}}}})
