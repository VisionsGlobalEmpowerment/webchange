(ns webchange.editor-v2.diagram.diagram-model.items-factory.utils-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.diagram-model.items-factory.utils :refer [get-node-connections
                                                                           get-node-outs]]))

(deftest test-get-node-connections
  (testing "getting node connections"
    (let [node-name :trigger
          node-data {:connections #{{:previous :root
                                     :name     "start"
                                     :handler  :a}
                                    {:previous :root
                                     :name     "back"
                                     :handler  :b}}}]
      (let [actual-result (get-node-connections node-name node-data)
            expected-result #{[:trigger :start :a]
                              [:trigger :back :b]}]
        (is (= actual-result expected-result))))))

(deftest test-get-node-outs
  (testing "getting node outs"
    (let [node-data {:connections #{{:previous :root
                                     :name     "start"
                                     :handler  :a}
                                    {:previous :root
                                     :name     "start"
                                     :handler  :b}
                                    {:previous :root
                                     :name     "back"
                                     :handler  :c}}}]
      (let [actual-result (get-node-outs node-data)
            expected-result #{:start :back}]
        (is (= actual-result expected-result))))))
