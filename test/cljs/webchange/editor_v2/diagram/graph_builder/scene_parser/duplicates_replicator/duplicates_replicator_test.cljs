(ns webchange.editor-v2.diagram.graph-builder.scene-parser.duplicates-replicator.duplicates-replicator-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.duplicates-replicator.duplicates-replicator :refer [get-copy-name
                                                                                                                change-handlers-name
                                                                                                                change-connection-name
                                                                                                                filter-node-connections
                                                                                                                rename-node-connection
                                                                                                                change-node-connection-parent
                                                                                                                remove-reused-nodes
                                                                                                                replicate-reused-nodes]]))

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
                     :connections {:a {:handlers {:next [:e]}}
                                   :c {:handlers {:next [:e]}}}}}
          node-name :d
          prev-parent-name :a
          new-parent-name :a-copy-a]
      (let [actual-result (change-connection-name graph node-name prev-parent-name new-parent-name)
            expected-result {:d {:type        "empty"
                                 :connections {:a-copy-a {:handlers {:next [:e]}}
                                               :c        {:handlers {:next [:e]}}}}}]
        (is (= actual-result expected-result))))))

(deftest test-change-handlers-name
  (testing "change-handlers-name"
    (let [graph {:d {:type        "empty"
                     :connections {:a {:handlers {:next [:e]}}
                                   :c {:handlers {:next [:e]}}}}}
          node-name :d
          prev-child-name :e
          new-child-name :e-copy-a]
      (let [actual-result (change-handlers-name graph node-name prev-child-name new-child-name)
            expected-result {:d {:type        "empty"
                                 :connections {:a {:handlers {:next [:e-copy-a]}}
                                               :c {:handlers {:next [:e-copy-a]}}}}}]
        (is (= actual-result expected-result))))))

(deftest test-filter-node-connections
  (testing "filter-node-connections"
    (let [node-data {:type        "empty"
                     :connections {:a {:handlers {:next [:e]}}
                                   :c {:handlers {:next [:e]}}}}
          connection-name :a]
      (let [actual-result (filter-node-connections node-data connection-name)
            expected-result {:type        "empty"
                             :connections {:a {:handlers {:next [:e]}}}}]
        (is (= actual-result expected-result))))))

(deftest test-rename-node-connection
  (testing "rename-node-connection"
    (let [node-data {:type        "empty"
                     :connections {:a {:handlers {:next [:e]}}
                                   :c {:handlers {:next [:e]}}}}
          connection-name :a
          new-connection-name :a-copy-1]
      (let [actual-result (rename-node-connection node-data connection-name new-connection-name)
            expected-result {:type        "empty"
                             :connections {:a-copy-1 {:handlers {:next [:e]}}
                                           :c        {:handlers {:next [:e]}}}}]
        (is (= actual-result expected-result))))))

(deftest test-change-node-connection-parent
  (testing "change-node-connection-parent"
    (let [node-data {:type        "empty"
                     :connections {:c {:handlers {:next [:e]}
                                       :parent   :x}}}
          connection-name :c
          new-parent-name :x-copy-1]
      (let [actual-result (change-node-connection-parent node-data connection-name new-parent-name)
            expected-result {:type        "empty"
                             :connections {:c {:handlers {:next [:e]}
                                               :parent   :x-copy-1}}}]
        (is (= actual-result expected-result))))))

(deftest test-remove-reused-nodes
  (testing "remove-reused-nodes"
    (let [graph {:box1               {:type        "object"
                                      :data        {}
                                      :connections {:root {:handlers {:click [:click-on-box1]}}}}
                 :box2               {:type        "object"
                                      :data        {}
                                      :connections {:root {:handlers {:click [:click-on-box2]}}}}
                 :click-on-box1      {:type        "test-var-scalar"
                                      :data        {}
                                      :connections {:box1 {:handlers {:success [:empty-small]
                                                                      :fail    [:pick-wrong]}}}}
                 :click-on-box2      {:type        "test-var-scalar"
                                      :data        {}
                                      :connections {:box2 {:handlers {:success [:empty-small]
                                                                      :fail    [:pick-wrong]}}}}
                 :empty-small        {:type        "empty"
                                      :data        {}
                                      :connections {:click-on-box1 {:handlers {}}
                                                    :click-on-box2 {:handlers {}}}}
                 :empty-small-copy-1 {:type        "empty"
                                      :data        {}
                                      :connections {:click-on-box1 {:handlers {}}
                                                    :click-on-box2 {:handlers {}}}}
                 :empty-small-copy-2 {:type        "empty"
                                      :data        {:type     "empty"
                                                    :duration 500}
                                      :connections {:click-on-box1 {:handlers {}}
                                                    :click-on-box2 {:handlers {}}}}
                 :pick-wrong         {:type        "sequence"
                                      :data        {}
                                      :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                    :click-on-box2 {:handlers {:next [:audio-wrong]}}}}

                 :pick-wrong-copy-1  {:type        "sequence"
                                      :data        {}
                                      :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                    :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                 :pick-wrong-copy-2  {:type        "sequence"
                                      :data        {}
                                      :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                    :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                 :audio-wrong        {:type        "audio"
                                      :data        {}
                                      :connections {:pick-wrong {:handlers {}
                                                                 :parent   :pick-wrong}}}
                 :audio-wrong-copy-1 {:type        "audio"
                                      :data        {}
                                      :connections {:pick-wrong {:handlers {}
                                                                 :parent   :pick-wrong}}}
                 :audio-wrong-copy-2 {:type        "audio"
                                      :data        {}
                                      :connections {:pick-wrong {:handlers {}
                                                                 :parent   :pick-wrong}}}}
          reused-nodes {:empty-small 0
                        :pick-wrong  0
                        :audio-wrong 0}]
      (let [actual-result (remove-reused-nodes graph reused-nodes)
            expected-result {:box1               {:type        "object"
                                                  :data        {}
                                                  :connections {:root {:handlers {:click [:click-on-box1]}}}}
                             :box2               {:type        "object"
                                                  :data        {}
                                                  :connections {:root {:handlers {:click [:click-on-box2]}}}}
                             :click-on-box1      {:type        "test-var-scalar"
                                                  :data        {}
                                                  :connections {:box1 {:handlers {:success [:empty-small]
                                                                                  :fail    [:pick-wrong]}}}}
                             :click-on-box2      {:type        "test-var-scalar"
                                                  :data        {}
                                                  :connections {:box2 {:handlers {:success [:empty-small]
                                                                                  :fail    [:pick-wrong]}}}}
                             :empty-small-copy-1 {:type        "empty"
                                                  :data        {}
                                                  :connections {:click-on-box1 {:handlers {}}
                                                                :click-on-box2 {:handlers {}}}}
                             :empty-small-copy-2 {:type        "empty"
                                                  :data        {:type     "empty"
                                                                :duration 500}
                                                  :connections {:click-on-box1 {:handlers {}}
                                                                :click-on-box2 {:handlers {}}}}
                             :pick-wrong-copy-1  {:type        "sequence"
                                                  :data        {}
                                                  :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                                :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                             :pick-wrong-copy-2  {:type        "sequence"
                                                  :data        {}
                                                  :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                                :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                             :audio-wrong-copy-1 {:type        "audio"
                                                  :data        {}
                                                  :connections {:pick-wrong {:handlers {}
                                                                             :parent   :pick-wrong}}}
                             :audio-wrong-copy-2 {:type        "audio"
                                                  :data        {}
                                                  :connections {:pick-wrong {:handlers {}
                                                                             :parent   :pick-wrong}}}}]
        (is (= actual-result expected-result))))))

(deftest test-replicate-reused-nodes
  (testing "test-var-scalar"
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
                       :click-on-box2 {:type        "test-var-scalar"
                                       :data        {:type     "test-var-scalar"
                                                     :var-name "current-box"
                                                     :value    "box2"
                                                     :success  "empty-small"
                                                     :fail     "pick-wrong"}
                                       :connections {:box2 {:handlers {:success [:empty-small]
                                                                       :fail    [:pick-wrong]}}}}
                       :empty-small   {:type        "empty"
                                       :data        {:type     "empty"
                                                     :duration 500}
                                       :connections {:click-on-box1 {:handlers {}}
                                                     :click-on-box2 {:handlers {}}}}
                       :pick-wrong    {:type        "sequence"
                                       :data        {:type        "sequence"
                                                     :data        ["audio-wrong"]
                                                     :phrase      :wrong-click
                                                     :phrase-text "Try again"}
                                       :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                     :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                       :audio-wrong   {:type        "audio"
                                       :data        {:type     "audio"
                                                     :id       "fw-try-again"
                                                     :start    0.892
                                                     :duration 1.869
                                                     :offset   0.2}
                                       :connections {:pick-wrong {:handlers {}
                                                                  :parent   :pick-wrong}}}}
          start-nodes [:box1 :box2]
          reused-nodes {:empty-small 0
                        :pick-wrong  0
                        :audio-wrong 0}]
      (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
            expected-result {:box1               {:type        "object"
                                                  :data        {:actions {:click {:type "action"
                                                                                  :id   "click-on-box1"
                                                                                  :on   "click"}}}
                                                  :connections {:root {:handlers {:click [:click-on-box1]}}}}
                             :box2               {:type        "object"
                                                  :data        {:actions {:click {:type "action"
                                                                                  :id   "click-on-box2"
                                                                                  :on   "click"}}}
                                                  :connections {:root {:handlers {:click [:click-on-box2]}}}}
                             :click-on-box1      {:type        "test-var-scalar"
                                                  :data        {:type     "test-var-scalar"
                                                                :var-name "current-box"
                                                                :value    "box1"
                                                                :success  "empty-small"
                                                                :fail     "pick-wrong"}
                                                  :connections {:box1 {:handlers {:success [:empty-small-copy-1]
                                                                                  :fail    [:pick-wrong-copy-1]}}}}
                             :click-on-box2      {:type        "test-var-scalar"
                                                  :data        {:type     "test-var-scalar"
                                                                :var-name "current-box"
                                                                :value    "box2"
                                                                :success  "empty-small"
                                                                :fail     "pick-wrong"}
                                                  :connections {:box2 {:handlers {:success [:empty-small-copy-2]
                                                                                  :fail    [:pick-wrong-copy-2]}}}}
                             :empty-small-copy-1 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 1
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:click-on-box1 {:handlers {}}}}
                             :empty-small-copy-2 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 2
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:click-on-box2 {:handlers {}}}}
                             :pick-wrong-copy-1  {:type         "sequence"
                                                  :origin       :pick-wrong
                                                  :copy-counter 1
                                                  :data         {:type        "sequence"
                                                                 :data        ["audio-wrong"]
                                                                 :phrase      :wrong-click
                                                                 :phrase-text "Try again"}
                                                  :connections  {:click-on-box1 {:handlers {:next [:audio-wrong-copy-1]}}}}
                             :pick-wrong-copy-2  {:type         "sequence"
                                                  :origin       :pick-wrong
                                                  :copy-counter 2
                                                  :data         {:type        "sequence"
                                                                 :data        ["audio-wrong"]
                                                                 :phrase      :wrong-click
                                                                 :phrase-text "Try again"}
                                                  :connections  {:click-on-box2 {:handlers {:next [:audio-wrong-copy-2]}}}}
                             :audio-wrong-copy-1 {:type         "audio"
                                                  :origin       :audio-wrong
                                                  :copy-counter 1
                                                  :data         {:type     "audio"
                                                                 :id       "fw-try-again"
                                                                 :start    0.892
                                                                 :duration 1.869
                                                                 :offset   0.2}
                                                  :connections  {:pick-wrong-copy-1 {:handlers {}
                                                                                     :parent   :pick-wrong}}}
                             :audio-wrong-copy-2 {:type         "audio"
                                                  :origin       :audio-wrong
                                                  :copy-counter 2
                                                  :data         {:type     "audio"
                                                                 :id       "fw-try-again"
                                                                 :start    0.892
                                                                 :duration 1.869
                                                                 :offset   0.2}
                                                  :connections  {:pick-wrong-copy-2 {:handlers {}
                                                                                     :parent   :pick-wrong}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))

  (testing "sequence"
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
                       :click-on-box2 {:type        "test-var-scalar"
                                       :data        {:type     "test-var-scalar"
                                                     :var-name "current-box"
                                                     :value    "box2"
                                                     :success  "empty-small"
                                                     :fail     "pick-wrong"}
                                       :connections {:box2 {:handlers {:success [:empty-small]
                                                                       :fail    [:pick-wrong]}}}}
                       :pick-wrong    {:type        "sequence"
                                       :data        {:type        "sequence"
                                                     :data        ["audio-wrong"
                                                                   "empty-small"]
                                                     :phrase      :wrong-click
                                                     :phrase-text "Try again"}
                                       :connections {:click-on-box1 {:handlers {:next [:audio-wrong]}}
                                                     :click-on-box2 {:handlers {:next [:audio-wrong]}}}}
                       :audio-wrong   {:type        "audio"
                                       :data        {:type     "audio"
                                                     :id       "fw-try-again"
                                                     :start    0.892
                                                     :duration 1.869
                                                     :offset   0.2}
                                       :connections {:pick-wrong {:handlers {:next [:empty-small]}
                                                                  :parent   :pick-wrong}}}
                       :empty-small   {:type        "empty"
                                       :data        {:type     "empty"
                                                     :duration 500}
                                       :connections {:click-on-box1 {:handlers {}}
                                                     :click-on-box2 {:handlers {}}
                                                     :audio-wrong   {:handlers {}
                                                                     :parent   :pick-wrong}}}}
          start-nodes [:box1 :box2]
          reused-nodes {:empty-small 0
                        :pick-wrong  0
                        :audio-wrong 0}]
      (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
            expected-result {:box1               {:type        "object"
                                                  :data        {:actions {:click {:type "action"
                                                                                  :id   "click-on-box1"
                                                                                  :on   "click"}}}
                                                  :connections {:root {:handlers {:click [:click-on-box1]}}}}
                             :box2               {:type        "object"
                                                  :data        {:actions {:click {:type "action"
                                                                                  :id   "click-on-box2"
                                                                                  :on   "click"}}}
                                                  :connections {:root {:handlers {:click [:click-on-box2]}}}}
                             :click-on-box1      {:type        "test-var-scalar"
                                                  :data        {:type     "test-var-scalar"
                                                                :var-name "current-box"
                                                                :value    "box1"
                                                                :success  "empty-small"
                                                                :fail     "pick-wrong"}
                                                  :connections {:box1 {:handlers {:success [:empty-small-copy-1]
                                                                                  :fail    [:pick-wrong-copy-1]}}}}
                             :click-on-box2      {:type        "test-var-scalar"
                                                  :data        {:type     "test-var-scalar"
                                                                :var-name "current-box"
                                                                :value    "box2"
                                                                :success  "empty-small"
                                                                :fail     "pick-wrong"}
                                                  :connections {:box2 {:handlers {:success [:empty-small-copy-3]
                                                                                  :fail    [:pick-wrong-copy-2]}}}}
                             :empty-small-copy-1 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 1
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:click-on-box1 {:handlers {}}}}
                             :empty-small-copy-3 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 3
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:click-on-box2 {:handlers {}}}}
                             :pick-wrong-copy-1  {:type         "sequence"
                                                  :origin       :pick-wrong
                                                  :copy-counter 1
                                                  :data         {:type        "sequence"
                                                                 :data        ["audio-wrong"
                                                                               "empty-small"]
                                                                 :phrase      :wrong-click
                                                                 :phrase-text "Try again"}
                                                  :connections  {:click-on-box1 {:handlers {:next [:audio-wrong-copy-1]}}}}
                             :pick-wrong-copy-2  {:type         "sequence"
                                                  :origin       :pick-wrong
                                                  :copy-counter 2
                                                  :data         {:type        "sequence"
                                                                 :data        ["audio-wrong"
                                                                               "empty-small"]
                                                                 :phrase      :wrong-click
                                                                 :phrase-text "Try again"}
                                                  :connections  {:click-on-box2 {:handlers {:next [:audio-wrong-copy-2]}}}}
                             :audio-wrong-copy-1 {:type         "audio"
                                                  :origin       :audio-wrong
                                                  :copy-counter 1
                                                  :data         {:type     "audio"
                                                                 :id       "fw-try-again"
                                                                 :start    0.892
                                                                 :duration 1.869
                                                                 :offset   0.2}
                                                  :connections  {:pick-wrong-copy-1 {:handlers {:next [:empty-small-copy-2]}
                                                                                     :parent   :pick-wrong}}}
                             :audio-wrong-copy-2 {:type         "audio"
                                                  :origin       :audio-wrong
                                                  :copy-counter 2
                                                  :data         {:type     "audio"
                                                                 :id       "fw-try-again"
                                                                 :start    0.892
                                                                 :duration 1.869
                                                                 :offset   0.2}
                                                  :connections  {:pick-wrong-copy-2 {:handlers {:next [:empty-small-copy-4]}
                                                                                     :parent   :pick-wrong}}}
                             :empty-small-copy-2 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 2
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:audio-wrong-copy-1 {:handlers {}
                                                                                      :parent   :pick-wrong}}}
                             :empty-small-copy-4 {:type         "empty"
                                                  :origin       :empty-small
                                                  :copy-counter 4
                                                  :data         {:type     "empty"
                                                                 :duration 500}
                                                  :connections  {:audio-wrong-copy-2 {:handlers {}
                                                                                      :parent   :pick-wrong}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))

  (testing "sequence"
    (let [parsed-data {:a {:connections {:root {:handlers {:next [:c :d]}}}}
                       :b {:connections {:root {:handlers {:next [:c :d]}}}}
                       :c {:connections {:a {:handlers {}}
                                         :b {:handlers {}}
                                         :e {:handlers {}}}}
                       :d {:connections {:b {:handlers {:next [:e]}}
                                         :a {:handlers {:next [:e]}}}}
                       :e {:connections {:d {:handlers {:next [:c]}}}}}
          start-nodes [:a :b]
          reused-nodes {:c 0
                        :d 0
                        :e 0}]
      (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
            expected-result {:a        {:connections {:root {:handlers {:next [:c-copy-1 :d-copy-1]}}}}
                             :b        {:connections {:root {:handlers {:next [:c-copy-3 :d-copy-2]}}}}
                             :c-copy-1 {:connections  {:a {:handlers {}}}
                                        :origin       :c
                                        :copy-counter 1}
                             :c-copy-2 {:connections  {:e-copy-1 {:handlers {}}}
                                        :origin       :c
                                        :copy-counter 2}
                             :c-copy-3 {:connections  {:b {:handlers {}}}
                                        :origin       :c
                                        :copy-counter 3}
                             :c-copy-4 {:connections  {:e-copy-2 {:handlers {}}}
                                        :origin       :c
                                        :copy-counter 4}
                             :d-copy-1 {:connections  {:a {:handlers {:next [:e-copy-1]}}}
                                        :origin       :d
                                        :copy-counter 1}
                             :d-copy-2 {:connections  {:b {:handlers {:next [:e-copy-2]}}}
                                        :origin       :d
                                        :copy-counter 2}
                             :e-copy-1 {:connections  {:d-copy-1 {:handlers {:next [:c-copy-2]}}}
                                        :origin       :e
                                        :copy-counter 1}
                             :e-copy-2 {:connections  {:d-copy-2 {:handlers {:next [:c-copy-4]}}}
                                        :origin       :e
                                        :copy-counter 2}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))

  (testing "case with phrase 'this-is-concept' in home scene"
    (let [parsed-data {:a {:connections {:root {:handlers {:next [:b]}}}}
                       :b {:connections {:a {:handlers {:next [:c]}}}}
                       :c {:connections {:e {:handlers {:next [:f]}}
                                         :b {:handlers {:next [:d]}}}}
                       :d {:connections {:c {:handlers {:next [:e]}}}}
                       :e {:connections {:d {:handlers {:next [:c]}}}}

                       :f {:connections {:c {:handlers {}}}}}
          start-nodes [:a]
          reused-nodes {:c 0}]
      (let [actual-result (replicate-reused-nodes parsed-data start-nodes reused-nodes)
            expected-result {:a        {:connections {:root {:handlers {:next [:b]}}}}
                             :b        {:connections {:a {:handlers {:next [:c-copy-1]}}}}
                             :c-copy-1 {:connections  {:b {:handlers {:next [:d]}}}
                                        :origin       :c
                                        :copy-counter 1}
                             :d        {:connections {:c-copy-1 {:handlers {:next [:e]}}}}
                             :e        {:connections {:d {:handlers {:next [:c-copy-2]}}}}

                             :c-copy-2 {:connections  {:e {:handlers {:next [:f]}}}
                                        :origin       :c
                                        :copy-counter 2}
                             :f        {:connections {:c-copy-2 {:handlers {}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
