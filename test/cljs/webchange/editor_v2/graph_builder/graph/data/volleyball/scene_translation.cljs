(ns webchange.editor-v2.graph-builder.graph.data.volleyball.scene-translation)

(def data {:word-ardilla-high    {:path        [:word-ardilla-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box4                 {:path        [:box4]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-say-correct
                                                  :name    "next"}
                                                 {:handler :mari-audio-wrong
                                                  :name    "next"}}}
           :start                {:path        [:start]
                                  :entity      :trigger
                                  :children    []
                                  :connections #{{:handler :word-ardilla-high
                                                  :name    "next"}
                                                 {:handler :word-incendio-high
                                                  :name    "next"}
                                                 {:handler :word-oso-high
                                                  :name    "next"}
                                                 {:handler :mari-welcome-audio-1
                                                  :name    "next"}}}
           :box3                 {:path        [:box3]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-say-correct
                                                  :name    "next"} {:handler :mari-audio-wrong
                                                                    :name    "next"}}}
           :mari-welcome-audio-1 {:path        [:mari-welcome-audio-1]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:handler :word-ardilla-high
                                                  :name    "next"}
                                                 {:handler :word-incendio-high
                                                  :name    "next"}
                                                 {:handler :word-oso-high
                                                  :name    "next"}}}
           :word-incendio-high   {:path        [:word-incendio-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box1                 {:path        [:box1]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-say-correct
                                                  :name    "next"}
                                                 {:handler :mari-audio-wrong
                                                  :name    "next"}}}
           :mari-audio-wrong     {:path        [:mari-audio-wrong]
                                  :entity      :action
                                  :children    [:mari-audio-wrong-0 :mari-audio-wrong-1]
                                  :connections #{}}
           :word-oso-high        {:path        [:word-oso-high]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box2                 {:path        [:box2]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-say-correct
                                                  :name    "next"}
                                                 {:handler :mari-audio-wrong
                                                  :name    "next"}}}
           :mari-say-correct     {:path        [:mari-say-correct]
                                  :entity      :action
                                  :children    [:mari-say-correct-1 :mari-say-correct-2]
                                  :connections #{{:handler :mari-welcome-audio-1
                                                  :name    "next"}
                                                 {:handler :word-oso-high
                                                  :name    "next"}
                                                 {:handler :word-ardilla-high
                                                  :name    "next"}
                                                 {:handler :word-incendio-high
                                                  :name    "next"}}}})
