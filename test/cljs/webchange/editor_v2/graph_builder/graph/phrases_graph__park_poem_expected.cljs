(ns webchange.editor-v2.graph-builder.graph.phrases-graph--park-poem-expected)

(def data {:start                 {:path        [:start]
                                   :entity      :trigger
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "start"
                                                   :handler  :mari-voice-welcome}}}
           :mari-voice-welcome    {:path        [:mari-voice-welcome]
                                   :entity      :action
                                   :children    [:mari-voice-welcome-0 :mari-voice-welcome-1]
                                   :connections #{}}
           :image-story-1         {:path        [:image-story-1]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "click"
                                                   :handler  :run-concept-poem}}}
           :image-story-2         {:path        [:image-story-2]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "click"
                                                   :handler  :run-concept-poem}}}
           :image-story-3         {:path        [:image-story-3]
                                   :entity      :object
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "click"
                                                   :handler  :run-concept-poem}}}
           :run-concept-poem      {:path        [:run-concept-poem]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :image-story-3
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-now-repeat}
                                                  {:previous :image-story-2
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-now-repeat}
                                                  {:previous :image-story-1
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-now-repeat}
                                                  {:previous :image-story-3
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-finish}
                                                  {:previous :image-story-2
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-finish}
                                                  {:previous :image-story-1
                                                   :name     "next"
                                                   :sequence :run-story
                                                   :handler  :mari-voice-finish}}}
           :mari-voice-now-repeat {:path        [:mari-voice-now-repeat]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :mari-voice-finish     {:path        [:mari-voice-finish]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}})
