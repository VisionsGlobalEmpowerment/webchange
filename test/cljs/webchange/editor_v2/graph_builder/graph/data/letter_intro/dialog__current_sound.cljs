(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--current-sound)

(def data {:letter-intro-word-copy-0 {:data        {:phrase-text "ardilla"}
                                      :path        [:letter-intro-word]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :vaca-voice-listen-2
                                                      :name     "next"
                                                      :handler  :letter-intro-word-repeat}}}
           :letter-intro-song-copy-0 {:data        {:phrase-text "Ardilla, ardilla, a, a, A!"}
                                      :path        [:letter-intro-song]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :letter-intro-song-copy-1 {:data        {:phrase-text "Ardilla, ardilla, a, a, A!"}
                                      :path        [:letter-intro-song]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :letter-intro-song-copy-2
                                                      :name     "next"
                                                      :handler  :letter-intro-song-copy-0}}}
           :letter-intro-song-copy-2 {:data        {:phrase-text "Ardilla, ardilla, a, a, A!"}
                                      :path        [:letter-intro-song]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :vaca-voice-say-with-me
                                                      :name     "next"
                                                      :handler  :letter-intro-song-copy-1}}}
           :vaca-voice-listen-5      {:data        {:phrase-text "Sing it with me like this"}
                                      :path        [:vaca-voice-listen 5]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :letter-intro-word-repeat
                                                      :name     "next"
                                                      :sequence :vaca-voice-listen
                                                      :handler  :letter-intro-song-copy-3}}}
           :letter-intro-word-repeat {:data        {:phrase-text "Aaaaaardilla. Aaaaaardilla"}
                                      :path        [:letter-intro-word-repeat]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :letter-intro-word-copy-0
                                                      :name     "next"
                                                      :handler  :vaca-voice-listen-5}}}
           :vaca-voice-say-with-me   {:data        {:phrase-text "Say it with me! Say it with me 3 times."}
                                      :path        [:vaca-voice-say-with-me]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :letter-intro-song-copy-3
                                                      :name     "next"
                                                      :sequence :current-sound
                                                      :handler  :letter-intro-song-copy-2}}}
           :vaca-voice-listen-0      {:data        {:phrase-text "Excellent! We have a"}
                                      :path        [:vaca-voice-listen 0]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :sequence :vaca-voice-listen
                                                      :handler  :letter-intro-word-copy-1}}}
           :letter-intro-word-copy-1 {:data        {:phrase-text "ardilla"}
                                      :path        [:letter-intro-word]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :vaca-voice-listen-0
                                                      :name     "next"
                                                      :handler  :vaca-voice-listen-2}}}
           :letter-intro-song-copy-3 {:data        {:phrase-text "Ardilla, ardilla, a, a, A!"}
                                      :path        [:letter-intro-song]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :vaca-voice-listen-5
                                                      :name     "next"
                                                      :handler  :vaca-voice-say-with-me}}}
           :vaca-voice-listen-2      {:data        {:phrase-text "Listen to the first sound in the word"}
                                      :path        [:vaca-voice-listen 2]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :letter-intro-word-copy-1
                                                      :name     "next"
                                                      :sequence :vaca-voice-listen
                                                      :handler  :letter-intro-word-copy-0}}}})
