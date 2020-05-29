(ns webchange.editor-v2.graph-builder.graph.data.pinata.dialog--chant)

(def data {:mari-voice-act-1   {:data        {:phrase-text "This is the letter"}
                                :path        [:mari-voice-act-1]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :root
                                                :name     "next"
                                                :sequence :mari-says-task
                                                :handler  :mari-letter-copy-1}}}
           :mari-voice-act-2   {:data        {:phrase-text "Place the picture that starts with the letter"}
                                :path        [:mari-voice-act-2]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-letter-copy-1
                                                :name     "next"
                                                :sequence :mari-says-task
                                                :handler  :mari-letter-copy-0}}}
           :mari-letter-copy-0 {:data        {:phrase-text "A"}
                                :path        [:mari-letter]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-voice-act-2
                                                :name     "next"
                                                :handler  :mari-voice-act-3}}}
           :mari-voice-act-3   {:data        {:phrase-text "on the title."}
                                :path        [:mari-voice-act-3]
                                :entity      :action
                                :children    []
                                :connections #{}}
           :mari-letter-copy-1 {:data        {:phrase-text "A"}
                                :path        [:mari-letter]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-voice-act-1
                                                :name     "next"
                                                :handler  :mari-voice-act-2}}}})
