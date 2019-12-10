(ns webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data :refer [get-parallel-action-children
                                                                                  get-sequence-action-last-child]]))

(deftest test-get-action-data--get-parallel-action-children
  (let [graph {:b {:data        {:type "parallel"
                                 :data [{:type "empty"}
                                        {:type "empty"}]}
                   :path        [:b]
                   :connections #{{:previous :a
                                   :name     "next"
                                   :handler  :b-0
                                   :sequence :b}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :b-1
                                   :sequence :b}}}}
        action-name :b]
    (let [actual-result (get-parallel-action-children graph action-name)
          expected-result [:b-0 :b-1]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-data--sequence-action-last-child
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "c" "d"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}}
        action-name :a]
    (let [actual-result (get-sequence-action-last-child graph action-name)
          expected-result :d]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
