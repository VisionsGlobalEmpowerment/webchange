(ns webchange.editor-v2.graph-builder.graph.data.hide-n-seek.scene-translation)

(def data {:mari-audio-try-again {:path        [:mari-audio-try-again]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box3-click-area      {:path        [:box3-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box11-click-area     {:path        [:box11-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box2-click-area      {:path        [:box2-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box4-click-area      {:path        [:box4-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box9-click-area      {:path        [:box9-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :mari-audio-correct   {:path        [:mari-audio-correct]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:handler :make-riddle
                                                  :name    "next"}}}
           :start                {:path        [:start]
                                  :entity      :trigger
                                  :children    []
                                  :connections #{{:handler :mari-welcome-audio
                                                  :name    "next"}}}
           :box5-click-area      {:path        [:box5-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box10-click-area     {:path        [:box10-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :mari-welcome-audio   {:path        [:mari-welcome-audio]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:handler :make-riddle
                                                  :name    "next"}}}
           :make-riddle          {:path        [:make-riddle]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box7-click-area      {:path        [:box7-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box8-click-area      {:path        [:box8-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box6-click-area      {:path        [:box6-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}
           :box1-click-area      {:path        [:box1-click-area]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-audio-try-again
                                                  :name    "next"}
                                                 {:handler :mari-audio-correct
                                                  :name    "next"}}}})
