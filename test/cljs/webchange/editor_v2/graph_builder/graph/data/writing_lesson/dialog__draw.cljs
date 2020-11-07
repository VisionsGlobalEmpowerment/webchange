(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--draw)

(def data {:letter-intro-letter  {:data        {:phrase-text "A"}
                                  :path        [:letter-intro-letter]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :letter-drawing-0-0-0
                                                  :name     "next"
                                                  :handler  :letter-drawing-1}}}
           :letter-drawing-1     {:data        {:phrase-text "Wonderful! Well done Mari! Can you show us one more time? Remember to watch how Mari follows the arrows to paint the letter."}
                                  :path        [:letter-drawing 1]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :letter-drawing-0-0
                                                  :name     "next"
                                                  :sequence :letter-drawing
                                                  :handler  :letter-drawing-1}
                                                 {:previous :root
                                                  :name     "next"
                                                  :sequence :letter-drawing
                                                  :handler  :letter-drawing-1}}}
           :letter-drawing-0-0-0 {:data        {:phrase-text "Watch how Mari follows the arrows to paint the letter"}
                                  :path        [:letter-drawing 0 0 0]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "next"
                                                  :sequence :letter-drawing-0-0
                                                  :handler  :letter-intro-letter}}}})
