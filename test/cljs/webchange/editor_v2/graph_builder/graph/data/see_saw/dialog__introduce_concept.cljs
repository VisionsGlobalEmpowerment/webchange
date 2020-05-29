(ns webchange.editor-v2.graph-builder.graph.data.see-saw.dialog--introduce-concept)

(def data {:game-voice-action-low-copy-1 {:data        {:phrase-text "Ardilla. Ardilla.  ARDILLA!"}
                                          :path        [:game-voice-action-low]
                                          :entity      :action
                                          :children    []
                                          :connections #{{:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}
                                                         {:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-low-copy-0}
                                                         {:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-1}}}
           :game-voice-action-copy-0     {:data        {:phrase-text "Ardilla. Ardilla.  ARDILLA!"}
                                          :path        [:game-voice-action]
                                          :entity      :action
                                          :children    []
                                          :connections #{}}
           :game-voice-action-copy-1     {:data        {:phrase-text "Ardilla. Ardilla.  ARDILLA!"}
                                          :path        [:game-voice-action]
                                          :entity      :action
                                          :children    []
                                          :connections #{{:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}
                                                         {:previous :game-voice-action-low-copy-1
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}
                                                         {:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-low-copy-0}
                                                         {:previous :game-voice-action-low-copy-1
                                                          :name     "next"
                                                          :handler  :game-voice-action-low-copy-0}}}
           :game-voice-action-low-copy-0 {:data        {:phrase-text "Ardilla. Ardilla.  ARDILLA!"}
                                          :path        [:game-voice-action-low]
                                          :entity      :action
                                          :children    []
                                          :connections #{{:previous :game-voice-action-copy-1
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}
                                                         {:previous :root
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}
                                                         {:previous :game-voice-action-low-copy-1
                                                          :name     "next"
                                                          :handler  :game-voice-action-copy-0}}}})
