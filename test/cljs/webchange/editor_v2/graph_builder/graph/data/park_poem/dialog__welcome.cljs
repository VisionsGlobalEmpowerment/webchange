(ns webchange.editor-v2.graph-builder.graph.data.park-poem.dialog--welcome)

(def data {:mari-voice-welcome-0 {:data        {:phrase-text "Welcome to the park little genius! Do you want to listen to a short story?"}
                                  :path        [:mari-voice-welcome 0]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:previous :root
                                                  :name     "next"
                                                  :sequence :mari-voice-welcome
                                                  :handler  :mari-voice-welcome-1}}}
           :mari-voice-welcome-1 {:data        {:phrase-text "Touch the picture and listen carefully!"}
                                  :path        [:mari-voice-welcome 1]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}})
