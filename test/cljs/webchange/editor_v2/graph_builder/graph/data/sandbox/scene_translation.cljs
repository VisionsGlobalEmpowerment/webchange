(ns webchange.editor-v2.graph-builder.graph.data.sandbox.scene-translation)

(def data {:word-1-state-var-copy-1 {:path         [:word-1-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :mari-more-audio, :name "next"}}
                                     :origin-name  :word-1-state-var
                                     :copy-counter 1}
           :box4                    {:path [:box4], :entity :object, :children [], :connections #{{:handler :word-4-state-var-copy-3, :name "next"}}}
           :word-2-state-var-copy-1 {:path         [:word-2-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :mari-more-audio, :name "next"}}
                                     :origin-name  :word-2-state-var
                                     :copy-counter 1}
           :word-4-state-var-copy-1 {:path         [:word-4-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :mari-more-audio, :name "next"}}
                                     :origin-name  :word-4-state-var
                                     :copy-counter 1}
           :start                   {:path [:start], :entity :trigger, :children [], :connections #{{:handler :mari-welcome-audio, :name "next"}}}
           :mari-welcome-audio      {:path        [:mari-welcome-audio]
                                     :entity      :action
                                     :children    []
                                     :connections #{{:handler :letter-intro, :name "next"}}}
           :word-3-state-var-copy-3 {:path         [:word-3-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-3-state-var-copy-2, :name "next"}}
                                     :origin-name  :word-3-state-var
                                     :copy-counter 3}
           :box3                    {:path [:box3], :entity :object, :children [], :connections #{{:handler :word-3-state-var-copy-3, :name "next"}}}
           :word-4-state-var-copy-2 {:path         [:word-4-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-4-state-var-copy-1, :name "next"}}
                                     :origin-name  :word-4-state-var
                                     :copy-counter 2}
           :word-2-state-var-copy-2 {:path         [:word-2-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-2-state-var-copy-1, :name "next"}}
                                     :origin-name  :word-2-state-var
                                     :copy-counter 2}
           :word-3-state-var-copy-2 {:path         [:word-3-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-3-state-var-copy-1, :name "next"}}
                                     :origin-name  :word-3-state-var
                                     :copy-counter 2}
           :word-2-state-var-copy-3 {:path         [:word-2-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-2-state-var-copy-2, :name "next"}}
                                     :origin-name  :word-2-state-var
                                     :copy-counter 3}
           :word-3-state-var-copy-1 {:path         [:word-3-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :mari-more-audio, :name "next"}}
                                     :origin-name  :word-3-state-var
                                     :copy-counter 1}
           :box1                    {:path [:box1], :entity :object, :children [], :connections #{{:handler :word-1-state-var-copy-3, :name "next"}}}
           :mari-more-audio         {:path        [:mari-more-audio]
                                     :entity      :action
                                     :children    []
                                     :connections #{{:handler :letter-intro, :name "next"}}}
           :letter-intro            {:path        [:letter-intro]
                                     :entity      :action
                                     :children    [:mari-letter-1
                                                   :mari-short-letter-var
                                                   :mari-letter-2
                                                   :mari-short-letter-var
                                                   :mari-letter-3
                                                   :mari-long-letter-var
                                                   :mari-letter-4
                                                   :mari-long-letter-var]
                                     :connections #{}}
           :box2                    {:path [:box2], :entity :object, :children [], :connections #{{:handler :word-2-state-var-copy-3, :name "next"}}}
           :word-1-state-var-copy-3 {:path         [:word-1-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-1-state-var-copy-2, :name "next"}}
                                     :origin-name  :word-1-state-var
                                     :copy-counter 3}
           :word-4-state-var-copy-3 {:path         [:word-4-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-4-state-var-copy-2, :name "next"}}
                                     :origin-name  :word-4-state-var
                                     :copy-counter 3}
           :word-1-state-var-copy-2 {:path         [:word-1-state-var]
                                     :entity       :action
                                     :children     []
                                     :connections  #{{:handler :word-1-state-var-copy-1, :name "next"}}
                                     :origin-name  :word-1-state-var
                                     :copy-counter 2}})
