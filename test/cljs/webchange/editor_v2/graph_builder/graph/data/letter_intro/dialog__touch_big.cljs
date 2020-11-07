(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--touch-big)

(def data {:touch-big-letter-1       {:data        {:phrase-text "Touch the big letter"}
                                      :path        [:touch-big-letter 1]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :sequence :touch-big-letter
                                                      :handler  :letter-intro-letter-mari}}}
           :touch-big-letter-3       {:data        {:phrase-text "to hear how it sounds."}
                                      :path        [:touch-big-letter 3]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}
           :letter-intro-letter-mari {:data        {:phrase-text "a"}
                                      :path        [:letter-intro-letter-mari]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :touch-big-letter-1
                                                      :name     "next"
                                                      :handler  :touch-big-letter-3}}}})
