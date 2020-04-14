(ns webchange.editor-v2.translator.translator-form.audio-assets.utils.test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.translator.translator-form.audio-assets.utils :refer [get-action-audio-data]]))

(deftest test-get-action-audio-data--animation-sequence
  (let [action-data {:type   "animation-sequence"
                     :audio  "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                     :target "senoravaca"
                     :data   [{:end      29.318
                               :anim     "talk"
                               :start    28.838
                               :duration 0.48}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                            :target "senoravaca"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--parallel-with-animation-sequence
  (let [action-data {:type "parallel"
                     :data [{:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}
                            {:type   "empty"}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                            :target "vera"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--parallel-with-2-animation-sequences
  (let [action-data {:type "parallel"
                     :data [{:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}
                            {:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                             :target "senoravaca"}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                            :target "vera"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                            :target "senoravaca"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--sequence-data-with-2-animation-sequences
  (let [action-data {:type "sequence-data"
                     :data [{:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}
                            {:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                             :target "senoravaca"}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                            :target "vera"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                            :target "senoravaca"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--sequence-data-with-contained-animation-sequence
  (let [action-data {:type "sequence-data"
                     :data [{:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}
                            {:type "sequence-data"
                             :data [{:type   "animation-sequence"
                                     :audio  "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                                     :target "senoravaca"}
                                    {:type   "empty"}]}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                            :target "vera"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a"
                            :target "senoravaca"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--sequence-data-with-equal-animation-sequence
  (let [action-data {:type "sequence-data"
                     :data [{:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}
                            {:type   "animation-sequence"
                             :audio  "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                             :target "vera"}]}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result [{:url    "/raw/audio/l1/a1/L1_A1_Vera_Object_1_insertions.m4a"
                            :target "vera"}]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-audio-data--others
  (let [action-data {:type   "empty"}]
    (let [actual-result (get-action-audio-data action-data)
          expected-result []]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
