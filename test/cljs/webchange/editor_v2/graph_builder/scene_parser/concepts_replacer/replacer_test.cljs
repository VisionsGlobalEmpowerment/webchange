(ns webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer :refer [get-referenced-concept-action-name
                                                                                       concept-action-ref?
                                                                                       get-concept-action
                                                                                       override-concept-actions]]))

(deftest test-get-referenced-concept-action-name
  (testing "parsing concept action ref"
    (let [action-data {:type     "action"
                       :from-var [{:var-name "some-var" :var-property "d"}]}]
      (let [actual-result (get-referenced-concept-action-name action-data)
            expected-result :d]
        (is (= actual-result expected-result)))))
  (testing "parsing not concept action ref"
    (let [action-data {:type "action"
                       :id   "a"}]
      (let [actual-result (get-referenced-concept-action-name action-data)
            expected-result nil]
        (is (= actual-result expected-result)))))
  (testing "parsing action without ref"
    (let [action-data {:type "empty"}]
      (let [actual-result (get-referenced-concept-action-name action-data)
            expected-result nil]
        (is (= actual-result expected-result))))))

(deftest test-concept-action-ref?
  (let [action-data {:type     "action"
                     :from-var [{:var-name "some-var" :var-property "d"}]}
        concept {:data {:d {:type   "animation-sequence"}}}]
    (let [actual-result (concept-action-ref? action-data concept)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-get-concept-action--from-current-concept
  (let [action-name :d
        current-concept {:data {:d {:type   "animation-sequence"
                                    :target "target-1"
                                    :track  1}}}
        copy-data {}]
    (let [actual-result (get-concept-action action-name current-concept copy-data)
          expected-result {:d {:data        {:type   "animation-sequence"
                                             :target "target-1"
                                             :track  1}
                               :path        [:d]
                               :connections #{{:previous :prev-action
                                               :name     "next"
                                               :handler  :next-action}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-concept-action--from-current-concept-and-apply-copy-name
  (let [action-name :d
        current-concept {:data {:d {:type   "animation-sequence"
                                    :target "target-1"
                                    :track  1}}}
        copy-data {:copy-counter 2}]
    (let [actual-result (get-concept-action action-name current-concept copy-data)
          expected-result {:d-copy-2 {:data        {:type   "animation-sequence"
                                                    :target "target-1"
                                                    :track  1
                                                    :origin-name :d}
                                      :path        [:d]
                                      :connections #{{:previous :prev-action
                                                      :name     "next"
                                                      :handler  :next-action}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-override-concept-actions--with-current-concept
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}
                   :data        {:type "empty"}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}
                   :data        {:type     "action"
                                 :from-var [{:var-property "d"}]}}
               :c {:connections #{}
                   :data        {:type "empty"}}}
        current-concept {:data {:d {:type   "animation-sequence"
                                    :target "target-1"
                                    :track  1}}}]
    (let [actual-result (override-concept-actions graph {:current-concept current-concept})
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :x}}
                               :data        {:type "empty"}}
                           :x {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :d}}
                               :data        {:type     "action"
                                             :from-var [{:var-property "d"}]}}
                           :d {:data        {:type           "animation-sequence"
                                             :target         "target-1"
                                             :track          1
                                             :concept-action true}
                               :path        [:d]
                               :connections #{{:previous :x
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{}
                               :data        {:type "empty"}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-override-concept-actions--with-composite-current-concept
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}
                   :data        {:type "empty"}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}
                   :data        {:type     "action"
                                 :from-var [{:var-property "d"}]}}
               :c {:connections #{}
                   :data        {:type "empty"}}}
        concept-scheme [{:name     "d"
                         :type     "action"
                         :template {:type "animation-sequence"}}]
        current-concept {:data {:d {:type "parallel",
                                    :data [{:type   "animation-sequence"
                                            :target "target-1"}
                                           {:type   "animation-sequence"
                                            :target "target-2"}]}}}]
    (let [actual-result (override-concept-actions graph {:current-concept current-concept
                                                         :concept-scheme  concept-scheme})
          expected-result {:a   {:connections #{{:previous :root
                                                 :name     "next"
                                                 :handler  :x}}
                                 :data        {:type "empty"}}
                           :x {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :d}}
                               :data        {:type     "action"
                                             :from-var [{:var-property "d"}]}}
                           :d   {:data        {:type           "parallel"
                                               :data           [{:type   "animation-sequence"
                                                                 :target "target-1"} {:type   "animation-sequence"
                                                                                      :target "target-2"}]
                                               :concept-action true}
                                 :path        [:d]
                                 :connections #{{:previous :x
                                                 :name     "next"
                                                 :handler  :d-0
                                                 :sequence :d}
                                                {:previous :x
                                                 :name     "next"
                                                 :handler  :d-1
                                                 :sequence :d}}}
                           :d-0 {:data        {:type           "animation-sequence"
                                               :target         "target-1"
                                               :concept-action true}
                                 :path        [:d 0]
                                 :connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :c}}}
                           :d-1 {:data        {:type           "animation-sequence"
                                               :target         "target-2"
                                               :concept-action true}
                                 :path        [:d 1]
                                 :connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :c}}}
                           :c   {:connections #{}
                                 :data        {:type "empty"}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
