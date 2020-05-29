(ns webchange.editor-v2.graph-builder.graph.data.cinema-video.dialog--finish)

(def data {:mari-voice-touch-again-1 {:data        {:phrase-text "Touch here to view the video again"}
                                      :path        [:mari-voice-touch-again-1]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :vaca-voice-very-good
                                                      :name     "next"
                                                      :sequence :show-play-again-form
                                                      :handler  :mari-voice-touch-again-2}}}
           :vaca-voice-very-good     {:data        {:phrase-text "Very good!"}
                                      :path        [:vaca-voice-very-good]
                                      :entity      :action
                                      :children    []
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :sequence :play-video-finish
                                                      :handler  :mari-voice-touch-again-1}}}
           :mari-voice-touch-again-2 {:data        {:phrase-text "or touch here to go to your next activity.\n"}
                                      :path        [:mari-voice-touch-again-2]
                                      :entity      :action
                                      :children    []
                                      :connections #{}}})
