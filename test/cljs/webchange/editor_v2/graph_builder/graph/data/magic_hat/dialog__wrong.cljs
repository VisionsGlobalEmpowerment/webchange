(ns webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--wrong)

(def data {:mari-voice-wrong-1 {:data        {:phrase-text "That is"}
                                :path        [:mari-voice-wrong-1]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-word-copy-2
                                                :name     "next"
                                                :sequence :mari-says-wrong-answer
                                                :handler  :mari-word-copy-1}}}
           :mari-voice-wrong-2 {:data        {:phrase-text "starts with the sound"}
                                :path        [:mari-voice-wrong-2]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-word-copy-0
                                                :name     "next"
                                                :sequence :mari-says-wrong-answer
                                                :handler  :mari-sound-copy-1}}}
           :mari-word-copy-0   {:data        {:phrase-text "Ardilla"}
                                :path        [:mari-word]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-word-copy-1
                                                :name     "next"
                                                :handler  :mari-voice-wrong-2}}}
           :mari-sound-copy-0  {:data        {:phrase-text "A"}
                                :path        [:mari-sound]
                                :entity      :action
                                :children    []
                                :connections #{}}
           :mari-word-copy-1   {:data        {:phrase-text "Ardilla"}
                                :path        [:mari-word]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-voice-wrong-1
                                                :name     "next"
                                                :handler  :mari-word-copy-0}}}
           :mari-word-copy-2   {:data        {:phrase-text "Ardilla"}
                                :path        [:mari-word]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :root
                                                :name     "next"
                                                :handler  :mari-voice-wrong-1}}}
           :mari-sound-copy-1  {:data        {:phrase-text "A"}
                                :path        [:mari-sound]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-voice-wrong-2
                                                :name     "next"
                                                :handler  :mari-voice-wrong-3}}}
           :mari-voice-wrong-3 {:data        {:phrase-text "Touch the picture that makes this sound"}
                                :path        [:mari-voice-wrong-3]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :mari-sound-copy-1
                                                :name     "next"
                                                :sequence :mari-says-wrong-answer
                                                :handler  :mari-sound-copy-0}}}})
