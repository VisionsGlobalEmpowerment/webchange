(ns webchange.editor-v2.graph-builder.utils.node-siblings-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.node-siblings :refer [get-node-ins
                                                                   get-node-outs]]))

(deftest test-get-node-ins
  (let [graph {:x {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :y}}}
               :a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :y}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :y}}}
               :y {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :e}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :c}}}}
        node-name :y]
    (let [actual-result (get-node-ins graph node-name)
          expected-result {:a [:e]
                           :b [:c]
                           :x []}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-node-outs
  (let [node-data {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}]
    (let [actual-result (get-node-outs node-data)
          expected-result {:e [:a]
                           :c [:b]}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
