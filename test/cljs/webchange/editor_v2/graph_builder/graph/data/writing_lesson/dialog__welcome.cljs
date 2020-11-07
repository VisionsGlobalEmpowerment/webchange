(ns webchange.editor-v2.graph-builder.graph.data.writing-lesson.dialog--welcome)

(def data {:vaca-voice-welcome {:data        {:phrase-text "Welcome to the library genius! Isn’t it such a fantastic place? I love how libraries are filled with so many books.  Hmmm...Well, are you ready to learn about letters?  Let’s begin!"}
                                :path        [:vaca-voice-welcome]
                                :entity      :action
                                :children    []
                                :connections #{{:previous :root
                                                :name     "next"
                                                :sequence :welcome-voice
                                                :handler  :mari-voice-welcome}}}
           :mari-voice-welcome {:data        {:phrase-text "Hello!"}
                                :path        [:mari-voice-welcome]
                                :entity      :action
                                :children    []
                                :connections #{}}})
