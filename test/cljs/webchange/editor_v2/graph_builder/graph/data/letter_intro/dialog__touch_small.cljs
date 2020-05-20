(ns webchange.editor-v2.graph-builder.graph.data.letter-intro.dialog--touch-small)

(def data {:letter-intro-letter-mari {:data        {:phrase-text "a"}
                                      :path        [:letter-intro-letter-mari]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :touch-small-letter-1
                                                      :name     "next"
                                                      :handler  :touch-small-letter-3}}}
           :touch-small-letter-1     {:data        {:phrase-text "Touch the small letter"}
                                      :path        [:touch-small-letter 1]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :sequence :touch-small-letter
                                                      :handler  :letter-intro-letter-mari}}}
           :touch-small-letter-3     {:data        {:phrase-text "to hear how it sounds."}
                                      :path        [:touch-small-letter 3]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}})
