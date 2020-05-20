(ns webchange.editor-v2.graph-builder.graph.data.hide-n-seek.scene-translation)

(def data {:mari-audio-try-again
           {:path        [:mari-audio-try-again],
            :entity      :action,
            :children    [],
            :connections #{}},
           :box3-click-area
           {:path     [:box3-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :box11-click-area
           {:path     [:box11-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :box2-click-area
           {:path     [:box2-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :box4-click-area
           {:path     [:box4-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-correct}
                        {:previous :root, :name "click", :handler :mari-audio-try-again}}},
           :box9-click-area
           {:path     [:box9-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :mari-audio-correct
           {:path     [:mari-audio-correct],
            :entity   :action,
            :children [],
            :connections
                      #{{:previous :box9-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box3-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box1-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box10-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box4-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box2-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box8-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box5-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box7-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box11-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}
                        {:previous :box6-click-area,
                         :name     "next",
                         :sequence :pick-correct,
                         :handler  :make-riddle}}},
           :start
           {:path     [:start],
            :entity   :trigger,
            :children [],
            :connections
                      #{{:previous :root, :name "start", :handler :mari-welcome-audio}}},
           :box5-click-area
           {:path     [:box5-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-correct}
                        {:previous :root, :name "click", :handler :mari-audio-try-again}}},
           :box10-click-area
           {:path     [:box10-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :mari-welcome-audio
           {:path     [:mari-welcome-audio],
            :entity   :action,
            :children [],
            :connections
                      #{{:previous :start,
                         :name     "next",
                         :sequence :start-scene,
                         :handler  :make-riddle}}},
           :make-riddle
           {:path        [:make-riddle],
            :entity      :action,
            :children    [],
            :connections #{}},
           :box7-click-area
           {:path     [:box7-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-correct}
                        {:previous :root, :name "click", :handler :mari-audio-try-again}}},
           :box8-click-area
           {:path     [:box8-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-correct}
                        {:previous :root, :name "click", :handler :mari-audio-try-again}}},
           :box6-click-area
           {:path     [:box6-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}},
           :box1-click-area
           {:path     [:box1-click-area],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "click", :handler :mari-audio-try-again}
                        {:previous :root, :name "click", :handler :mari-audio-correct}}}})
