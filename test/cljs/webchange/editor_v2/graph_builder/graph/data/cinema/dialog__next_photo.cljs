(ns webchange.editor-v2.graph-builder.graph.data.cinema.dialog--next-photo)

(def data {:vaca-voice-next-picture-1 {:data        {:phrase-text "What’s the next picture?"}
                                       :path        [:vaca-voice-next-picture-1]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :vaca-voice-next-picture-2 {:data        {:phrase-text "And now the next picture!"}
                                       :path        [:vaca-voice-next-picture-2]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :vaca-voice-next-picture-3 {:data        {:phrase-text "What’s next?!"}
                                       :path        [:vaca-voice-next-picture-3]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :vaca-voice-next-picture-4 {:data        {:phrase-text "Keep going!"}
                                       :path        [:vaca-voice-next-picture-4]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :parent                    {:connections #{{:previous :root
                                                       :name     "next"
                                                       :handler  :vaca-voice-next-picture-1}
                                                      {:previous :root
                                                       :name     "next"
                                                       :handler  :vaca-voice-next-picture-2}
                                                      {:previous :root
                                                       :name     "next"
                                                       :handler  :vaca-voice-next-picture-3}
                                                      {:previous :root
                                                       :name     "next"
                                                       :handler  :vaca-voice-next-picture-4}}
                                       :data        {:phrase-text nil}}})
