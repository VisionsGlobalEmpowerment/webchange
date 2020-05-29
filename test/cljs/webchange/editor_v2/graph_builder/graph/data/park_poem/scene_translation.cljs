(ns webchange.editor-v2.graph-builder.graph.data.park-poem.scene-translation)

(def data {:start                 {:path        [:start]
                                   :entity      :trigger
                                   :children    []
                                   :connections #{{:handler :mari-voice-welcome
                                                   :name    "next"}}}
           :mari-voice-welcome    {:path        [:mari-voice-welcome]
                                   :entity      :action
                                   :children    [:mari-voice-welcome-0 :mari-voice-welcome-1]
                                   :connections #{}}
           :image-story-1         {:path        [:image-story-1]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :run-concept-poem
                                                   :name    "next"}}}
           :image-story-2         {:path        [:image-story-2]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :run-concept-poem
                                                   :name    "next"}}}
           :image-story-3         {:path        [:image-story-3]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:handler :run-concept-poem
                                                   :name    "next"}}}
           :run-concept-poem      {:path        [:run-concept-poem]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:handler :mari-voice-now-repeat
                                                   :name    "next"}
                                                  {:handler :mari-voice-finish
                                                   :name    "next"}}}
           :mari-voice-now-repeat {:path        [:mari-voice-now-repeat]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :mari-voice-finish     {:path        [:mari-voice-finish]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}})
