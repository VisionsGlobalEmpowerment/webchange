(ns webchange.editor-v2.diagram.graph-builder.scene-parser.duplicates-replicator.usages-counter-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.duplicates-replicator.usages-counter :refer [count-node-usages
                                                                                           get-reused-nodes]]))

(deftest test-get-reused-nodes
  (testing "converting usages to duplicates map"
    (let [usages-map {:box1          1
                      :box2          1
                      :click-on-box1 1
                      :click-on-box2 1
                      :empty-small   2
                      :pick-wrong    2
                      :audio-wrong   2}]
      (let [actual-result (get-reused-nodes usages-map)
            expected-result {:empty-small 0
                             :pick-wrong  0
                             :audio-wrong 0}]
        (is (= actual-result expected-result))))))

(deftest test-count-node-usages
  (testing "test-var-scalar"
    (let [parsed-data {:box1          {:type        "object"
                                       :data        {:actions {:click {:type "action"
                                                                       :id   "click-on-box1"
                                                                       :on   "click"}}}
                                       :connections {:root {:handlers {:click [:click-on-box1]}}}}
                       :box2          {:type        "object"
                                       :data        {:actions {:click {:type "action"
                                                                       :id   "click-on-box2"
                                                                       :on   "click"}}}
                                       :connections {:root {:handlers {:click [:click-on-box2]}}}}
                       :click-on-box1 {:type        "test-var-scalar"
                                       :data        {:type     "test-var-scalar"
                                                     :var-name "current-box"
                                                     :value    "box1"
                                                     :success  "empty-small"
                                                     :fail     "pick-wrong"}
                                       :connections {:box1 {:handlers {:success [:empty-small]
                                                                       :fail    [:pick-wrong]}}}}
                       :click-on-box2 {:type        "test-var-scalar"
                                       :data        {:type     "test-var-scalar"
                                                     :var-name "current-box"
                                                     :value    "box2"
                                                     :success  "empty-small"
                                                     :fail     "pick-wrong"}
                                       :connections {:box2 {:handlers {:success [:empty-small]
                                                                       :fail    [:pick-wrong]}}}}
                       :empty-small   {:type        "empty"
                                       :data        {:type     "empty"
                                                     :duration 500}
                                       :connections {:click-on-box1 {:handlers {}}
                                                     :click-on-box2 {:handlers {}}}}
                       :pick-wrong    {:type        "sequence"
                                       :data        {:type        "sequence"
                                                     :data        ["audio-wrong"]
                                                     :phrase      :wrong-click
                                                     :phrase-text "Try again"}
                                       :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                     :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                       :audio-wrong   {:type        "audio"
                                       :data        {:type     "audio"
                                                     :id       "fw-try-again"
                                                     :start    0.892
                                                     :duration 1.869
                                                     :offset   0.2}
                                       :connections {:pick-wrong {:handlers {}
                                                                  :parent   :pick-wrong}}}}
          start-nodes [:box1 :box2]]
      (let [actual-result (count-node-usages parsed-data start-nodes)
            expected-result {:box1          1
                             :box2          1
                             :click-on-box1 1
                             :click-on-box2 1
                             :empty-small   2
                             :pick-wrong    2
                             :audio-wrong   2}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "parallel"
    (let [parsed-data {:a   {:type        "sequence"
                             :connections {:root {:handlers {:next [:b]}}}}
                       :b   {:type        "parallel"
                             :connections {:a {:handlers {:next [:b-1 :b-2]}}}}
                       :b-1 {:type        "empty"
                             :connections {:b {:handlers {:next [:c]}
                                               :parent   :b}}}
                       :b-2 {:type        "empty"
                             :connections {:b {:handlers {:next [:c]}
                                               :parent   :b}}}
                       :c   {:type        "empty"
                             :connections {:b-1 {:handlers {}}
                                           :b-2 {:handlers {}}}}}
          start-nodes [:a]]
      (let [actual-result (count-node-usages parsed-data start-nodes)
            expected-result {:a   1
                             :b   1
                             :b-1 1
                             :b-2 1
                             :c   1}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
