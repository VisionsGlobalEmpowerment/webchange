(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--intro)

(def data {:letter-intro-letter-copy-1 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :vaca-voice-welcome-0
                                                        :name     "next"
                                                        :handler  :vaca-voice-give-word-0}}}
           :letter-intro-letter-copy-0 {:data        {:phrase-text "A"}
                                        :path        [:letter-intro-letter]
                                        :entity      :action
                                        :children    []
                                        :connections #{}}
           :vaca-voice-welcome-0       {:data        {:phrase-text "Hello little genius! Let’s learn some new letters! Let’s start with the letter"}
                                        :path        [:vaca-voice-welcome 0]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :root
                                                        :name     "next"
                                                        :sequence :vaca-voice-welcome
                                                        :handler  :letter-intro-letter-copy-1}}}
           :vaca-voice-give-word-0     {:data        {:phrase-text "Mari, can you give me a word that starts with the letter"}
                                        :path        [:vaca-voice-give-word 0]
                                        :entity      :action
                                        :children    []
                                        :connections #{{:previous :letter-intro-letter-copy-1
                                                        :name     "next"
                                                        :sequence :vaca-voice-give-word
                                                        :handler  :letter-intro-letter-copy-0}}}})
