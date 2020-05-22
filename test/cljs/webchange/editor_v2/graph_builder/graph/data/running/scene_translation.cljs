(ns webchange.editor-v2.graph-builder.graph.data.running.scene-translation)

(def data {:start                 {:path        [:start]
                                   :entity      :trigger
                                   :children    []
                                   :connections #{{:handler :mari-voice-welcome
                                                   :name    "next"}}}
           :mari-voice-welcome    {:path        [:mari-voice-welcome]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:handler :current-concept-chant
                                                   :name    "next"}}}
           :current-concept-chant {:path        [:current-concept-chant]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :box1                  {:path        [:box1]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :mari-voice-correct
                                                   :name    "next"}
                                                  {:handler :mari-voice-wrong
                                                   :name    "next"}}}
           :box2                  {:path        [:box2]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :mari-voice-correct
                                                   :name    "next"}
                                                  {:handler :mari-voice-wrong
                                                   :name    "next"}}}
           :box3                  {:path        [:box3]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :mari-voice-correct
                                                   :name    "next"}
                                                  {:handler :mari-voice-wrong
                                                   :name    "next"}}}
           :mari-voice-correct    {:path        [:mari-voice-correct]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:handler :current-concept-chant
                                                   :name    "next"}}}
           :mari-voice-wrong      {:path        [:mari-voice-wrong]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:handler :current-concept-chant
                                                   :name    "next"}}}})
