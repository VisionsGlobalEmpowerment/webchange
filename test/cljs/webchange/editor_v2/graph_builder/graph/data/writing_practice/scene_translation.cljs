(ns webchange.editor-v2.graph-builder.graph.data.writing-practice.scene-translation)

(def data {:start                       {:path        [:start]
                                         :entity      :trigger
                                         :children    []
                                         :connections #{{:handler :mari-voice-intro
                                                         :name    "next"}}}
           :mari-voice-intro            {:path        [:mari-voice-intro]
                                         :entity      :action
                                         :children    []
                                         :connections #{{:handler :mari-voice-go-next-practice
                                                         :name    "next"}}}
           :mari-voice-go-next-practice {:path        [:mari-voice-go-next-practice]
                                         :entity      :action
                                         :children    [:mari-voice-go-next-practice-0 :mari-voice-go-next-practice-1]
                                         :connections #{}}
           :next-button                 {:path        [:next-button]
                                         :entity      :object
                                         :children    []
                                         :connections #{{:handler :mari-voice-intro-2
                                                         :name    "next"}}}
           :mari-voice-intro-2          {:path        [:mari-voice-intro-2]
                                         :entity      :action
                                         :children    []
                                         :connections #{}}})
