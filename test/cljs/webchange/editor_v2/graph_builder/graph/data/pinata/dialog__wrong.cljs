(ns webchange.editor-v2.graph-builder.graph.data.pinata.dialog--wrong)

(def data {:parent                 {:connections #{{:previous :root
                                                    :name     "next"
                                                    :handler  :mari-audio-try-again-1}
                                                   {:previous :root
                                                    :name     "next"
                                                    :handler  :mari-audio-try-again-2}}
                                    :data        {:phrase-text nil}}
           :mari-audio-try-again-1 {:data        {:phrase-text "Try again!"}
                                    :path        [:mari-audio-try-again-1]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}
           :mari-audio-try-again-2 {:data        {:phrase-text "Hmmmm. Try again."}
                                    :path        [:mari-audio-try-again-2]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}})
