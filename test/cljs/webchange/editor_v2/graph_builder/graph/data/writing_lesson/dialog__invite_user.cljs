(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--invite-user)

(def data {:vaca-voice-your-turn     {:data        {:phrase-text "Now it’s your turn to practice tracing letters.  Be sure to trace each letter carefully, the same way that Mari did! Have fun!"}
                                      :path        [:vaca-voice-your-turn]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :sequence :invite-user
                                                      :handler  :mari-click-to-practice-1}}}
           :mari-click-to-practice-1 {:data        {:phrase-text "Click here to start your letter practice!"}
                                      :path        [:mari-click-to-practice 1]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}})
