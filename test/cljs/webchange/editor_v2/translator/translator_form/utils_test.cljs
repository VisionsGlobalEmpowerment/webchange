(ns webchange.editor-v2.translator.translator-form.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-audios]]))

(deftest test-get-audios
  (testing "getting scene audio assets"
    (let [scene-data {:assets [{:url "/raw/audio/audio_1.mp3" :type "audio" :alias "alias_1"}
                               {:url "/raw/audio/audio_2.m4a" :type "audio"}]}
          concepts []
          used-concept-actions nil]
      (let [actual-result (get-audios scene-data concepts used-concept-actions)
            expected-result [{:alias "alias_1"
                              :key   "/raw/audio/audio_1.mp3"
                              :url   "/raw/audio/audio_1.mp3"}
                             {:alias nil
                              :key   "/raw/audio/audio_2.m4a"
                              :url   "/raw/audio/audio_2.m4a"}]]
        (is (= actual-result expected-result)))))
  (testing "getting scene external audios"
    (let [scene-data {:audio {:audio-1-key "/raw/audio/audio-1.mp3"
                              :audio-2-key "/raw/audio/audio-2.mp3"}}
          concepts []
          used-concept-actions nil]
      (let [actual-result (get-audios scene-data concepts used-concept-actions)
            expected-result [{:alias nil
                              :key   "audio-1-key"
                              :url   "/raw/audio/audio-1.mp3"}
                             {:alias nil
                              :key   "audio-2-key"
                              :url   "/raw/audio/audio-2.mp3"}]]
        (is (= actual-result expected-result)))))
  (testing "getting concept audios"
    (let [scene-data {}
          concepts [{:id   1
                     :name "concept-1"
                     :data {:action-1 {:type "audio"
                                       :id   "/raw/audio/concept-1-audio-1.m4a"}
                            :action-2 {:type  "animation-sequence"
                                       :audio "/raw/audio/concept-1-audio-2.m4a"}}}
                    {:id   2
                     :name "concept-2"
                     :data {:action-1 {:type "audio"
                                       :id   "/raw/audio/concept-2-audio-1.m4a"}
                            :action-2 {:type  "animation-sequence"
                                       :audio "/raw/audio/concept-2-audio-2.m4a"}}}]
          used-concept-actions nil]
      (let [actual-result (get-audios scene-data concepts used-concept-actions)
            expected-result [{:alias nil
                              :key   "/raw/audio/concept-1-audio-1.m4a"
                              :url   "/raw/audio/concept-1-audio-1.m4a"}
                             {:alias nil
                              :key   "/raw/audio/concept-1-audio-2.m4a"
                              :url   "/raw/audio/concept-1-audio-2.m4a"}
                             {:alias nil
                              :key   "/raw/audio/concept-2-audio-1.m4a"
                              :url   "/raw/audio/concept-2-audio-1.m4a"}
                             {:alias nil
                              :key   "/raw/audio/concept-2-audio-2.m4a"
                              :url   "/raw/audio/concept-2-audio-2.m4a"}]]
        (is (= actual-result expected-result)))))
  (testing "getting concept audios filtered by actions"
    (let [scene-data {}
          concepts [{:id   1
                     :name "concept-1"
                     :data {:action-1 {:type "audio"
                                       :id   "/raw/audio/concept-1-audio-1.m4a"}
                            :action-2 {:type  "animation-sequence"
                                       :audio "/raw/audio/concept-1-audio-2.m4a"}}}
                    {:id   2
                     :name "concept-2"
                     :data {:action-1 {:type "audio"
                                       :id   "/raw/audio/concept-2-audio-1.m4a"}
                            :action-2 {:type  "animation-sequence"
                                       :audio "/raw/audio/concept-2-audio-2.m4a"}}}]
          used-concept-actions [:action-2]]
      (let [actual-result (get-audios scene-data concepts used-concept-actions)
            expected-result [{:alias nil
                              :key   "/raw/audio/concept-1-audio-2.m4a"
                              :url   "/raw/audio/concept-1-audio-2.m4a"}
                             {:alias nil
                              :key   "/raw/audio/concept-2-audio-2.m4a"
                              :url   "/raw/audio/concept-2-audio-2.m4a"}]]
        (is (= actual-result expected-result)))))
  (testing "removing duplicates"
    (let [scene-data {:assets [{:url "/raw/audio/audio-1.mp3" :type "audio" :alias "alias-1"}]
                      :audio  {:audio-1-key "/raw/audio/audio-1.mp3"}}
          concepts [{:id   1
                     :name "concept-1"
                     :data {:action-1 {:type "audio"
                                       :id   "/raw/audio/audio-1.mp3"}}}]
          used-concept-actions nil]
      (let [actual-result (get-audios scene-data concepts used-concept-actions)
            expected-result [{:alias "alias-1"
                              :key   "audio-1-key"
                              :url   "/raw/audio/audio-1.mp3"}]]
        (is (= actual-result expected-result))))))
