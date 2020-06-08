(ns webchange.editor-v2.graph-builder.graph.data.cycling.scene-translation)

(def data {:mari-says-correct    {:path        [:mari-says-correct]
                                  :entity      :action
                                  :children    [:mari-says-correct-0 :mari-says-correct-1]
                                  :connections #{{:handler :voice-high-var
                                                  :name    "next"}}}
           :mari-says-wrong      {:path        [:mari-says-wrong]
                                  :entity      :action
                                  :children    [:mari-says-wrong-0 :mari-says-wrong-1]
                                  :connections #{}}
           :start                {:path        [:start]
                                  :entity      :trigger
                                  :children    []
                                  :connections #{{:handler :mari-welcome-audio-1
                                                  :name    "next"}}}
           :mari-welcome-audio-1 {:path        [:mari-welcome-audio-1]
                                  :entity      :action
                                  :children    []
                                  :connections #{{:handler :voice-high-var
                                                  :name    "next"}}}
           :voice-high-var       {:path        [:voice-high-var]
                                  :entity      :action
                                  :children    []
                                  :connections #{}}
           :box1                 {:path        [:box1]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-says-correct
                                                  :name    "next"}
                                                 {:handler :voice-high-var
                                                  :name    "next"}
                                                 {:handler :mari-says-wrong
                                                  :name    "next"}}}
           :box2                 {:path        [:box2]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-says-correct
                                                  :name    "next"}
                                                 {:handler :voice-high-var
                                                  :name    "next"}
                                                 {:handler :mari-says-wrong
                                                  :name    "next"}}}
           :box3                 {:path        [:box3]
                                  :entity      :object
                                  :children    []
                                  :connections #{{:handler :mari-says-correct
                                                  :name    "next"}
                                                 {:handler :voice-high-var
                                                  :name    "next"}
                                                 {:handler :mari-says-wrong
                                                  :name    "next"}}}})
