(ns webchange.editor-v2.translator.translator-form.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-dialog-data]]))

(deftest test-get-dialog-data--text-in-parent-defined-target
  (let [phrase-node {:data {:phrase      :move-picture
                            :phrase-text "Move the picture onto the swing."
                            :target      "mari"}}
        dialog-graph {:mari-move-to-start-1 {:connections #{}}}
        get-actual-data (fn [node-data] {:data (:data node-data)})]
    (let [actual-result (get-dialog-data phrase-node dialog-graph get-actual-data)
          expected-result [{:target      "mari"
                            :phrase-text "Move the picture onto the swing."}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-dialog-data--text-in-parent-undefined-target
  (let [phrase-node {:data {:phrase      :move-picture
                            :phrase-text "Move the picture onto the swing."}}
        dialog-graph {:mari-move-to-start-1 {:connections #{}}}
        get-actual-data (fn [node-data] {:data (:data node-data)})]
    (let [actual-result (get-dialog-data phrase-node dialog-graph get-actual-data)
          expected-result [{:phrase-text "Move the picture onto the swing."}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-dialog-data--text-in-children
  (let [phrase-node {:data {:phrase :welcome}}
        dialog-graph {:vera-welcome-audio-1  {:data        {:phrase-text "Hello Mari! Are we going to play on the swings now? I like the swings."
                                                            :target      "vera"}
                                              :connections #{{:previous :root
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :mari-welcome-audio-2}}}
                      :mari-welcome-audio-2  {:data        {:phrase-text "Yes! We are going to play on the swings! Our friends [ardilla, oso and imán] want to go on the swings. Let’s give each of them a turn."
                                                            :target      "mari"}
                                              :connections #{{:previous :vera-welcome-audio-1
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :vera-welcome-audio-3}}}
                      :vera-welcome-audio-3  {:data        {:phrase-text "Ok. I’m going to give everyone a turn."
                                                            :target      "vera"}
                                              :connections #{{:previous :mari-welcome-audio-2
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :mari-welcome-audio-4}}}
                      :mari-welcome-audio-4  {:data        {:phrase-text "Good job!"
                                                            :target      "mari"}
                                              :connections #{{:previous :vera-welcome-audio-3
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :rock-welcome-audio-5}}}
                      :rock-welcome-audio-5  {:data        {:phrase-text "That’s very kind of you!"
                                                            :target      "rock"}
                                              :connections #{{:previous :mari-welcome-audio-4
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :mari-welcome-audio-6}}}
                      :mari-welcome-audio-6  {:data        {:phrase-text "Oh, hello Senora Piedra! It’s a pleasure to see you."
                                                            :target      "mari"}
                                              :connections #{{:previous :rock-welcome-audio-5
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :rock-welcome-audio-7}}}
                      :rock-welcome-audio-7  {:data        {:phrase-text "Hello Mari. It’s a pleasure to see you too."
                                                            :target      "rock"}
                                              :connections #{{:previous :mari-welcome-audio-6
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :vera-welcome-audio-8}}}
                      :vera-welcome-audio-8  {:data        {:phrase-text "Hola Senor Piedra. How are you?"
                                                            :target      "vera"}
                                              :connections #{{:previous :rock-welcome-audio-7
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :rock-welcome-audio-9}}}
                      :rock-welcome-audio-9  {:data        {:phrase-text "Hello my friend. I’m very well, thank you."
                                                            :target      "rock"}
                                              :connections #{{:previous :vera-welcome-audio-8
                                                              :name     "next"
                                                              :sequence :welcome-audio
                                                              :handler  :mari-welcome-audio-10}}}
                      :mari-welcome-audio-10 {:data        {:phrase-text "Ok little genius, let’s begin!"
                                                            :target      "mari"}
                                              :connections #{}}}
        get-actual-data (fn [node-data] {:data (:data node-data)})]
    (let [actual-result (get-dialog-data phrase-node dialog-graph get-actual-data)
          expected-result [{:target      "vera"
                            :phrase-text "Hello Mari! Are we going to play on the swings now? I like the swings."}
                           {:target      "mari"
                            :phrase-text "Yes! We are going to play on the swings! Our friends [ardilla, oso and imán] want to go on the swings. Let’s give each of them a turn."}
                           {:target      "vera"
                            :phrase-text "Ok. I’m going to give everyone a turn."}
                           {:target      "mari"
                            :phrase-text "Good job!"}
                           {:target      "rock"
                            :phrase-text "That’s very kind of you!"}
                           {:target      "mari"
                            :phrase-text "Oh, hello Senora Piedra! It’s a pleasure to see you."}
                           {:target      "rock"
                            :phrase-text "Hello Mari. It’s a pleasure to see you too."}
                           {:target      "vera"
                            :phrase-text "Hola Senor Piedra. How are you?"}
                           {:target      "rock"
                            :phrase-text "Hello my friend. I’m very well, thank you."}
                           {:target      "mari"
                            :phrase-text "Ok little genius, let’s begin!"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
