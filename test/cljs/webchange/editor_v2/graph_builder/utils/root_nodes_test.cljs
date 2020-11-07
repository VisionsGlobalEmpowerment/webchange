(ns webchange.editor-v2.graph-builder.utils.root-nodes-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes]]))

(deftest test-get-root-nodes--with-connections
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}
               :e {:connections #{{:previous :b
                                   :name     "next"
                                   :handler  :d
                                   :sequence :a}}}}]
    (let [actual-result (get-root-nodes graph)
          expected-result [:a]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-root-nodes--node-alone
  (let [graph {:a {:connections #{}}}]
    (let [actual-result (get-root-nodes graph)
          expected-result [:a]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))


(deftest test-add-root-node
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "e"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :c {:data        {:type "empty"}
                   :path        [:e]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :d}}}}]
    (let [actual-result (add-root-node graph [:a :c])
          expected-result {:root {:connections #{{:previous :root
                                                  :name     "next"
                                                  :handler  :a}
                                                 {:previous :root
                                                  :name     "next"
                                                  :handler  :c}}}
                           :a    {:data        {:type "sequence"
                                                :data ["b" "e"]}
                                  :path        [:a]
                                  :connections #{{:previous :root
                                                  :name     "next"
                                                  :handler  :b}}}
                           :c    {:data        {:type "empty"}
                                  :path        [:e]
                                  :connections #{{:previous :root
                                                  :name     "next"
                                                  :handler  :d}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

