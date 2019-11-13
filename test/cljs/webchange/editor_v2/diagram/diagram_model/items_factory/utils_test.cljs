(ns webchange.editor-v2.diagram.diagram-model.items-factory.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.diagram-model.items-factory.utils :refer [get-node-connections
                                                                     get-node-outs]]))

(deftest test-get-node-connections
  (testing "getting node connections"
    (let [node-name :triggers
          node-data {:type        "trigger"
                     :connections {:root {:handlers {:start [:start-background-music :intro]
                                                     :back  [:stop-activity]}}}}]
      (let [actual-result (get-node-connections node-name node-data)
            expected-result [[:triggers :start :start-background-music]
                             [:triggers :start :intro]
                             [:triggers :back :stop-activity]]]
        (is (= actual-result expected-result))))))

(deftest test-get-node-outs
  (testing "getting node outs"
    (let [node-data {:type        "trigger"
                     :connections {:root {:handlers {:start [:start-background-music :intro]
                                                     :back  [:stop-activity]}}}}]
      (let [actual-result (get-node-outs node-data)
            expected-result [:start :back]]
        (is (= actual-result expected-result)))))
  (testing "getting node outs for empty handlers list"
    (let [node-data {:type        "trigger"
                     :connections {:root {:handlers {:start [:start-background-music]
                                                     :back  []}}}}]
      (let [actual-result (get-node-outs node-data)
            expected-result [:start :back]]
        (is (= actual-result expected-result))))))
