(ns webchange.editor-v2.graph-builder.graph.data.cycling-letters.dialog--correct)

(def data {:parent               {:connections #{{:previous :root
                                                  :name     "next"
                                                  :handler  :mari-audio-correct-2}
                                                 {:previous :root
                                                  :name     "next"
                                                  :handler  :mari-audio-correct-1}
                                                 {:previous :root
                                                  :name     "next"
                                                  :handler  :mari-audio-correct-5}
                                                 {:previous :root
                                                  :name     "next"
                                                  :handler  :mari-audio-correct-4}
                                                 {:previous :root
                                                  :name     "next"
                                                  :handler  :mari-audio-correct-3}}
                                  :data        {:phrase-text nil}}
           :mari-audio-correct-2 {:data        {:phrase-text "Good job!"}
                                  :path        [:mari-audio-correct-2]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :mari-audio-correct-1 {:data        {:phrase-text "That’s correct!"}
                                  :path        [:mari-audio-correct-1]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :mari-audio-correct-5 {:data        {:phrase-text "Wow, excelent!"}
                                  :path        [:mari-audio-correct-5]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :mari-audio-correct-4 {:data        {:phrase-text "You got it!"}
                                  :path        [:mari-audio-correct-4]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :mari-audio-correct-3 {:data        {:phrase-text "Well done!"}
                                  :path        [:mari-audio-correct-3]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}})
