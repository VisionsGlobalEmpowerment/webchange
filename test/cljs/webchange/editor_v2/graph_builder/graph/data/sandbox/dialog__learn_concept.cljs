(ns webchange.editor-v2.graph-builder.graph.data.sandbox.dialog--learn-concept)

(def data {:sandbox-short-letter-action-copy-0 {:data        {:phrase-text nil}
                                                :path        [:sandbox-short-letter-action]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :mari-letter-2
                                                                :name     "next"
                                                                :handler  :mari-letter-3}}}
           :sandbox-long-letter-action-copy-1  {:data        {:phrase-text nil}
                                                :path        [:sandbox-long-letter-action]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :mari-letter-3
                                                                :name     "next"
                                                                :handler  :mari-letter-4}}}
           :mari-letter-1                      {:data        {:phrase-text "This is the letter"}
                                                :path        [:mari-letter-1]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :root
                                                                :name     "next"
                                                                :sequence :letter-intro
                                                                :handler  :sandbox-short-letter-action-copy-1}}}
           :mari-letter-2                      {:data        {:phrase-text "Did you know that the letter"}
                                                :path        [:mari-letter-2]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :sandbox-short-letter-action-copy-1
                                                                :name     "next"
                                                                :sequence :letter-intro
                                                                :handler  :sandbox-short-letter-action-copy-0}}}
           :mari-letter-3                      {:data        {:phrase-text "makes the sound"}
                                                :path        [:mari-letter-3]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :sandbox-short-letter-action-copy-0
                                                                :name     "next"
                                                                :sequence :letter-intro
                                                                :handler  :sandbox-long-letter-action-copy-1}}}
           :sandbox-short-letter-action-copy-1 {:data        {:phrase-text nil}
                                                :path        [:sandbox-short-letter-action]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :mari-letter-1
                                                                :name     "next"
                                                                :handler  :mari-letter-2}}}
           :mari-letter-4                      {:data        {:phrase-text "Click on the pictures to hear more words that start with the sound"}
                                                :path        [:mari-letter-4]
                                                :entity      :action
                                                :children    []
                                                :connections #{{:previous :sandbox-long-letter-action-copy-1
                                                                :name     "next"
                                                                :sequence :letter-intro
                                                                :handler  :sandbox-long-letter-action-copy-0}}}
           :sandbox-long-letter-action-copy-0  {:data        {:phrase-text nil}
                                                :path        [:sandbox-long-letter-action]
                                                :entity      :action
                                                :children    []
                                                :connections #{}}})
