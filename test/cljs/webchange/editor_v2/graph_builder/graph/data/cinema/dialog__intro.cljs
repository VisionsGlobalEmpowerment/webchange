(ns webchange.editor-v2.graph-builder.graph.data.cinema.dialog--intro)

(def data {:intro-4               {:data        {:phrase-text "Listen to the sounds at the beginning of each word.  For example, When we say the word “Ant”, we hear an “aaaaa” sound.  “Aa- Aaant. Aa - Aant."}
                                   :path        [:intro 4]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :intro-2
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-8}}}
           :intro-2               {:data        {:phrase-text "Sure thing, Senora Vaca!"}
                                   :path        [:intro 2]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :intro-1
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-4}}}
           :vaca-voice-chanting-4 {:data        {:phrase-text "Let’s sing it"}
                                   :path        [:vaca-voice-chanting-4]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :intro-9
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-15}}}
           :intro-1               {:data        {:phrase-text "Hello everyone! Are you ready to practice letter sounds? Look at all these words you already know! Let’s review the sound at the beginning of each word. Mari, can you fly around and point to each picture and letter?"}
                                   :path        [:intro 1]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :root
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-2}}}
           :intro-15              {:data        {:phrase-text "At the beginning of the word “Oso”, we hear the “oooooo” sound. Ooooso.  Ooooso."}
                                   :path        [:intro 15]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :vaca-voice-chanting-4
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-16}}}
           :intro-16              {:data        {:phrase-text "This is what the oooooo sound looks like. oooooo. Let’s sing it -  Mat, mat, M-M-Mmm!!"}
                                   :path        [:intro 16]
                                   :entity      :action
                                   :children    []
                                   :connections #{}}
           :intro-8               {:data        {:phrase-text "Let’s say the word and beginning sound 3 times like this - Ant! Ant!  a-a-A!"}
                                   :path        [:intro 8]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :intro-4
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :intro-9}}}
           :intro-9               {:data        {:phrase-text "Say it with me 3 times! - Ant! Ant!  a-a-A! Ant! Ant!  a-a-A! Ant! Ant!  a-a-A!"}
                                   :path        [:intro 9]
                                   :entity      :action
                                   :children    []
                                   :connections #{{:previous :intro-8
                                                   :name     "next"
                                                   :sequence :intro
                                                   :handler  :vaca-voice-chanting-4}}}})
