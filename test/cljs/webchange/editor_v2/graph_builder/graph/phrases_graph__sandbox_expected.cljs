(ns webchange.editor-v2.graph-builder.graph.phrases-graph--sandbox-expected)

(def data {:word-1-state-var-copy-1
           {:path         [:word-1-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-1-state-var-copy-2,
                             :name     "next",
                             :sequence :box-1-start,
                             :handler  :mari-more-audio}
                            {:previous :word-1-state-var-copy-2,
                             :name     "next",
                             :sequence :box-1-start,
                             :handler  :test-concept-complete-fail}},
            :origin-name  :word-1-state-var,
            :copy-counter 1},
           :box4
           {:path        [:box4],
            :entity      :object,
            :children    [],
            :connections #{{:previous :root,
                            :name     "click",
                            :handler  :word-4-state-var-copy-3}}},
           :word-2-state-var-copy-1
           {:path         [:word-2-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-2-state-var-copy-2,
                             :name     "next",
                             :sequence :box-2-start,
                             :handler  :mari-more-audio}
                            {:previous :word-2-state-var-copy-2,
                             :name     "next",
                             :sequence :box-2-start,
                             :handler  :test-concept-complete-fail}},
            :origin-name  :word-2-state-var,
            :copy-counter 1},
           :word-4-state-var-copy-1
           {:path         [:word-4-state-var],
            :entity       :action,
            :children     [],
            :connections  #{},
            :origin-name  :word-4-state-var,
            :copy-counter 1},
           :start
           {:path     [:start],
            :entity   :trigger,
            :children [],
            :connections
                      #{{:previous :root, :name "start", :handler :mari-welcome-audio}}},
           :mari-welcome-audio
           {:path        [:mari-welcome-audio],
            :entity      :action,
            :children    [],
            :connections #{{:previous :start,
                            :name     "next",
                            :sequence :start-scene,
                            :handler  :letter-intro}}},
           :word-3-state-var-copy-3
           {:path         [:word-3-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :box3,
                             :name     "next",
                             :sequence :box-3-start,
                             :handler  :word-3-state-var-copy-2}},
            :origin-name  :word-3-state-var,
            :copy-counter 3},
           :box3
           {:path        [:box3],
            :entity      :object,
            :children    [],
            :connections #{{:previous :root,
                            :name     "click",
                            :handler  :word-3-state-var-copy-3}}},
           :word-4-state-var-copy-2
           {:path         [:word-4-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-4-state-var-copy-3,
                             :name     "next",
                             :sequence :box-4-start,
                             :handler  :word-4-state-var-copy-1}},
            :origin-name  :word-4-state-var,
            :copy-counter 2},
           :word-2-state-var-copy-2
           {:path         [:word-2-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-2-state-var-copy-3,
                             :name     "next",
                             :sequence :box-2-start,
                             :handler  :word-2-state-var-copy-1}},
            :origin-name  :word-2-state-var,
            :copy-counter 2},
           :word-3-state-var-copy-2
           {:path         [:word-3-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-3-state-var-copy-3,
                             :name     "next",
                             :sequence :box-3-start,
                             :handler  :word-3-state-var-copy-1}},
            :origin-name  :word-3-state-var,
            :copy-counter 2},
           :word-2-state-var-copy-3
           {:path         [:word-2-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :box2,
                             :name     "next",
                             :sequence :box-2-start,
                             :handler  :word-2-state-var-copy-2}},
            :origin-name  :word-2-state-var,
            :copy-counter 3},
           :word-3-state-var-copy-1
           {:path         [:word-3-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-3-state-var-copy-2,
                             :name     "next",
                             :sequence :box-3-start,
                             :handler  :mari-more-audio}
                            {:previous :word-3-state-var-copy-2,
                             :name     "next",
                             :sequence :box-3-start,
                             :handler  :test-concept-complete-fail}},
            :origin-name  :word-3-state-var,
            :copy-counter 1},
           :box1
           {:path        [:box1],
            :entity      :object,
            :children    [],
            :connections #{{:previous :root,
                            :name     "click",
                            :handler  :word-1-state-var-copy-3}}},
           :mari-more-audio
           {:path        [:mari-more-audio],
            :entity      :action,
            :children    [],
            :connections #{{:previous :word-2-state-var-copy-1,
                            :name     "next",
                            :sequence :next-concept-workflow,
                            :handler  :letter-intro}
                           {:previous :word-1-state-var-copy-1,
                            :name     "next",
                            :sequence :next-concept-workflow,
                            :handler  :letter-intro}
                           {:previous :word-3-state-var-copy-1,
                            :name     "next",
                            :sequence :next-concept-workflow,
                            :handler  :letter-intro}}},
           :letter-intro
           {:path        [:letter-intro],
            :entity      :action,
            :children
                         [:mari-letter-1
                          :mari-short-letter-var
                          :mari-letter-2
                          :mari-short-letter-var
                          :mari-letter-3
                          :mari-long-letter-var
                          :mari-letter-4
                          :mari-long-letter-var],
            :connections #{}},
           :box2
           {:path        [:box2],
            :entity      :object,
            :children    [],
            :connections #{{:previous :root,
                            :name     "click",
                            :handler  :word-2-state-var-copy-3}}},
           :word-1-state-var-copy-3
           {:path         [:word-1-state-var],
            :entity       :action,
            :children     [],
            :connections
                          #{{:previous :box1,
                             :name     "next",
                             :sequence :box-1-start,
                             :handler  :word-1-state-var-copy-2}},
            :origin-name  :word-1-state-var,
            :copy-counter 3},
           :word-4-state-var-copy-3
           {:path         [:word-4-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :box4,
                             :name     "next",
                             :sequence :box-4-start,
                             :handler  :word-4-state-var-copy-2}},
            :origin-name  :word-4-state-var,
            :copy-counter 3},
           :word-1-state-var-copy-2
           {:path         [:word-1-state-var],
            :entity       :action,
            :children     [],
            :connections  #{{:previous :word-1-state-var-copy-3,
                             :name     "next",
                             :sequence :box-1-start,
                             :handler  :word-1-state-var-copy-1}},
            :origin-name  :word-1-state-var,
            :copy-counter 2}})