(ns webchange.editor-v2.graph-builder.graph.data.swings.dialog--welcome)

(def data {:mari-welcome-audio-2  {:data        {:phrase-text " Si! Vamos a jugar el los columpios! Nuestros amigos quieren ir a jugar en los columpios! Vamos a darles un turno a cada uno."}
                                   :path        [:mari-welcome-audio-2]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :vera-welcome-audio-1
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :vera-welcome-audio-3}}}
           :vera-welcome-audio-8  {:data        {:phrase-text "Hola Senora Piedra. Como esta?"}
                                   :path        [:vera-welcome-audio-8]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :rock-welcome-audio-7
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :rock-welcome-audio-9}}}
           :mari-welcome-audio-10 {:data        {:phrase-text "Ok pequeno genio! Vamos a comenzar!"}
                                   :path        [:mari-welcome-audio-10]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :rock-welcome-audio-5  {:data        {:phrase-text "Que amable!"}
                                   :path        [:rock-welcome-audio-5]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :mari-welcome-audio-4
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :mari-welcome-audio-6}}}
           :mari-welcome-audio-6  {:data        {:phrase-text "Oh, hola Senora Piedra! Es un placer verla!"}
                                   :path        [:mari-welcome-audio-6]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :rock-welcome-audio-5
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :rock-welcome-audio-7}}}
           :vera-welcome-audio-3  {:data        {:phrase-text "Ok. Voy a darles turno a cada uno."}
                                   :path        [:vera-welcome-audio-3]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :mari-welcome-audio-2
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :mari-welcome-audio-4}}}
           :rock-welcome-audio-7  {:data        {:phrase-text "Hola Mari. Igualmente! Es un placer verte!"}
                                   :path        [:rock-welcome-audio-7]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :mari-welcome-audio-6
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :vera-welcome-audio-8}}}
           :rock-welcome-audio-9  {:data        {:phrase-text "Hola mi amiga. Estoy muy bien, gracias!"}
                                   :path        [:rock-welcome-audio-9]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :vera-welcome-audio-8
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :mari-welcome-audio-10}}}
           :vera-welcome-audio-1  {:data        {:phrase-text "Hola Mari! Vamos a jugar en los columpios? A mi me gustan los columpios."}
                                   :path        [:vera-welcome-audio-1]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :mari-welcome-audio-2}}}
           :mari-welcome-audio-4  {:data        {:phrase-text "Muy bien! Buen trabajo!"}
                                   :path        [:mari-welcome-audio-4]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :vera-welcome-audio-3
                                                   :name     "next"
                                                   :sequence :welcome-audio
                                                   :handler  :rock-welcome-audio-5}}}})
