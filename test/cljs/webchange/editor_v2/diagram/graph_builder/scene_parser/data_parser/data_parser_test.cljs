(ns webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [compare-maps]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser :refer [get-chain-entries
                                                                                            parse-data]]))

(deftest test-get-chain-entries
  (testing "getting entries"
    (let [objects-data {:door        {:type        "object"
                                      :connections {:root {:handlers {}}}}
                        :senora-vaca {:type        "object"
                                      :connections {:root {:handlers {:next [:restart]}}}}
                        :box1        {:type        "object"
                                      :connections {:root {:handlers {:next [:click-on-box1
                                                                             :click-on-box2]}}}}}]
      (let [actual-result (get-chain-entries objects-data)
            expected-result [[:restart :senora-vaca]
                             [:click-on-box1 :box1]
                             [:click-on-box2 :box1]]]
        (is (= actual-result expected-result))))))

(deftest test-stage-1
  (testing "test-var-scalar"
    (let [scene-data {:objects {:box1 {:actions {:click {:type "action" :id "click-on-box1" :on "click"}}}
                                :box2 {:actions {:click {:type "action" :id "click-on-box2" :on "click"}}}}
                      :actions {:click-on-box1 {:type     "test-var-scalar"
                                                :var-name "current-box"
                                                :value    "box1"
                                                :success  "empty-small"
                                                :fail     "pick-wrong"}
                                :click-on-box2 {:type     "test-var-scalar"
                                                :var-name "current-box"
                                                :value    "box2"
                                                :success  "empty-small"
                                                :fail     "pick-wrong"}
                                :pick-wrong    {:type        "sequence"
                                                :data        ["audio-wrong"]
                                                :phrase      :wrong-click
                                                :phrase-text "Try again"}
                                :audio-wrong   {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}
                                :empty-small   {:type "empty" :duration 500}}}]
      (let [actual-result (parse-data scene-data)
            expected-result {:box1          {:type        "object"
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
                                                                        :parent   :pick-wrong}}}}]
        (when-not (= actual-result expected-result)
          (println (compare-maps actual-result expected-result)))
        (is (= actual-result expected-result))))))