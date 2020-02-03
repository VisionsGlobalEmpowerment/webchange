(ns webchange.editor-v2.graph-builder.graph.phrases-graph--swings-expected)

(def data {:mari-finish
           {:path        [:mari-finish],
            :entity      :action,
            :children    [:mari-finish-0 :mari-finish-1],
            :connections #{}},
           :welcome-audio
           {:path   [:welcome-audio],
            :entity :action,
            :children
                    [:vera-welcome-audio-1
                     :mari-welcome-audio-2
                     :vera-welcome-audio-3
                     :mari-welcome-audio-4
                     :rock-welcome-audio-5
                     :mari-welcome-audio-6
                     :rock-welcome-audio-7
                     :vera-welcome-audio-8
                     :rock-welcome-audio-9
                     :mari-welcome-audio-10],
            :connections
                    #{{:previous :start,
                       :name     "next",
                       :sequence :welcome-audio,
                       :handler  :mari-move-to-start}}},
           :start
           {:path     [:start],
            :entity   :trigger,
            :children [],
            :connections
                      #{{:previous :root, :name "start", :handler :welcome-audio}}},
           :try-another
           {:path        [:try-another],
            :entity      :action,
            :children    [],
            :connections #{}},
           :dialog-var
           {:path     [:dialog-var],
            :entity   :action,
            :children [],
            :connections
            ;; ToDo: too many connections (whole set for each parent sequence)
                      #{{:previous :box1,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :mari-finish}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :try-another}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :try-another}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :mari-finish}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :mari-finish}
                        {:previous :box1,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :try-another}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :mari-finish}
                        {:previous :box1,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :mari-finish}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :try-another}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :try-another}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :mari-finish}
                        {:previous :box1,
                         :name     "next",
                         :sequence :box-1-start,
                         :handler  :try-another}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :try-another}
                        {:previous :box3,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :mari-finish}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :mari-finish}
                        {:previous :box2,
                         :name     "next",
                         :sequence :box-3-start,
                         :handler  :try-another}
                        {:previous :box1,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :mari-finish}
                        {:previous :box1,
                         :name     "next",
                         :sequence :box-2-start,
                         :handler  :try-another}}},
           :box3
           {:path     [:box3],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "drag-end", :handler nil}
                        {:previous :root, :name "drag-end", :handler :dialog-var}}},
           :box1
           {:path     [:box1],
            :entity   :object,
            :children [],
            :connections
                      #{{:previous :root, :name "drag-end", :handler nil}
                        {:previous :root, :name "drag-end", :handler :dialog-var}}},
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
                      #{{:previous :root, :name "drag-end", :handler nil}
                        {:previous :root, :name "drag-end", :handler :dialog-var}}}})
