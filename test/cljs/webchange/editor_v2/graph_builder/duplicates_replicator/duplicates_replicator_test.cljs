(ns webchange.editor-v2.graph-builder.duplicates-replicator.duplicates-replicator-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.duplicates-replicator.duplicates-replicator :refer [
                                                                                           get-copy-name
                                                                                           change-connection-name
                                                                                           change-handlers-name
                                                                                           filter-node-connections
                                                                                           rename-node-connection
                                                                                           change-node-connection-parent
                                                                                           remove-reused-nodes
                                                                                           replicate-reused-nodes
                                                                                           ]]))

(deftest test-get-copy-name
  (testing "getting copy name"
    (let [origin-name "action"
          number 1]
      (let [actual-result (get-copy-name origin-name number)
            expected-result "action-copy-1"]
        (is (= actual-result expected-result))))))

(deftest test-change-connection-name
  (testing "change-connection-name"
    (let [graph {:d {:type        "empty"
                     :connections #{{:previous :a
                                     :handler  :e
                                     :name     "next"}
                                    {:previous :c
                                     :handler  :e
                                     :name     "next"}}}}
          node-name :d
          prev-parent-name :a
          new-parent-name :a-copy-a]
      (let [actual-result (change-connection-name graph node-name prev-parent-name new-parent-name)
            expected-result {:d {:type        "empty"
                                 :connections #{{:previous :a-copy-a
                                                 :handler  :e
                                                 :name     "next"}
                                                {:previous :c
                                                 :handler  :e
                                                 :name     "next"}}}}]
        (is (= actual-result expected-result))))))

(deftest test-change-handlers-name
  (testing "change-handlers-name"
    (let [graph {:d {:type        "empty"
                     :connections #{{:previous :a
                                     :handler  :e
                                     :name     "next"}
                                    {:previous :c
                                     :handler  :e
                                     :name     "next"}}}}
          node-name :d
          prev-child-name :e
          new-child-name :e-copy-a]
      (let [actual-result (change-handlers-name graph node-name prev-child-name new-child-name)
            expected-result {:d {:type        "empty"
                                 :connections #{{:previous :a
                                                 :handler  :e-copy-a
                                                 :name     "next"}
                                                {:previous :c
                                                 :handler  :e-copy-a
                                                 :name     "next"}}}}]
        (is (= actual-result expected-result))))))

(deftest test-filter-node-connections
  (testing "filter-node-connections"
    (let [node-data {:type        "empty"
                     :connections #{{:previous :a
                                     :handler  :e
                                     :name     "next"}
                                    {:previous :c
                                     :handler  :e
                                     :name     "next"}}}
          connection-name :a]
      (let [actual-result (filter-node-connections node-data connection-name)
            expected-result {:type        "empty"
                             :connections #{{:previous :a
                                             :handler  :e
                                             :name     "next"}}}]
        (is (= actual-result expected-result))))))

(deftest test-rename-node-connection
  (testing "rename-node-connection"
    (let [node-data {:type        "empty"
                     :connections #{{:previous :a
                                     :handler  :e
                                     :name     "next"}
                                    {:previous :c
                                     :handler  :e
                                     :name     "next"}}}
          connection-name :a
          new-connection-name :a-copy-1]
      (let [actual-result (rename-node-connection node-data connection-name new-connection-name)
            expected-result {:type        "empty"
                             :connections #{{:previous :a-copy-1
                                             :handler  :e
                                             :name     "next"}
                                            {:previous :c
                                             :handler  :e
                                             :name     "next"}}}]
        (is (= actual-result expected-result))))))

(deftest test-change-node-connection-parent
  (testing "change-node-connection-parent"
    (let [node-data {:type        "empty"
                     :connections #{{:previous :c
                                     :handler  :e
                                     :name     "next"
                                     :sequence :x}}}
          connection-name :c
          new-parent-name :x-copy-1]
      (let [actual-result (change-node-connection-parent node-data connection-name new-parent-name)
            expected-result {:type        "empty"
                             :connections #{{:previous :c
                                             :handler  :e
                                             :name     "next"
                                             :sequence :x-copy-1}}}]
        (is (= actual-result expected-result))))))

(deftest test-remove-reused-nodes
  (let [graph {:box1               {:connections #{{:previous :root
                                                    :handler  :click-on-box1
                                                    :name     "click"}}}
               :box2               {:connections #{{:previous :root
                                                    :handler  :click-on-box2
                                                    :name     "click"}}}
               :click-on-box1      {:connections #{{:previous :box1
                                                    :handler  :empty-small
                                                    :name     "success"}
                                                   {:previous :box1
                                                    :handler  :pick-wrong
                                                    :name     "fail"}}}
               :click-on-box2      {:connections #{{:previous :box2
                                                    :handler  :empty-small
                                                    :name     "success"}
                                                   {:previous :box2
                                                    :handler  :pick-wrong
                                                    :name     "fail"}}}
               :empty-small        {:connections #{}}
               :empty-small-copy-1 {:connections #{}}
               :empty-small-copy-2 {:connections #{}}
               :pick-wrong         {:connections #{{:previous :click-on-box1
                                                    :handler  :audio-wrong
                                                    :name     "next"}
                                                   {:previous :click-on-box2
                                                    :handler  :audio-wrong
                                                    :name     "next"}}}

               :pick-wrong-copy-1  {:connections #{{:previous :click-on-box1
                                                    :handler  :audio-wrong
                                                    :name     "next"}
                                                   {:previous :click-on-box2
                                                    :handler  :audio-wrong
                                                    :name     "next"}}}
               :pick-wrong-copy-2  {:connections #{{:previous :click-on-box1
                                                    :handler  :audio-wrong
                                                    :name     "next"}
                                                   {:previous :click-on-box2
                                                    :handler  :audio-wrong
                                                    :name     "next"}}}
               :audio-wrong        {:connections #{}}
               :audio-wrong-copy-1 {:connections #{}}
               :audio-wrong-copy-2 {:connections #{}}}
        reused-nodes {:empty-small 0
                      :pick-wrong  0
                      :audio-wrong 0}]
    (let [actual-result (remove-reused-nodes graph reused-nodes)
          expected-result {:box1               {:connections #{{:previous :root
                                                                :handler  :click-on-box1
                                                                :name     "click"}}}
                           :box2               {:connections #{{:previous :root
                                                                :handler  :click-on-box2
                                                                :name     "click"}}}
                           :click-on-box1      {:connections #{{:previous :box1
                                                                :handler  :empty-small
                                                                :name     "success"}
                                                               {:previous :box1
                                                                :handler  :pick-wrong
                                                                :name     "fail"}}}
                           :click-on-box2      {:connections #{{:previous :box2
                                                                :handler  :empty-small
                                                                :name     "success"}
                                                               {:previous :box2
                                                                :handler  :pick-wrong
                                                                :name     "fail"}}}
                           :empty-small-copy-1 {:connections #{}}
                           :empty-small-copy-2 {:connections #{}}
                           :pick-wrong-copy-1  {:connections #{{:previous :click-on-box1
                                                                :handler  :audio-wrong
                                                                :name     "next"}
                                                               {:previous :click-on-box2
                                                                :handler  :audio-wrong
                                                                :name     "next"}}}
                           :pick-wrong-copy-2  {:connections #{{:previous :click-on-box1
                                                                :handler  :audio-wrong
                                                                :name     "next"}
                                                               {:previous :click-on-box2
                                                                :handler  :audio-wrong
                                                                :name     "next"}}}
                           :audio-wrong-copy-1 {:connections #{}}
                           :audio-wrong-copy-2 {:connections #{}}}]
      (is (= actual-result expected-result)))))

(deftest test-replicate-reused-nodes--test-var-scalar
  (let [parsed-data {:box1          {:connections #{{:previous :root
                                                     :handler  :click-on-box1
                                                     :name     "click"}}}
                     :box2          {:connections #{{:previous :root
                                                     :handler  :click-on-box2
                                                     :name     "click"}}}
                     :click-on-box1 {:connections #{{:previous :box1
                                                     :handler  :empty-small
                                                     :name     "success"}
                                                    {:previous :box1
                                                     :handler  :pick-wrong
                                                     :name     "fail"}}}
                     :click-on-box2 {:connections #{{:previous :box2
                                                     :handler  :empty-small
                                                     :name     "success"}
                                                    {:previous :box2
                                                     :handler  :pick-wrong
                                                     :name     "fail"}}}
                     :empty-small   {:connections #{}}
                     :pick-wrong    {:connections #{{:previous :click-on-box1
                                                     :handler  :audio-wrong
                                                     :name     "next"}
                                                    {:previous :click-on-box2
                                                     :handler  :audio-wrong
                                                     :name     "next"}}}
                     :audio-wrong   {:connections #{}}}
        start-nodes [:box1 :box2]
        reused-nodes {:empty-small 0
                      :pick-wrong  0
                      :audio-wrong 0}]
    (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
          expected-result {:box1               {:connections #{{:previous :root
                                                                :handler  :click-on-box1
                                                                :name     "click"}}}
                           :box2               {:connections #{{:previous :root
                                                                :handler  :click-on-box2
                                                                :name     "click"}}}
                           :click-on-box1      {:connections #{{:previous :box1
                                                                :handler  :empty-small-copy-1
                                                                :name     "success"}
                                                               {:previous :box1
                                                                :handler  :pick-wrong-copy-1
                                                                :name     "fail"}}}
                           :click-on-box2      {:connections #{{:previous :box2
                                                                :handler  :empty-small-copy-2
                                                                :name     "success"}
                                                               {:previous :box2
                                                                :handler  :pick-wrong-copy-2
                                                                :name     "fail"}}}
                           :empty-small-copy-1 {:origin       :empty-small
                                                :copy-counter 1
                                                :connections  #{}}
                           :empty-small-copy-2 {:origin       :empty-small
                                                :copy-counter 2
                                                :connections  #{}}
                           :pick-wrong-copy-1  {:origin       :pick-wrong
                                                :copy-counter 1
                                                :connections  #{{:previous :click-on-box1
                                                                 :handler  :audio-wrong-copy-1
                                                                 :name     "next"}}}
                           :pick-wrong-copy-2  {:origin       :pick-wrong
                                                :copy-counter 2
                                                :connections  #{{:previous :click-on-box2
                                                                 :handler  :audio-wrong-copy-2
                                                                 :name     "next"}}}
                           :audio-wrong-copy-1 {:origin       :audio-wrong
                                                :copy-counter 1
                                                :connections  #{}}
                           :audio-wrong-copy-2 {:origin       :audio-wrong
                                                :copy-counter 2
                                                :connections  #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-replicate-reused-nodes--sequence-1
  (let [parsed-data {:box1          {:connections #{{:previous :root
                                                     :handler  :click-on-box1
                                                     :name     "click"}}}
                     :box2          {:connections #{{:previous :root
                                                     :handler  :click-on-box2
                                                     :name     "click"}}}
                     :click-on-box1 {:connections #{{:previous :box1
                                                     :handler  :empty-small
                                                     :name     "success"}
                                                    {:previous :box1
                                                     :handler  :pick-wrong
                                                     :name     "fail"}}}
                     :click-on-box2 {:connections #{{:previous :box2
                                                     :handler  :empty-small
                                                     :name     "success"}
                                                    {:previous :box2
                                                     :handler  :pick-wrong
                                                     :name     "fail"}}}
                     :pick-wrong    {:connections #{{:previous :click-on-box1
                                                     :handler  :audio-wrong
                                                     :name     "next"}
                                                    {:previous :click-on-box2
                                                     :handler  :audio-wrong
                                                     :name     "next"}}}
                     :audio-wrong   {:connections #{{:previous :pick-wrong
                                                     :handler  :empty-small
                                                     :name     "next"
                                                     :sequence :pick-wrong}}}
                     :empty-small   {:connections #{}}}
        start-nodes [:box1 :box2]
        reused-nodes {:empty-small 0
                      :pick-wrong  0
                      :audio-wrong 0}]
    (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
          expected-result {:box1               {:connections #{{:previous :root
                                                                :handler  :click-on-box1
                                                                :name     "click"}}}
                           :box2               {:connections #{{:previous :root
                                                                :handler  :click-on-box2
                                                                :name     "click"}}}
                           :click-on-box1      {:connections #{{:previous :box1
                                                                :handler  :empty-small-copy-1
                                                                :name     "success"}
                                                               {:previous :box1
                                                                :handler  :pick-wrong-copy-1
                                                                :name     "fail"}}}
                           :click-on-box2      {:connections #{{:previous :box2
                                                                :handler  :empty-small-copy-3
                                                                :name     "success"}
                                                               {:previous :box2
                                                                :handler  :pick-wrong-copy-2
                                                                :name     "fail"}}}
                           :empty-small-copy-1 {:origin       :empty-small
                                                :copy-counter 1
                                                :connections  #{}}
                           :empty-small-copy-3 {:origin       :empty-small
                                                :copy-counter 3
                                                :connections  #{}}
                           :pick-wrong-copy-1  {:origin       :pick-wrong
                                                :copy-counter 1
                                                :connections  #{{:previous :click-on-box1
                                                                 :handler  :audio-wrong-copy-1
                                                                 :name     "next"}}}
                           :pick-wrong-copy-2  {:origin       :pick-wrong
                                                :copy-counter 2
                                                :connections  #{{:previous :click-on-box2
                                                                 :handler  :audio-wrong-copy-2
                                                                 :name     "next"}}}
                           :audio-wrong-copy-1 {:origin       :audio-wrong
                                                :copy-counter 1
                                                :connections  #{{:previous :pick-wrong-copy-1
                                                                 :handler  :empty-small-copy-2
                                                                 :name     "next"
                                                                 :sequence :pick-wrong}}}
                           :audio-wrong-copy-2 {:origin       :audio-wrong
                                                :copy-counter 2
                                                :connections  #{{:previous :pick-wrong-copy-2
                                                                 :handler  :empty-small-copy-4
                                                                 :name     "next"
                                                                 :sequence :pick-wrong}}}
                           :empty-small-copy-2 {:origin       :empty-small
                                                :copy-counter 2
                                                :connections  #{}}
                           :empty-small-copy-4 {:origin       :empty-small
                                                :copy-counter 4
                                                :connections  #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-replicate-reused-nodes--sequence-2
  (let [parsed-data {:a {:connections #{{:previous :root
                                         :handler  :c
                                         :name     "next"}
                                        {:previous :root
                                         :handler  :d
                                         :name     "next"}}}
                     :b {:connections #{{:previous :root
                                         :handler  :c
                                         :name     "next"}
                                        {:previous :root
                                         :handler  :d
                                         :name     "next"}}}
                     :c {:connections #{}}
                     :d {:connections #{{:previous :b
                                         :handler  :e
                                         :name     "next"}
                                        {:previous :a
                                         :handler  :e
                                         :name     "next"}}}
                     :e {:connections #{{:previous :d
                                         :handler  :c
                                         :name     "next"}}}}
        start-nodes [:a :b]
        reused-nodes {:c 0
                      :d 0
                      :e 0}]
    (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
          expected-result {:a        {:connections #{{:previous :root
                                                      :handler  :c-copy-2
                                                      :name     "next"}
                                                     {:previous :root
                                                      :handler  :d-copy-1
                                                      :name     "next"}}}
                           :b        {:connections #{{:previous :root
                                                      :handler  :c-copy-4
                                                      :name     "next"}
                                                     {:previous :root
                                                      :handler  :d-copy-2
                                                      :name     "next"}}}
                           :c-copy-1 {:connections  #{}
                                      :origin       :c
                                      :copy-counter 1}
                           :c-copy-2 {:connections  #{}
                                      :origin       :c
                                      :copy-counter 2}
                           :c-copy-3 {:connections  #{}
                                      :origin       :c
                                      :copy-counter 3}
                           :c-copy-4 {:connections  #{}
                                      :origin       :c
                                      :copy-counter 4}
                           :d-copy-1 {:connections  #{{:previous :a
                                                       :handler  :e-copy-1
                                                       :name     "next"}}
                                      :origin       :d
                                      :copy-counter 1}
                           :d-copy-2 {:connections  #{{:previous :b
                                                       :handler  :e-copy-2
                                                       :name     "next"}}
                                      :origin       :d
                                      :copy-counter 2}
                           :e-copy-1 {:connections  #{{:previous :d-copy-1
                                                       :handler  :c-copy-1
                                                       :name     "next"}}
                                      :origin       :e
                                      :copy-counter 1}
                           :e-copy-2 {:connections  #{{:previous :d-copy-2
                                                       :handler  :c-copy-3
                                                       :name     "next"}}
                                      :origin       :e
                                      :copy-counter 2}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-replicate-reused-nodes--this-is-concept
  (testing "case with phrase 'this-is-concept' in home scene"
    (let [parsed-data {:a {:connections #{{:previous :root
                                           :handler  :b
                                           :name     "next"}}}
                       :b {:connections #{{:previous :a
                                           :handler  :c
                                           :name     "next"}}}
                       :c {:connections #{{:previous :e
                                           :handler  :f
                                           :name     "next"}
                                          {:previous :b
                                           :handler  :d
                                           :name     "next"}}}
                       :d {:connections #{{:previous :c
                                           :handler  :e
                                           :name     "next"}}}
                       :e {:connections #{{:previous :d
                                           :handler  :c
                                           :name     "next"}}}
                       :f {:connections #{}}}
          start-nodes [:a]
          reused-nodes {:c 0}]
      (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
            expected-result {:a        {:connections #{{:previous :root
                                                        :handler  :b
                                                        :name     "next"}}}
                             :b        {:connections #{{:previous :a
                                                        :handler  :c-copy-1
                                                        :name     "next"}}}
                             :c-copy-1 {:connections  #{{:previous :b
                                                         :handler  :d
                                                         :name     "next"}}
                                        :origin       :c
                                        :copy-counter 1}
                             :d        {:connections #{{:previous :c-copy-1
                                                        :handler  :e
                                                        :name     "next"}}}
                             :e        {:connections #{{:previous :d
                                                        :handler  :c-copy-2
                                                        :name     "next"}}}
                             :c-copy-2 {:connections  #{{:previous :e
                                                         :handler  :f
                                                         :name     "next"}}
                                        :origin       :c
                                        :copy-counter 2}
                             :f        {:connections #{}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
