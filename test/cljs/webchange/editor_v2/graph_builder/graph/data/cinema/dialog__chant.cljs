(ns webchange.editor-v2.graph-builder.graph.data.cinema.dialog--chant)

(def data {:vaca-voice-chanting-4 {:data        {:phrase-text "Let’s sing it"}
                                   :path        [:vaca-voice-chanting-4]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :vaca-chanting-word
                                                   :name     "next"
                                                   :sequence :chant-current-letter
                                                   :handler  :vaca-chanting-song}}}
           :vaca-chanting-song    {:data        {:phrase-text "Ardilla! Ardilla! a-a-A!"}
                                   :path        [:vaca-chanting-song]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :vaca-chanting-word    {:data        {:phrase-text "Ardilla"}
                                   :path        [:vaca-chanting-word]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "next"
                                                   :handler  :vaca-voice-chanting-4}}}})
