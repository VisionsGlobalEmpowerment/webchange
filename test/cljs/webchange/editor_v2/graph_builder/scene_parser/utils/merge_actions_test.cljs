(ns webchange.editor-v2.graph-builder.scene-parser.utils.merge-actions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.merge-actions :refer [merge-actions]]))

(deftest test-merge-actions--simple-merge
  (let [map-1 {:x {:data        {:type "empty"}
                   :path        [:x]
                   :connections #{{:previous :a
                                   :handler  :b
                                   :name     "next"
                                   :sequence :v}}}}
        map-2 {:y {:data        {:type "empty"}
                   :path        [:y]
                   :connections #{{:previous :c
                                   :handler  :d
                                   :name     "next"
                                   :sequence :w}}}}]
    (let [actual-result (merge-actions map-1 map-2)
          expected-result {:x {:data        {:type "empty"}
                               :path        [:x]
                               :connections #{{:previous :a
                                               :handler  :b
                                               :name     "next"
                                               :sequence :v}}}
                           :y {:data        {:type "empty"}
                               :path        [:y]
                               :connections #{{:previous :c
                                               :handler  :d
                                               :name     "next"
                                               :sequence :w}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-merge-actions--same-action-merge
  (let [map-1 {:x {:data        {:type "empty"}
                   :path        [:x]
                   :connections #{{:previous :a
                                   :handler  :b
                                   :name     "next"
                                   :sequence :v}}}}
        map-2 {:x {:data        {:type "empty"}
                   :path        [:x]
                   :connections #{{:previous :c
                                   :handler  :d
                                   :name     "next"
                                   :sequence :w}}}}]
    (let [actual-result (merge-actions map-1 map-2)
          expected-result {:x {:data        {:type "empty"}
                               :path        [:x]
                               :connections #{{:previous :a
                                               :handler  :b
                                               :name     "next"
                                               :sequence :v}
                                              {:previous :c
                                               :handler  :d
                                               :name     "next"
                                               :sequence :w}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
