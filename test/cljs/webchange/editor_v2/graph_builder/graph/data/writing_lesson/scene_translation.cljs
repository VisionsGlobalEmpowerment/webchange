(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.scene-translation)

(def data {:start                  {:path        [:start]
                                    :entity      :trigger
                                    :children    []
                                    :connections #{{:handler :welcome-voice
                                                    :name    "next"}}}
           :welcome-voice          {:path        [:welcome-voice]
                                    :entity      :action
                                    :children    [:vaca-voice-welcome :mari-voice-welcome]
                                    :connections #{{:handler :introduce-picture
                                                    :name    "next"}}}
           :introduce-picture      {:path        [:introduce-picture]
                                    :entity      :action
                                    :children    [:vaca-asks-sound :show-current-word-picture :vaca-letter-pronouncing]
                                    :connections #{{:handler :introduce-letter
                                                    :name    "next"}}}
           :introduce-letter       {:path        [:introduce-letter]
                                    :entity      :action
                                    :children    [:vaca-voice-sound-look :init-letter :show-letter :pronounce-letter]
                                    :connections #{{:handler :letter-drawing-prepare
                                                    :name    "next"}}}
           :letter-drawing-prepare {:path        [:letter-drawing-prepare]
                                    :entity      :action
                                    :children    [:vaca-voice-help-mari :mari-voice-sure]
                                    :connections #{{:handler :letter-drawing
                                                    :name    "next"}}}
           :letter-drawing         {:path        [:letter-drawing]
                                    :entity      :action
                                    :children    [:letter-drawing-0
                                                  :letter-drawing-1
                                                  :letter-drawing-2
                                                  :letter-drawing-3
                                                  :letter-drawing-4
                                                  :letter-drawing-5]
                                    :connections #{{:handler :invite-user
                                                    :name    "next"}}}
           :invite-user            {:path        [:invite-user]
                                    :entity      :action
                                    :children    [:vaca-voice-your-turn :finish-activity :mari-click-to-practice]
                                    :connections #{}}})
