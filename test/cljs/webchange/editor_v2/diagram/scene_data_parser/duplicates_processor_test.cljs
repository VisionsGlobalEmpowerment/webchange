(ns webchange.editor-v2.diagram.scene-data-parser.duplicates-processor-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [compare-maps]]
    [webchange.editor-v2.diagram.scene-data-parser.duplicates-processor :refer [get-fixed-next-nodes
                                                                                get-fixed-prev-node
                                                                                get-next-siblings
                                                                                get-prev-siblings
                                                                                get-node-copy
                                                                                remove-duplicates
                                                                                remove-duplicate-origins]]))

(deftest test-get-fixed-prev-node
  (testing "test duplicated node parent replacement"
    (let [nodes {:a2 {:connections {:a1 {:handlers {:next [:b]}}}
                      :data        {:key ":a2"}}}
          duplicate-name :b
          new-duplicate-name :b-copy-0]
      (let [actual-result (get-fixed-prev-node nodes duplicate-name new-duplicate-name)
            expected-result {:a2 {:connections {:a1 {:handlers {:next [:b-copy-0]}}}
                                  :data        {:key ":a2"}}}]
        (is (= actual-result expected-result))))))

(deftest test-get-fixed-next-nodes
  (testing "test duplicated node children replacement"
    (let [nodes {:a3 {:connections {:b {:handlers {:next [:a4]}}}
                      :data        {:key ":a3"}}}
          duplicate-name :b
          new-duplicate-name :b-copy-0]
      (let [actual-result (get-fixed-next-nodes nodes duplicate-name new-duplicate-name)
            expected-result {:a3 {:connections {:b        {:handlers {:next [:a4]}}
                                                :b-copy-0 {:handlers {:next [:a4]}}}
                                  :data        {:key ":a3"}}}]
        (is (= actual-result expected-result))))))

(deftest test-get-next-siblings
  (testing "test getting node's next siblings"
    (let [node-data {:connections {:a1 {:handlers {:next [:a3]}}
                                   :a2 {:handlers {:next [:a4]}}}}]
      (let [actual-result (get-next-siblings node-data)
            expected-result [:a3 :a4]]
        (is (= actual-result expected-result)))))
  (testing "test getting node's next siblings for defined previous"
    (let [node-data {:connections {:a1 {:handlers {:next [:a3]}}
                                   :a2 {:handlers {:next [:a4]}}}}]
      (let [actual-result (get-next-siblings node-data :a1)
            expected-result [:a3]]
        (is (= actual-result expected-result))))))

(deftest test-get-prev-siblings
  (testing "test getting node's previous siblings"
    (let [node-data {:connections {:a1 {:handlers {:next [:a3]}}
                                   :a2 {:handlers {:next [:a4]}}}}]
      (let [actual-result (get-prev-siblings node-data)
            expected-result [:a1 :a2]]
        (is (= actual-result expected-result))))))

(deftest test-remove-duplicate-origins
  (testing "test getting node's previous siblings"
    (let [data {:a1       {:connections {:root {:handlers {:next [:a2 :b-copy-1]}}}
                           :data        {:key ":a1"}}
                :a2       {:connections {:a1 {:handlers {:next [:b-copy-0]}}}
                           :data        {:key ":a2"}}
                :a3       {:connections {:b        {:handlers {:next [:a4]}}
                                         :b-copy-0 {:handlers {:next [:a4]}}
                                         :b-copy-1 {:handlers {:next [:a4]}}}
                           :data        {:key ":a3"}}
                :a4       {:connections {:a3 {:handlers {}}}
                           :data        {:key ":a4"}}
                :b        {:connections {:a2 {:handlers {:next [:a3]}}}
                           :origin      :b
                           :data        {:key ":b"}}
                :b-copy-0 {:connections {:a2 {:handlers {:next [:a3]}}}
                           :origin      :b
                           :data        {:key ":b"}}
                :b-copy-1 {:connections {:a1 {:handlers {:next [:a3]}}}
                           :origin      :b
                           :data        {:key ":b"}}}
          duplicates [:b]]
      (let [actual-result (remove-duplicate-origins data duplicates)
            expected-result {:a1       {:connections {:root {:handlers {:next [:a2 :b-copy-1]}}}
                                        :data        {:key ":a1"}}
                             :a2       {:connections {:a1 {:handlers {:next [:b-copy-0]}}}
                                        :data        {:key ":a2"}}
                             :a3       {:connections {:b-copy-0 {:handlers {:next [:a4]}}
                                                      :b-copy-1 {:handlers {:next [:a4]}}}
                                        :data        {:key ":a3"}}
                             :a4       {:connections {:a3 {:handlers {}}}
                                        :data        {:key ":a4"}}
                             :b-copy-0 {:connections {:a2 {:handlers {:next [:a3]}}}
                                        :origin      :b
                                        :data        {:key ":b"}}
                             :b-copy-1 {:connections {:a1 {:handlers {:next [:a3]}}}
                                        :origin      :b
                                        :data        {:key ":b"}}}]
        (is (= actual-result expected-result)))))

  (testing "test getting node's previous siblings for duplicated node"
    (let [data {:introduce-word     {:type        "sequence"
                                     :connections {:previous-action {:handlers {:next [:empty-small-copy-0]}}}
                                     :data        {}}
                :empty-small        {:type        "empty"
                                     :connections {:introduce-word     {:handlers {:next [:empty-small]}
                                                                        :parent   :introduce-word}
                                                   :empty-small        {:handlers {:next [:group-3-times-var]}
                                                                        :parent   :introduce-word}
                                                   :group-3-times-var  {:handlers {:next [:next-action]}
                                                                        :parent   :introduce-word}
                                                   :empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                                        :parent   :introduce-word}}
                                     :data        {}}
                :group-3-times-var  {:type        "action"
                                     :connections {:empty-small        {:handlers {:next [:empty-small-copy-2]}
                                                                        :parent   :introduce-word}
                                                   :empty-small-copy-1 {:handlers {:next [:empty-small-copy-2]}
                                                                        :parent   :introduce-word}}
                                     :data        {}}
                :empty-small-copy-0 {:type        "empty"
                                     :connections {:introduce-word {:handlers {:next [:empty-small-copy-1]}
                                                                    :parent   :introduce-word}}
                                     :data        {}
                                     :origin      :empty-small}
                :empty-small-copy-1 {:type        "empty"
                                     :connections {:empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                                        :parent   :introduce-word}}
                                     :data        {}
                                     :origin      :empty-small}
                :empty-small-copy-2 {:type        "empty"
                                     :connections {:group-3-times-var {:handlers {:next [:next-action]}
                                                                       :parent   :introduce-word}}
                                     :data        {}
                                     :origin      :empty-small}
                }
          duplicates [:empty-small]]
      (let [actual-result (remove-duplicate-origins data duplicates)
            expected-result {:introduce-word     {:type        "sequence"
                                                  :connections {:previous-action {:handlers {:next [:empty-small-copy-0]}}}
                                                  :data        {}}
                             :group-3-times-var  {:type        "action"
                                                  :connections {:empty-small-copy-1 {:handlers {:next [:empty-small-copy-2]}
                                                                                     :parent   :introduce-word}}
                                                  :data        {}}
                             :empty-small-copy-0 {:type        "empty"
                                                  :connections {:introduce-word {:handlers {:next [:empty-small-copy-1]}
                                                                                 :parent   :introduce-word}}
                                                  :data        {}
                                                  :origin      :empty-small}
                             :empty-small-copy-1 {:type        "empty"
                                                  :connections {:empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                                                     :parent   :introduce-word}}
                                                  :data        {}
                                                  :origin      :empty-small}
                             :empty-small-copy-2 {:type        "empty"
                                                  :connections {:group-3-times-var {:handlers {:next [:next-action]}
                                                                                    :parent   :introduce-word}}
                                                  :data        {}
                                                  :origin      :empty-small}}]
        (is (= actual-result expected-result))))))

(deftest test-get-node-copy
  (testing "get-node-copy"
    (let [node-name :empty-small
          node-data {:type        "empty"
                     :connections {:introduce-word    {:handlers {:next [:empty-small]}
                                                       :parent   :introduce-word}
                                   :empty-small       {:handlers {:next [:group-3-times-var]}
                                                       :parent   :introduce-word}
                                   :group-3-times-var {:handlers {:next [:next-action]}
                                                       :parent   :introduce-word}}
                     :data        {}}
          duplicate-counter 0
          prev-node-name :introduce-word]
      (let [actual-result (get-node-copy node-name node-data duplicate-counter prev-node-name)
            expected-result [:empty-small-copy-0
                             {:type        "empty"
                              :connections {:introduce-word {:handlers {:next [:empty-small]}
                                                             :parent   :introduce-word}}
                              :data        {}
                              :origin      :empty-small}]]
        (is (= actual-result expected-result)))))

  (testing "get-node-copy cloned"
    (let [node-name :empty-small
          node-data {:type        "empty"
                     :connections {:introduce-word     {:handlers {:next [:empty-small]}
                                                        :parent   :introduce-word}
                                   :empty-small        {:handlers {:next [:group-3-times-var]}
                                                        :parent   :introduce-word}
                                   :group-3-times-var  {:handlers {:next [:next-action]}
                                                        :parent   :introduce-word}
                                   :empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                        :parent   :introduce-word}}
                     :data        {}}
          duplicate-counter 1
          prev-node-name :empty-small]
      (let [actual-result (get-node-copy node-name node-data duplicate-counter prev-node-name)
            expected-result [:empty-small-copy-1
                             {:type        "empty"
                              :connections {:empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                                 :parent   :introduce-word}}
                              :data        {}
                              :origin      :empty-small}]]
        (is (= actual-result expected-result))))))

(deftest test-remove-duplicates
  (testing "test duplicates copying"
    (let [entries {:a1 {:connections {:root {:handlers {:next [:a2 :b]}}}
                        :data        {:key ":a1"}}}
          data {:a1 {:connections {:root {:handlers {:next [:a2 :b]}}}
                     :data        {:key ":a1"}}
                :a2 {:connections {:a1 {:handlers {:next [:b]}}}
                     :data        {:key ":a2"}}
                :a3 {:connections {:b {:handlers {:next [:a4]}}}
                     :data        {:key ":a3"}}
                :a4 {:connections {:a3 {:handlers {}}}
                     :data        {:key ":a4"}}
                :b  {:connections {:a1 {:handlers {:next [:a3]}}
                                   :a2 {:handlers {:next [:a3]}}}
                     :data        {:key ":b"}}}]
      (let [actual-result (remove-duplicates entries data)
            expected-result {:a1       {:connections {:root {:handlers {:next [:a2 :b-copy-1]}}}
                                        :data        {:key ":a1"}}
                             :a2       {:connections {:a1 {:handlers {:next [:b-copy-0]}}}
                                        :data        {:key ":a2"}}
                             :a3       {:connections {:b-copy-0 {:handlers {:next [:a4]}}
                                                      :b-copy-1 {:handlers {:next [:a4]}}}
                                        :data        {:key ":a3"}}
                             :a4       {:connections {:a3 {:handlers {}}}
                                        :data        {:key ":a4"}}
                             :b-copy-0 {:connections {:a2 {:handlers {:next [:a3]}}}
                                        :origin      :b
                                        :data        {:key ":b"}}
                             :b-copy-1 {:connections {:a1 {:handlers {:next [:a3]}}}
                                        :origin      :b
                                        :data        {:key ":b"}}}]
        (when-not (= actual-result expected-result)
          (println (compare-maps actual-result expected-result)))
        (is (= actual-result expected-result)))))

  (testing "sequence with double duplicates"
    (let [entries {:introduce-word {:type        "sequence"
                                    :connections {:root {:handlers {:next [:empty-small]}}}
                                    :data        {}}}
          data {:introduce-word    {:type        "sequence"
                                    :connections {:root {:handlers {:next [:empty-small]}}}
                                    :data        {}}
                :empty-small       {:type        "empty"
                                    :connections {:introduce-word    {:handlers {:next [:empty-small]}
                                                                      :parent   :introduce-word}
                                                  :empty-small       {:handlers {:next [:group-3-times-var]}
                                                                      :parent   :introduce-word}
                                                  :group-3-times-var {:handlers {:next [:next-action]}
                                                                      :parent   :introduce-word}}
                                    :data        {}}
                :group-3-times-var {:type        "action"
                                    :connections {:empty-small {:handlers {:next [:empty-small]}
                                                                :parent   :introduce-word}}
                                    :data        {}}}]
      (let [actual-result (remove-duplicates entries data)
            expected-result {:introduce-word     {:type        "sequence"
                                                  :connections {:root {:handlers {:next [:empty-small-copy-0]}}}
                                                  :data        {}}
                             :group-3-times-var  {:type        "action"
                                                  :connections {:empty-small-copy-1 {:handlers {:next [:empty-small-copy-2]}
                                                                                     :parent   :introduce-word}}
                                                  :data        {}}
                             :empty-small-copy-0 {:type        "empty"
                                                  :origin      :empty-small
                                                  :connections {:introduce-word {:handlers {:next [:empty-small-copy-1]}
                                                                                 :parent   :introduce-word}}
                                                  :data        {}}
                             :empty-small-copy-1 {:type        "empty"
                                                  :origin      :empty-small
                                                  :connections {:empty-small-copy-0 {:handlers {:next [:group-3-times-var]}
                                                                                     :parent   :introduce-word}}
                                                  :data        {}}
                             :empty-small-copy-2 {:type        "empty"
                                                  :origin      :empty-small
                                                  :connections {:group-3-times-var {:handlers {:next [:next-action]}
                                                                                    :parent   :introduce-word}}
                                                  :data        {}}}]
        (when-not (= actual-result expected-result)
          (println (compare-maps actual-result expected-result)))
        (is (= actual-result expected-result))))))
