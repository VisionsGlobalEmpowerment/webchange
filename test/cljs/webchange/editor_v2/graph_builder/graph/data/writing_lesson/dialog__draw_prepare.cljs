(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--draw-prepare)

(def data {:vaca-voice-help-mari-0 {:data        {:phrase-text "Mari, can you help me paint the letter"}
                                    :path        [:vaca-voice-help-mari 0]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :root
                                                    :name     "next"
                                                    :sequence :vaca-voice-help-mari
                                                    :handler  :letter-intro-letter}}}
           :mari-voice-sure        {:data        {:phrase-text "Sure, of course!"}
                                    :path        [:mari-voice-sure]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}
           :letter-intro-letter    {:data        {:phrase-text "A"}
                                    :path        [:letter-intro-letter]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :vaca-voice-help-mari-0
                                                    :name     "next"
                                                    :handler  :mari-voice-sure}}}})
