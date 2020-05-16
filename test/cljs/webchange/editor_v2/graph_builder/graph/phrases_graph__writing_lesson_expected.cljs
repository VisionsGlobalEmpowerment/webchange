(ns webchange.editor-v2.graph-builder.graph.phrases-graph--writing-lesson-expected)

(def data {:start                  {:path        [:start]
                                    :entity      :trigger
                                    :children    []
                                    :connections #{{:previous :root
                                                    :name     "start"
                                                    :handler  :welcome-voice}}}
           :welcome-voice          {:path        [:welcome-voice]
                                    :entity      :action
                                    :children    [:vaca-voice-welcome
                                                  :mari-voice-welcome]
                                    :connections #{{:previous :start
                                                    :name     "next"
                                                    :sequence :welcome-voice
                                                    :handler  :introduce-picture}}}
           :introduce-picture      {:path        [:introduce-picture]
                                    :entity      :action
                                    :children    [:vaca-asks-sound
                                                  :show-current-word-picture
                                                  :vaca-letter-pronouncing]
                                    :connections #{{:previous :welcome-voice
                                                    :name     "next"
                                                    :sequence :introduce-picture
                                                    :handler  :introduce-letter}}}
           :introduce-letter       {:path        [:introduce-letter]
                                    :entity      :action
                                    :children    [:vaca-voice-sound-look
                                                  :init-letter
                                                  :show-letter
                                                  :pronounce-letter]
                                    :connections #{{:previous :introduce-picture
                                                    :name     "next"
                                                    :sequence :introduce-letter
                                                    :handler  :letter-drawing-prepare}}}

           :letter-drawing-prepare {:path        [:letter-drawing-prepare]
                                    :entity      :action
                                    :children    [:vaca-voice-help-mari
                                                  :mari-voice-sure]
                                    :connections #{{:previous :introduce-letter
                                                    :name     "next"
                                                    :sequence :letter-drawing-prepare
                                                    :handler  :letter-drawing}}}
           :letter-drawing         {:path        [:letter-drawing]
                                    :entity      :action
                                    :children    [:letter-drawing-0
                                                  :letter-drawing-1
                                                  :letter-drawing-2
                                                  :letter-drawing-3
                                                  :letter-drawing-4
                                                  :letter-drawing-5]
                                    :connections #{{:previous :letter-drawing-prepare
                                                    :name     "next"
                                                    :sequence :letter-drawing
                                                    :handler  :letter-drawing-1}
                                                   {:previous :letter-drawing-prepare
                                                    :name     "next"
                                                    :sequence :letter-drawing
                                                    :handler  :invite-user}}}
           :invite-user            {:path        [:invite-user]
                                    :entity      :action
                                    :children    [:vaca-voice-your-turn
                                                  :finish-activity
                                                  :mari-click-to-practice]
                                    :connections #{}}})
