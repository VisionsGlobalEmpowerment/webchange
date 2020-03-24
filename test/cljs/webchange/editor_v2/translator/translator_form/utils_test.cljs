(ns webchange.editor-v2.translator.translator-form.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-audios get-dialog-data]]))

(deftest test-get-audios
  (testing "getting scene audio assets"
    (let [scene-data {:assets [{:url "/raw/audio/audio_1.mp3" :type "audio" :alias "alias_1"}
                               {:url "/raw/audio/audio_2.m4a" :type "audio"}]}
          graph {}]
      (let [actual-result (get-audios scene-data graph)
            expected-result [{:alias "alias_1"
                              :key   "/raw/audio/audio_1.mp3"
                              :url   "/raw/audio/audio_1.mp3"
                              :date  nil}
                             {:alias nil
                              :key   "/raw/audio/audio_2.m4a"
                              :url   "/raw/audio/audio_2.m4a"
                              :date  nil}]]
        (is (= actual-result expected-result)))))
  (testing "scene audio assets are sorted by date"
    (let [scene-data {:assets [{:url "/raw/audio/audio_1.mp3" :type "audio" :alias "alias_1"}
                               {:url "/raw/audio/audio_2.m4a" :type "audio" :date 1584715285776}
                               {:url "/raw/audio/audio_3.mp3" :type "audio" :alias "alias_3" :date 1584715294636}]}
          graph {}]
      (let [actual-result (get-audios scene-data graph)
            expected-result [{:alias "alias_3"
                              :key   "/raw/audio/audio_3.mp3"
                              :url   "/raw/audio/audio_3.mp3"
                              :date  1584715294636}
                             {:alias nil
                              :key   "/raw/audio/audio_2.m4a"
                              :url   "/raw/audio/audio_2.m4a"
                              :date  1584715285776}
                             {:alias "alias_1"
                              :key   "/raw/audio/audio_1.mp3"
                              :url   "/raw/audio/audio_1.mp3"
                              :date  nil}]]
        (is (= actual-result expected-result)))))
  (testing "getting scene external audios"
    (let [scene-data {:audio {:audio-1-key "/raw/audio/audio-1.mp3"
                              :audio-2-key "/raw/audio/audio-2.mp3"}}
          graph {}]
      (let [actual-result (get-audios scene-data graph)
            expected-result [{:alias nil
                              :key   "audio-1-key"
                              :url   "/raw/audio/audio-1.mp3"}
                             {:alias nil
                              :key   "audio-2-key"
                              :url   "/raw/audio/audio-2.mp3"}]]
        (is (= actual-result expected-result)))))
  (testing "getting concept audios"
    (let [scene-data {}
          graph {:a {:data {:concept-action true
                            :type           "animation-sequence"
                            :audio          "/raw/audio/concept-1-audio-1.m4a"}}
                 :b {:data {:concept-action true
                            :type           "audio"
                            :id             "/raw/audio/concept-2-audio-1.m4a"}}}]
      (let [actual-result (get-audios scene-data graph)
            expected-result [{:alias nil
                              :key   "/raw/audio/concept-1-audio-1.m4a"
                              :url   "/raw/audio/concept-1-audio-1.m4a"}
                             {:alias nil
                              :key   "/raw/audio/concept-2-audio-1.m4a"
                              :url   "/raw/audio/concept-2-audio-1.m4a"}]]
        (is (= actual-result expected-result)))))
  (testing "removing duplicates"
    (let [scene-data {:assets [{:url "/raw/audio/audio-1.mp3" :type "audio" :alias "alias-1"}]
                      :audio  {:audio-1-key "/raw/audio/audio-1.mp3"}}
          graph {}]
      (let [actual-result (get-audios scene-data graph)
            expected-result [{:alias "alias-1"
                              :key   "audio-1-key"
                              :url   "/raw/audio/audio-1.mp3"
                              :date  nil}]]
        (is (= actual-result expected-result))))))

(deftest test-get-dialog-data--text-in-parent-defined-target
  (let [phrase-node {:data {:phrase      :move-picture
                            :phrase-text "Move the picture onto the swing."
                            :target      "mari"}}
        dialog-graph {:mari-move-to-start-1 {:connections #{}}}]
    (let [actual-result (get-dialog-data phrase-node dialog-graph)
          expected-result [{:target      "mari"
                            :phrase-text "Move the picture onto the swing."}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-dialog-data--text-in-parent-undefined-target
  (let [phrase-node {:data {:phrase      :move-picture
                            :phrase-text "Move the picture onto the swing."}}
        dialog-graph {:mari-move-to-start-1 {:connections #{}}}]
    (let [actual-result (get-dialog-data phrase-node dialog-graph)
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
                                              :connections #{}}}]
    (let [actual-result (get-dialog-data phrase-node dialog-graph)
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
