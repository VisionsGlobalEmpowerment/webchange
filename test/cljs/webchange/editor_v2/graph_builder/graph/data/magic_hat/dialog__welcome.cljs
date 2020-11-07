(ns webchange.editor-v2.graph-builder.graph.data.magic-hat.dialog--welcome)

(def data {:mari-voice-welcome {:data        {:phrase-text "Hello my friend! Did you see the wizard’s hat?"}
                                :path        [:mari-voice-welcome]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :root
                                                :name     "next"
                                                :sequence :intro
                                                :handler  :mari-voice-intro}}}
           :mari-voice-intro   {:data        {:phrase-text "There are lots of magical pictures in the wizards hat.  Listen to the chant, and then touch the picture that completes it."}
                                :path        [:mari-voice-intro]
                                :entity      :action
                                :children    []
                                :connections #{}}})
