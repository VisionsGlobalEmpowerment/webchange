(ns webchange.editor-v2.graph-builder.graph.data.slide.scene-translation)

(def data {:mari-voice-correct     {:path        [:mari-voice-correct]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:handler :finish-activity-0
                                                    :name    "next"}
                                                   {:handler :mari-voice-try-another
                                                    :name    "next"}}}
           :finish-activity-0      {:path        [:finish-activity 0]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}
           :say-selected-concept   {:path        [:say-selected-concept]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:handler :mari-voice-wrong
                                                    :name    "next"}
                                                   {:handler :mari-voice-correct
                                                    :name    "next"}}}
           :mari-voice-welcome     {:path        [:mari-voice-welcome]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:handler :finish-activity-0
                                                    :name    "next"}
                                                   {:handler :mari-voice-try-another
                                                    :name    "next"}}}
           :start                  {:path        [:start]
                                    :entity      :trigger
                                    :children    []
                                    :connections #{{:handler :mari-voice-welcome
                                                    :name    "next"}}}
           :box3                   {:path        [:box3]
                                    :entity      :object
                                    :children    []
                                    :connections #{{:handler :say-selected-concept
                                                    :name    "next"}}}
           :box1                   {:path        [:box1]
                                    :entity      :object
                                    :children    []
                                    :connections #{{:handler :say-selected-concept
                                                    :name    "next"}}}
           :box2                   {:path        [:box2]
                                    :entity      :object
                                    :children    []
                                    :connections #{{:handler :say-selected-concept
                                                    :name    "next"}}}
           :mari-voice-try-another {:path        [:mari-voice-try-another]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:handler :riddle
                                                    :name    "next"}}}
           :riddle                 {:path        [:riddle]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}
           :mari-voice-wrong       {:path        [:mari-voice-wrong]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}}
  )
