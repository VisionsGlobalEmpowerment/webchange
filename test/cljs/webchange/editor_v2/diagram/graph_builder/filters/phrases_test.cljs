(ns webchange.editor-v2.diagram.graph-builder.filters.phrases-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.filters.phrases :refer [count-subtree-phrases
                                                                       get-phrases-graph]]))

(deftest test-count-subtree-phrases
  (testing "count-subtree-phrases"
    (let [graph {:root {:connections {:root {:handlers {:next [:01 :02]}}}}
                 :01   {:connections {:root {:handlers {:next [:03]}}}}
                 :02   {:connections {:root {:handlers {:next [:04]}}}}
                 :03   {:connections {:01 {:handlers {:true  [:05]
                                                      :false [:07]}}}}
                 :04   {:connections {:02 {:handlers {:true  [:06]
                                                      :false [:07]}}}}
                 :05   {:connections {:03 {:handlers {:next [:08]}}}}
                 :06   {:connections {:04 {:handlers {:next [:08]}}}}
                 :07   {:connections {:03 {:handlers {:next [:09]}}
                                      :04 {:handlers {:next [:09]}}}
                        :data        {:phrase true}}
                 :08   {:connections {:05 {:handlers {:next [:10]}}
                                      :06 {:handlers {:next [:10]}}}}
                 :09   {:connections {:07 {:handlers {}}}}
                 :10   {:connections {:08 {:handlers {:next [:11]}}}
                        :data        {:phrase true}}
                 :11   {:connections {:10 {:handlers {:next [:12]}}}}
                 :12   {:connections {:11 {:handlers {:next [:13]}}}
                        :data        {:phrase true}}
                 :13   {:connections {:12 {:handlers {}}}}}]
      (let [actual-result (count-subtree-phrases graph)
            expected-result {:root [6 3 3]
                             :01   [3 3]
                             :02   [3 3]
                             :03   [3 2 1]
                             :04   [3 2 1]
                             :05   [2 2]
                             :06   [2 2]
                             :07   [1 0]
                             :08   [2 2]
                             :09   [0]
                             :10   [2 1]
                             :11   [1 1]
                             :12   [1 0]
                             :13   [0]}]
        (is (= actual-result expected-result))))))

(deftest test-get-phrases-graph
  (testing "get-phrases-graph"
    (let [graph {:01 {:connections {:root {:handlers {:next [:03]}}}}
                 :02 {:connections {:root {:handlers {:next [:04]}}}}
                 :03 {:connections {:01 {:handlers {:true  [:05]
                                                    :false [:07]}}}}
                 :04 {:connections {:02 {:handlers {:true  [:06]
                                                    :false [:07]}}}}
                 :05 {:connections {:03 {:handlers {:next [:08]}}}}
                 :06 {:connections {:04 {:handlers {:next [:08]}}}}
                 :07 {:connections {:03 {:handlers {:next [:09]}}
                                    :04 {:handlers {:next [:09]}}}
                      :data        {:phrase true}}
                 :08 {:connections {:05 {:handlers {:next [:10]}}
                                    :06 {:handlers {:next [:10]}}}}
                 :09 {:connections {:07 {:handlers {}}}}
                 :10 {:connections {:08 {:handlers {:next [:11]}}}
                      :data        {:phrase true}}
                 :11 {:connections {:10 {:handlers {:next [:12]}}}}
                 :12 {:connections {:11 {:handlers {:next [:13]}}}
                      :data        {:phrase true}}
                 :13 {:connections {:12 {:handlers {}}}}}]
      (let [actual-result (get-phrases-graph graph)
            expected-result {:01 {:connections {:root {:handlers {:next [:03]}}}}
                             :02 {:connections {:root {:handlers {:next [:04]}}}}
                             :03 {:connections {:01 {:handlers {:true  [:10]
                                                                :false [:07]}}}}
                             :04 {:connections {:02 {:handlers {:true  [:10]
                                                                :false [:07]}}}}
                             :07 {:connections {:03 {:handlers {}}
                                                :04 {:handlers {}}}
                                  :data        {:phrase true}}
                             :10 {:connections {:03 {:handlers {:next [:12]}}
                                                :04 {:handlers {:next [:12]}}}
                                  :data        {:phrase true}}
                             :12 {:connections {:10 {:handlers {}}}
                                  :data        {:phrase true}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "no-phrases graph"
    (let [graph {:01 {:connections {:root {:handlers {:next [:03]}}}}
                 :02 {:connections {:root {:handlers {:next [:04]}}}}
                 :03 {:connections {:01 {:handlers {:true  [:05]
                                                    :false [:07]}}}}
                 :04 {:connections {:02 {:handlers {:true  [:06]
                                                    :false [:07]}}}}
                 :05 {:connections {:03 {:handlers {:next [:08]}}}}
                 :06 {:connections {:04 {:handlers {:next [:08]}}}}
                 :07 {:connections {:03 {:handlers {:next [:09]}}
                                    :04 {:handlers {:next [:09]}}}}
                 :08 {:connections {:05 {:handlers {:next [:10]}}
                                    :06 {:handlers {:next [:10]}}}}
                 :09 {:connections {:07 {:handlers {}}}}
                 :10 {:connections {:08 {:handlers {:next [:11]}}}}
                 :11 {:connections {:10 {:handlers {:next [:12]}}}}
                 :12 {:connections {:11 {:handlers {:next [:13]}}}}
                 :13 {:connections {:12 {:handlers {}}}}}]
      (let [actual-result (get-phrases-graph graph)
            expected-result {}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
