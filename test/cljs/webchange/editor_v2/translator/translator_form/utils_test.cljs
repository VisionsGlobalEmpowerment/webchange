(ns webchange.editor-v2.translator.translator-form.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-audios]]))

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
