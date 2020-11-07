(ns webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--lets-watch)

(def data {:vaca-voice-lets-watch-0 {:data        {:phrase-text "Excellent! Now, let’s watch a video about the letter"}
                                     :path        [:vaca-voice-lets-watch 0]
                                     :entity      :action
                                     :children    []
                                     :connections #{{:previous :root
                                                     :name     "next"
                                                     :sequence :vaca-voice-lets-watch
                                                     :handler  :letter-intro-letter}}}
           :letter-intro-letter     {:data        {:phrase-text "A"}
                                     :path        [:letter-intro-letter]
                                     :entity      :action
                                     :children    []
                                     :connections #{}}})
