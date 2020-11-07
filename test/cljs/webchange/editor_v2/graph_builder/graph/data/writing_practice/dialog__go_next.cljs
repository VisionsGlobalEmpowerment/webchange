(ns webchange.editor-v2.graph-builder.graph.data.writing-practice.dialog--go-next)

(def data {:mari-voice-go-next-practice-0 {:data        {:phrase-text "Good job tracing the letter a!"}
                                           :path        [:mari-voice-go-next-practice 0]
                                           :entity      :action
                                           :children    []
                                           :connections #{{:previous :root
                                                           :name     "next"
                                                           :sequence :mari-voice-go-next-practice
                                                           :handler  :mari-voice-go-next-practice-1}}}
           :mari-voice-go-next-practice-1 {:data        {:phrase-text "Click on the arrow to do a little more writing work."}
                                           :path        [:mari-voice-go-next-practice 1]
                                           :entity      :action
                                           :children    []
                                           :connections #{}}})
