(ns webchange.editor-v2.graph-builder.graph.phrases-graph--see-saw-expected)

(def data {:mari-finish
           {:path        [:mari-finish],
            :entity      :action,
            :children    [:mari-finish-0 :mari-finish-1],
            :connections #{}},
           :start
           {:path     [:start],
            :entity   :trigger,
            :children [],
            :connections
                      #{{:previous :root, :name "start", :handler :mari-welcome-audio}}},
           :try-another
           {:path     [:try-another],
            :entity   :action,
            :children [],
            :connections
                      #{{:previous :introduce-concept,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :mari-move-to-start}
                        {:previous :introduce-concept,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :mari-move-to-start}}},
           :mari-welcome-audio
           {:path     [:mari-welcome-audio],
            :entity   :action,
            :children [],
            :connections
                      #{{:previous :start,
                         :name     "next",
                         :sequence :start-scene,
                         :handler  :mari-move-to-start}}},
           :box3
           {:path     [:box3],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "drag-end", :handler :introduce-concept}}},
           :box1
           {:path     [:box1],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "drag-end", :handler :introduce-concept}}},
           :mari-move-to-start
           {:path        [:mari-move-to-start],
            :entity      :action,
            :children    [:mari-move-to-start-0 :mari-move-to-start-1],
            :connections #{}},
           :box2
           {:path     [:box2],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "drag-end", :handler :introduce-concept}}},
           :introduce-concept
           {:path     [:introduce-concept],
            :entity   :action,
            :children [:go-down-1 :go-up-1 :go-down-2 :go-up-2],
            :connections
                      #{{:previous :box3,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :mari-finish}
                        {:previous :box3,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :try-another}
                        {:previous :box1,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :mari-finish}
                        {:previous :box1,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :try-another}
                        {:previous :box2,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :mari-finish}
                        {:previous :box2,
                         :name     "next",
                         :sequence :introduce-concept,
                         :handler  :try-another}}}})
