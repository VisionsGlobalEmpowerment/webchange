(ns webchange.editor-v2.diagram.graph-builder.scene-parser.concepts-replacer.replacer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.concepts-replacer.replacer :refer [get-referenced-concept-action-name
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
  (testing ""
    (let [action-data {:type     "action"
                       :from-var [{:var-name "some-var" :var-property "d"}]}
          concept-scheme [{:name     "d"
                           :type     "action"
                           :template {:type "animation-sequence"}}]]
      (let [actual-result (concept-action-ref? action-data concept-scheme)
            expected-result true]
        (is (= actual-result expected-result)))))
  (testing ""
    (let [action-data {:type     "action"
                       :from-var [{:var-name "some-var" :var-property "d"}]}
          concept-scheme [{:name     "q"
                           :type     "action"
                           :template {:type "animation-sequence"}}]]
      (let [actual-result (concept-action-ref? action-data concept-scheme)
            expected-result false]
        (is (= actual-result expected-result)))))
  (testing ""
    (let [action-data {:type "empty"}
          concept-scheme [{:name     "q"
                           :type     "action"
                           :template {:type "animation-sequence"}}]]
      (let [actual-result (concept-action-ref? action-data concept-scheme)
            expected-result false]
        (is (= actual-result expected-result))))))

(deftest test-get-concept-action
  (testing "getting concept action from scheme"
    (let [action-name :d
          prev-action :a
          next-actions :c
          concept-scheme [{:name     "d"
                           :type     "action"
                           :template {:type "animation-sequence"}}]
          current-concept nil]
      (let [actual-result (get-concept-action action-name prev-action next-actions concept-scheme current-concept)
            expected-result {:d {:type        "animation-sequence"
                                 :data        {:type "animation-sequence"}
                                 :connections {:a {:handlers {:next [:c]}}}}}]
        (is (= actual-result expected-result)))))

  (testing "getting concept action from current-concept"
    (let [action-name :d
          prev-action :a
          next-actions :c
          concept-scheme [{:name     "d"
                           :type     "action"
                           :template {:type "animation-sequence"}}]
          current-concept {:data {:d {:type   "animation-sequence"
                                      :target "target-1"
                                      :track  1}}}]
      (let [actual-result (get-concept-action action-name prev-action next-actions concept-scheme current-concept)
            expected-result {:d {:type        "animation-sequence"
                                 :data        {:type   "animation-sequence"
                                               :target "target-1"
                                               :track  1}
                                 :connections {:a {:handlers {:next [:c]}}}}}]
        (is (= actual-result expected-result)))))
  )

(deftest test-override-concept-actions
  (testing "concept action replacing with scheme template"
    (let [graph {:a {:type        "empty"
                     :connections {:root {:handlers {:next [:x]}}}
                     :data        {:type "empty"}}
                 :x {:type        "action"
                     :connections {:a {:handlers {:next [:c]}}}
                     :data        {:type     "action"
                                   :from-var [{:var-property "d"}]}}
                 :c {:type        "empty"
                     :connections {:x {:handlers {}}}
                     :data        {:type "empty"}}}
          concept-scheme [{:name     "d"
                           :type     "action"
                           :template {:type "animation-sequence"}}]
          current-concept nil]
      (let [actual-result (override-concept-actions graph {:current-concept current-concept
                                                           :concept-scheme  concept-scheme})
            expected-result {:a {:type        "empty"
                                 :connections {:root {:handlers {:next [:d]}}}
                                 :data        {:type "empty"}}
                             :d {:type        "animation-sequence"
                                 :connections {:a {:handlers {:next [:c]}}}
                                 :data        {:type           "animation-sequence"
                                               :concept-action true}}
                             :c {:type        "empty"
                                 :connections {:d {:handlers {}}}
                                 :data        {:type "empty"}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))

  (testing "concept action replacing with current-concept"
    (let [graph {:a {:type        "empty"
                     :connections {:root {:handlers {:next [:x]}}}
                     :data        {:type "empty"}}
                 :x {:type        "action"
                     :connections {:a {:handlers {:next [:c]}}}
                     :data        {:type     "action"
                                   :from-var [{:var-property "d"}]}}
                 :c {:type        "empty"
                     :connections {:x {:handlers {}}}
                     :data        {:type "empty"}}}
          concept-scheme [{:name     "d"
                           :type     "action"
                           :template {:type "animation-sequence"}}]
          current-concept {:data {:d {:type   "animation-sequence"
                                      :target "target-1"
                                      :track  1}}}]
      (let [actual-result (override-concept-actions graph {:current-concept current-concept
                                                           :concept-scheme  concept-scheme})
            expected-result {:a {:type        "empty"
                                 :connections {:root {:handlers {:next [:d]}}}
                                 :data        {:type "empty"}}
                             :d {:type        "animation-sequence"
                                 :connections {:a {:handlers {:next [:c]}}}
                                 :data        {:type           "animation-sequence"
                                               :target         "target-1"
                                               :track          1
                                               :concept-action true}}
                             :c {:type        "empty"
                                 :connections {:d {:handlers {}}}
                                 :data        {:type "empty"}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))

  (testing "concept action replacing with composite current-concept"
    (let [graph {:a {:type        "empty"
                     :connections {:root {:handlers {:next [:x]}}}
                     :data        {:type "empty"}}
                 :x {:type        "action"
                     :connections {:a {:handlers {:next [:c]}}}
                     :data        {:type     "action"
                                   :from-var [{:var-property "d"}]}}
                 :c {:type        "empty"
                     :connections {:x {:handlers {}}}
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
            expected-result {:a   {:type        "empty"
                                   :connections {:root {:handlers {:next [:d]}}}
                                   :data        {:type "empty"}}
                             :d   {:type        "parallel"
                                   :connections {:a {:handlers {:next [:d-0 :d-1]}}}
                                   :data        {:type           "parallel"
                                                 :data           [{:type   "animation-sequence"
                                                                   :target "target-1"}
                                                                  {:type   "animation-sequence"
                                                                   :target "target-2"}]
                                                 :concept-action true}}
                             :d-0 {:type        "animation-sequence"
                                   :connections {:d {:handlers {:next [:c]}
                                                     :parent   :d}}
                                   :data        {:type           "animation-sequence"
                                                 :target         "target-1"
                                                 :concept-action true}}
                             :d-1 {:type        "animation-sequence"
                                   :connections {:d {:handlers {:next [:c]}
                                                     :parent   :d}}
                                   :data        {:type           "animation-sequence"
                                                 :target         "target-2"
                                                 :concept-action true}}
                             :c   {:type        "empty"
                                   :connections {:d-0 {:handlers {}}
                                                 :d-1 {:handlers {}}}
                                   :data        {:type "empty"}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
