(ns webchange.editor-v2.diagram.graph-builder.utils.root-nodes-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.utils.root-nodes :refer [get-root-nodes]]))

(deftest test-get-root-nodes
  (testing "getting root nodes"
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
                       :empty-small   {:type        "empty"
                                       :data        {:type     "empty"
                                                     :duration 500}
                                       :connections {:click-on-box2 {:handlers {}}}}
                       :pick-wrong    {:type        "sequence"
                                       :data        {:type        "sequence"
                                                     :data        ["audio-wrong"]
                                                     :phrase      :wrong-click
                                                     :phrase-text "Try again"}
                                       :connections {:click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                       :audio-wrong   {:type        "audio"
                                       :data        {:type     "audio"
                                                     :id       "fw-try-again"
                                                     :start    0.892
                                                     :duration 1.869
                                                     :offset   0.2}
                                       :connections {:pick-wrong {:handlers {}
                                                                  :parent   :pick-wrong}}}
                       :click-on-box2 {:type        "test-var-scalar"
                                       :data        {:type     "test-var-scalar"
                                                     :var-name "current-box"
                                                     :value    "box2"
                                                     :success  "empty-small"
                                                     :fail     "pick-wrong"}
                                       :connections {:box2 {:handlers {:success [:empty-small]
                                                                       :fail    [:pick-wrong]}}}}}]
      (let [actual-result (get-root-nodes parsed-data)
            expected-result [:box1 :box2]]
        (is (= actual-result expected-result))))))
