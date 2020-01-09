(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]))

(deftest test-scene-parser--object-with-click-handler
  (let [scene-data {:objects {:a {:type    "animation"
                                  :actions {:click {:type "action" :id "b" :on "click"}}}}
                    :actions {:b {:type "empty"}}}]
    (let [actual-result (parse-data scene-data)
          expected-result {:a {:entity      :object
                               :data        {:type    "animation"
                                             :actions {:click {:type "action" :id "b" :on "click"}}}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "click"
                                               :handler  :b}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--object-with-drag-handler
  (let [scene-data {:objects {:a {:type    "animation"
                                  :actions {:drag-end {:type "action" :id "b" :on "drag-end"}}}}
                    :actions {:b {:type "empty"}}}]
    (let [actual-result (parse-data scene-data)
          expected-result {:a {:entity      :object
                               :data        {:type    "animation"
                                             :actions {:drag-end {:type "action" :id "b" :on "drag-end"}}}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "drag-end"
                                               :handler  :b}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--trigger-back
  (let [scene-data {:triggers {:a {:on "back" :action "b"}}
                    :actions  {:b {:type "empty"}}}]
    (let [actual-result (parse-data scene-data)
          expected-result {:a {:entity      :trigger
                               :data        {:on "back" :action "b"}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "back"
                                               :handler  :b}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--trigger-start
  (let [scene-data {:triggers {:a {:on "start" :action "c"}
                               :b {:on "start" :action "d"}}
                    :actions  {:c {:type "empty"}
                               :d {:type "empty"}}}]
    (let [actual-result (parse-data scene-data)
          expected-result {:a {:entity      :trigger
                               :data        {:on "start" :action "c"}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "start"
                                               :handler  :c}}}
                           :b {:entity      :trigger
                               :data        {:on "start" :action "d"}
                               :path        [:b]
                               :connections #{{:previous :root
                                               :name     "start"
                                               :handler  :d}}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{}}
                           :d {:entity      :action
                               :data        {:type "empty"}
                               :path        [:d]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-data-single
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence-data"
                                  :data [{:type "empty"}
                                         {:type "empty"}]}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a   {:entity      :action
                                 :data        {:type "sequence-data"
                                               :data [{:type "empty"}
                                                      {:type "empty"}]}
                                 :path        [:a]
                                 :connections #{{:previous :root
                                                 :handler  :a-0
                                                 :name     "next"
                                                 :sequence :a}}}
                           :a-0 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:a 0]
                                 :connections #{{:previous :a
                                                 :handler  :a-1
                                                 :name     "next"
                                                 :sequence :a}}}
                           :a-1 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:a 1]
                                 :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-data-in-sequence
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "c"]}
                              :b {:type "sequence-data"
                                  :data [{:type "empty"}
                                         {:type "empty"}]}
                              :c {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a   {:entity      :action
                                 :data        {:type "sequence"
                                               :data ["b" "c"]}
                                 :path        [:a]
                                 :connections #{{:previous :root
                                                 :handler  :b
                                                 :name     "next"
                                                 :sequence :a}}}
                           :b   {:entity      :action
                                 :data        {:type "sequence-data"
                                               :data [{:type "empty"}
                                                      {:type "empty"}]}
                                 :path        [:b]
                                 :connections #{{:previous :a
                                                 :handler  :b-0
                                                 :name     "next"
                                                 :sequence :b}}}
                           :b-0 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:b 0]
                                 :connections #{{:previous :b
                                                 :handler  :b-1
                                                 :name     "next"
                                                 :sequence :b}}}
                           :b-1 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:b 1]
                                 :connections #{{:previous :b-0
                                                 :handler  :c
                                                 :name     "next"
                                                 :sequence :a}}}
                           :c   {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:c]
                                 :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-data-in-sequence-data
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence-data"
                                  :data [{:type "empty"}
                                         {:type "sequence-data"
                                          :data [{:type "empty"}
                                                 {:type "empty"}]}
                                         {:type "empty"}
                                         {:type "empty"}]}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a     {:entity      :action
                                   :data        {:type "sequence-data"
                                                 :data [{:type "empty"}
                                                        {:type "sequence-data"
                                                         :data [{:type "empty"}
                                                                {:type "empty"}]}
                                                        {:type "empty"}
                                                        {:type "empty"}]}
                                   :path        [:a]
                                   :connections #{{:previous :root
                                                   :handler  :a-0
                                                   :name     "next"
                                                   :sequence :a}}}
                           :a-0   {:entity      :action
                                   :data        {:type "empty"}
                                   :path        [:a 0]
                                   :connections #{{:previous :a
                                                   :handler  :a-1
                                                   :name     "next"
                                                   :sequence :a}}}
                           :a-1   {:entity      :action
                                   :data        {:type "sequence-data"
                                                 :data [{:type "empty"}
                                                        {:type "empty"}]}
                                   :path        [:a 1]
                                   :connections #{{:previous :a-0
                                                   :handler  :a-1-0
                                                   :name     "next"
                                                   :sequence :a-1}}}
                           :a-1-0 {:entity      :action
                                   :data        {:type "empty"}
                                   :path        [:a 1 0]
                                   :connections #{{:previous :a-1
                                                   :handler  :a-1-1
                                                   :name     "next"
                                                   :sequence :a-1}}}
                           :a-1-1 {:entity      :action
                                   :data        {:type "empty"}
                                   :path        [:a 1 1]
                                   :connections #{{:previous :a-1-0
                                                   :handler  :a-2
                                                   :name     "next"
                                                   :sequence :a}}}
                           :a-2   {:entity      :action
                                   :data        {:type "empty"}
                                   :path        [:a 2]
                                   :connections #{{:previous :a-1-1
                                                   :handler  :a-3
                                                   :name     "next"
                                                   :sequence :a}}}
                           :a-3   {:entity      :action
                                   :data        {:type "empty"}
                                   :path        [:a 3]
                                   :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

;; ToDo: Fix parsing
;(deftest test-scene-parser--sequence-data-with-test-inside
;  (let [start-node-name :a
;        scene-data {:actions {:a {:type "sequence-data"
;                                  :data [{:type    "test-var-scalar"
;                                          :success {:type "empty"}
;                                          :fail    {:type "empty"}}
;                                         {:type "empty"}
;                                         {:type "empty"}]}}}]
;    (let [actual-result (parse-data scene-data start-node-name)
;          expected-result {:a           {:data        {:type "sequence-data"
;                                                       :data [{:type    "test-var-scalar"
;                                                               :success {:type "empty"}
;                                                               :fail    {:type "empty"}}
;                                                              {:type "empty"}
;                                                              {:type "empty"}]}
;                                         :path        [:a]
;                                         :connections #{{:previous :root
;                                                         :handler  :a-0
;                                                         :name     "next"
;                                                         :sequence :a}}}
;                           :a-0         {:data        {:type    "test-var-scalar"
;                                                       :success {:type "empty"}
;                                                       :fail    {:type "empty"}}
;                                         :path        [:a 0]
;                                         :connections #{{:previous :a
;                                                         :name     "success"
;                                                         :handler  :a-0-success}
;                                                        {:previous :a
;                                                         :name     "fail"
;                                                         :handler  :a-0-fail}}}
;                           :a-0-success {:data        {:type "empty"}
;                                         :path        [:a 0 :success]
;                                         :connections #{{:previous :a-0
;                                                         :handler  :a-1
;                                                         :name     "next"
;                                                         :sequence :a}}}
;                           :a-0-fail    {:data        {:type "empty"}
;                                         :path        [:a 0 :fail]
;                                         :connections #{{:previous :a-0
;                                                         :handler  :a-1
;                                                         :name     "next"
;                                                         :sequence :a}}}
;                           :a-1         {:data        {:type "empty"}
;                                         :path        [:a 1]
;                                         :connections #{{:previous :a-0-success
;                                                         :handler  :a-2
;                                                         :name     "next"
;                                                         :sequence :a}
;                                                        {:previous :a-0-fail
;                                                         :handler  :a-2
;                                                         :name     "next"
;                                                         :sequence :a}}}
;                           :a-2         {:data        {:type "empty"}
;                                         :path        [:a 2]
;                                         :connections #{}}}]
;      (when-not (= actual-result expected-result)
;        (print-maps-comparison actual-result expected-result))
;      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-in-sequence
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "c" "d" "g"]}
                              :b {:type "empty"}
                              :c {:type "sequence"
                                  :data ["e" "f"]}
                              :d {:type "empty"}
                              :e {:type "empty"}
                              :f {:type "empty"}
                              :g {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type "sequence"
                                             :data ["b" "c" "d" "g"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :handler  :b
                                               :name     "next"
                                               :sequence :a}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :handler  :c
                                               :name     "next"
                                               :sequence :a}}}
                           :c {:entity      :action
                               :data        {:type "sequence"
                                             :data ["e" "f"]}
                               :path        [:c]
                               :connections #{{:previous :b
                                               :handler  :e
                                               :name     "next"
                                               :sequence :c}}}
                           :e {:entity      :action
                               :data        {:type "empty"}
                               :path        [:e]
                               :connections #{{:previous :c
                                               :handler  :f
                                               :name     "next"
                                               :sequence :c}}}
                           :f {:entity      :action
                               :data        {:type "empty"}
                               :path        [:f]
                               :connections #{{:previous :e
                                               :handler  :d
                                               :name     "next"
                                               :sequence :a}}}
                           :d {:entity      :action
                               :data        {:type "empty"}
                               :path        [:d]
                               :connections #{{:previous :f
                                               :handler  :g
                                               :name     "next"
                                               :sequence :a}}}
                           :g {:entity      :action
                               :data        {:type "empty"}
                               :path        [:g]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-with-repetitive-actions
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "c"]}
                              :b {:type "sequence"
                                  :data ["d" "e" "d" "e"]}
                              :c {:type "empty"}
                              :d {:type "empty"}
                              :e {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type "sequence"
                                             :data ["b" "c"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :handler  :b
                                               :name     "next"
                                               :sequence :a}}}
                           :b {:entity      :action
                               :data        {:type "sequence"
                                             :data ["d" "e" "d" "e"]}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :handler  :d
                                               :name     "next"
                                               :sequence :b}}}
                           :d {:entity      :action
                               :data        {:type "empty"}
                               :path        [:d]
                               :connections #{{:previous :b
                                               :handler  :e
                                               :name     "next"
                                               :sequence :b}
                                              {:previous :e
                                               :handler  :e
                                               :name     "next"
                                               :sequence :b}}}
                           :e {:entity      :action
                               :data        {:type "empty"}
                               :path        [:e]
                               :connections #{{:previous :d
                                               :handler  :d
                                               :name     "next"
                                               :sequence :b}
                                              {:previous :d
                                               :handler  :c
                                               :name     "next"
                                               :sequence :a}}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-with-double-duplicates
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "b" "c" "b"]}
                              :c {:type "empty"}
                              :b {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type "sequence"
                                             :data ["b" "b" "c" "b"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :handler  :b
                                               :name     "next"
                                               :sequence :a}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :handler  :b
                                               :name     "next"
                                               :sequence :a}
                                              {:previous :b
                                               :handler  :c
                                               :name     "next"
                                               :sequence :a}}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{{:previous :b
                                               :handler  :b
                                               :name     "next"
                                               :sequence :a}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-with-cycle
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "e"]}
                              :b {:type               "sequence"
                                  :data               ["c" "d" "b"]
                                  :return-immediately true}
                              :c {:type "empty"}
                              :d {:type "empty"}
                              :e {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type "sequence"
                                             :data ["b" "e"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b
                                               :sequence :a}}}
                           :b {:entity      :action
                               :data        {:type               "sequence"
                                             :data               ["c" "d" "b"]
                                             :return-immediately true}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c
                                               :sequence :b}
                                              {:previous :a
                                               :name     "next"
                                               :handler  :e
                                               :sequence :a}
                                              {:previous :d
                                               :name     "next"
                                               :handler  :c
                                               :sequence :b}}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{{:previous :b
                                               :name     "next"
                                               :handler  :d
                                               :sequence :b}}}
                           :d {:entity      :action
                               :data        {:type "empty"}
                               :path        [:d]
                               :connections #{{:previous :c
                                               :name     "next"
                                               :handler  :b
                                               :sequence :b}}}
                           :e {:entity      :action
                               :data        {:type "empty"}
                               :path        [:e]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--2-sequences-with-cycle
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "e"]}
                              :b {:type               "sequence"
                                  :data               ["c" "d" "f"]
                                  :return-immediately true}
                              :f {:type "sequence"
                                  :data ["c" "d" "b"]}
                              :c {:type "empty"}
                              :d {:type "empty"}
                              :e {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type "sequence"
                                             :data ["b" "e"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b
                                               :sequence :a}}}
                           :b {:entity      :action
                               :data        {:type               "sequence"
                                             :data               ["c" "d" "f"]
                                             :return-immediately true}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c
                                               :sequence :b}
                                              {:previous :a
                                               :name     "next"
                                               :handler  :e
                                               :sequence :a}
                                              {:previous :d
                                               :name     "next"
                                               :handler  :c
                                               :sequence :b}}}
                           :f {:entity      :action
                               :data        {:type "sequence"
                                             :data ["c" "d" "b"]}
                               :path        [:f]
                               :connections #{{:previous :d
                                               :name     "next"
                                               :handler  :c
                                               :sequence :f}}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{{:previous :b
                                               :name     "next"
                                               :handler  :d
                                               :sequence :b}
                                              {:previous :f
                                               :name     "next"
                                               :handler  :d
                                               :sequence :f}}}
                           :d {:entity      :action
                               :data        {:type "empty"}
                               :path        [:d]
                               :connections #{{:previous :c
                                               :name     "next"
                                               :handler  :f
                                               :sequence :b}
                                              {:previous :c
                                               :name     "next"
                                               :handler  :b
                                               :sequence :f}}}
                           :e {:entity      :action
                               :data        {:type "empty"}
                               :path        [:e]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--sequence-data
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence-data"
                                  :data [{:type "empty"}
                                         {:type "empty"}]}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a   {:entity      :action
                                 :data        {:type "sequence-data"
                                               :data [{:type "empty"}
                                                      {:type "empty"}]}
                                 :path        [:a]
                                 :connections #{{:previous :root
                                                 :name     "next"
                                                 :handler  :a-0
                                                 :sequence :a}}}
                           :a-0 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:a 0]
                                 :connections #{{:previous :a
                                                 :name     "next"
                                                 :handler  :a-1
                                                 :sequence :a}}}
                           :a-1 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:a 1]
                                 :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--parallel
  (let [start-node-name :a
        scene-data {:actions {:a {:type "sequence"
                                  :data ["b" "c" "d"]}
                              :b {:type "parallel"
                                  :data [{:type "empty"}
                                         {:type "empty"}]}
                              :c {:type "empty"}
                              :d {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a   {:entity      :action
                                 :data        {:type "sequence"
                                               :data ["b" "c" "d"]}
                                 :path        [:a]
                                 :connections #{{:previous :root
                                                 :name     "next"
                                                 :handler  :b
                                                 :sequence :a}}}
                           :b   {:entity      :action
                                 :data        {:type "parallel"
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
                                                 :sequence :b}}}
                           :b-0 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:b 0]
                                 :connections #{{:previous :b
                                                 :name     "next"
                                                 :handler  :c
                                                 :sequence :a}}}
                           :b-1 {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:b 1]
                                 :connections #{{:previous :b
                                                 :name     "next"
                                                 :handler  :c
                                                 :sequence :a}}}
                           :c   {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:c]
                                 :connections #{{:previous :b-0
                                                 :name     "next"
                                                 :handler  :d
                                                 :sequence :a}
                                                {:previous :b-1
                                                 :name     "next"
                                                 :handler  :d
                                                 :sequence :a}}}
                           :d   {:entity      :action
                                 :data        {:type "empty"}
                                 :path        [:d]
                                 :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--test-var-scalar-with-actions-name
  (let [start-node-name :a
        scene-data {:actions {:a {:type    "test-var-scalar"
                                  :success "b"
                                  :fail    "c"}
                              :b {:type "empty"}
                              :c {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a {:entity      :action
                               :data        {:type    "test-var-scalar"
                                             :success "b"
                                             :fail    "c"}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "success"
                                               :handler  :b}
                                              {:previous :root
                                               :name     "fail"
                                               :handler  :c}}}
                           :b {:entity      :action
                               :data        {:type "empty"}
                               :path        [:b]
                               :connections #{}}
                           :c {:entity      :action
                               :data        {:type "empty"}
                               :path        [:c]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--test-var-scalar-with-actions-data
  (let [start-node-name :a
        scene-data {:actions {:a {:type    "test-var-scalar"
                                  :success {:type "empty"}
                                  :fail    {:type "empty"}}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a         {:entity      :action
                                       :data        {:type    "test-var-scalar"
                                                     :success {:type "empty"}
                                                     :fail    {:type "empty"}}
                                       :path        [:a]
                                       :connections #{{:previous :root
                                                       :name     "success"
                                                       :handler  :a-success}
                                                      {:previous :root
                                                       :name     "fail"
                                                       :handler  :a-fail}}}
                           :a-success {:entity      :action
                                       :data        {:type "empty"}
                                       :path        [:a :success]
                                       :connections #{}}
                           :a-fail    {:entity      :action
                                       :data        {:type "empty"}
                                       :path        [:a :fail]
                                       :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--action-with-id
  (let [start-node-name :a
        scene-data {:actions {:a        {:type "sequence"
                                         :data ["b" "c"]}
                              :b        {:type "action"
                                         :id   "b-next-1"}
                              :b-next-1 {:type "empty"}
                              :c        {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a        {:entity      :action
                                      :data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:entity      :action
                                      :data        {:type "action"
                                                    :id   "b-next-1"}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-1
                                                      :sequence :a}}}
                           :b-next-1 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-1]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--action-with-template-from-var
  (let [start-node-name :a
        scene-data {:actions {:a        {:type "sequence"
                                         :data ["b" "c"]}
                              :b        {:type     "action"
                                         :from-var [{:var-name        "some-var"
                                                     :template        "b-next-%"
                                                     :action-property "id"
                                                     :possible-values [1 "2" :3]}]}
                              :b-next-1 {:type "empty"}
                              :b-next-2 {:type "empty"}
                              :b-next-3 {:type "empty"}
                              :c        {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a        {:entity      :action
                                      :data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:entity      :action
                                      :data        {:type     "action"
                                                    :from-var [{:var-name        "some-var"
                                                                :template        "b-next-%"
                                                                :action-property "id"
                                                                :possible-values [1 "2" :3]}]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-1
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-2
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-3
                                                      :sequence :a}}}
                           :b-next-1 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-1]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-2 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-2]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-3 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-3]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--action-with-template-from-params
  (let [start-node-name :a
        scene-data {:actions {:a        {:type "sequence"
                                         :data ["b" "c"]}
                              :b        {:type        "action"
                                         :from-params [{:template        "b-next-%"
                                                        :action-property "id"
                                                        :possible-values [1 "2" :3]}]}
                              :b-next-1 {:type "empty"}
                              :b-next-2 {:type "empty"}
                              :b-next-3 {:type "empty"}
                              :c        {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a        {:entity      :action
                                      :data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:entity      :action
                                      :data        {:type        "action"
                                                    :from-params [{:template        "b-next-%"
                                                                   :action-property "id"
                                                                   :possible-values [1 "2" :3]}]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-1
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-2
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-3
                                                      :sequence :a}}}
                           :b-next-1 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-1]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-2 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-2]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-3 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-3]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--action-with-passed-name-from-var
  (let [start-node-name :a
        scene-data {:actions {:a        {:type "sequence"
                                         :data ["b" "c"]}
                              :b        {:type     "action"
                                         :from-var [{:var-name        "some-var"
                                                     :action-property "id"
                                                     :possible-values ["b-next-1" :b-next-2]}]}
                              :b-next-1 {:type "empty"}
                              :b-next-2 {:type "empty"}
                              :c        {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a        {:entity      :action
                                      :data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:entity      :action
                                      :data        {:type     "action"
                                                    :from-var [{:var-name        "some-var"
                                                                :action-property "id"
                                                                :possible-values ["b-next-1" :b-next-2]}]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-1
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-2
                                                      :sequence :a}}}
                           :b-next-1 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-1]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-2 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-2]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-scene-parser--action-with-passed-name-from-params
  (let [start-node-name :a
        scene-data {:actions {:a        {:type "sequence"
                                         :data ["b" "c"]}
                              :b        {:type        "action"
                                         :from-params [{:action-property "id"
                                                        :possible-values ["b-next-1" :b-next-2]}]}
                              :b-next-1 {:type "empty"}
                              :b-next-2 {:type "empty"}
                              :c        {:type "empty"}}}]
    (let [actual-result (parse-data scene-data start-node-name)
          expected-result {:a        {:entity      :action
                                      :data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:entity      :action
                                      :data        {:type        "action"
                                                    :from-params [{:action-property "id"
                                                                   :possible-values ["b-next-1" :b-next-2]}]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-1
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :b-next-2
                                                      :sequence :a}}}
                           :b-next-1 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-1]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-next-2 {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:b-next-2]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:entity      :action
                                      :data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

;; ToDo: Implement
;; {:type "action" :from-var [{:var-name "current-word" :var-property "game-voice-action-low"}]}

;; ToDo: Fix parsing
;(deftest test-scene-parser--deep-nesting-actions-data
;  (let [start-node-name :a
;        scene-data {:actions {:a {:type "sequence-data"
;                                  :data [{:type    "test-var-scalar"
;                                          :success {:type "sequence-data"
;                                                    :data [{:type "set-variable" :var-name "var-1" :var-value 1}
;                                                           {:type    "test-var-scalar"
;                                                            :success {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                            :fail    {:type "set-variable" :var-name "var-1-2" :var-value 12}}]}
;                                          :fail    {:type "empty"}}
;                                         {:type    "test-var-scalar"
;                                          :success {:type "sequence-data"
;                                                    :data [{:type "set-variable" :var-name "var-2" :var-value 2}
;                                                           {:type    "test-var-scalar"
;                                                            :success {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                            :fail    {:type "set-variable" :var-name "var-2-2" :var-value 22}}]}
;                                          :fail    {:type "empty"}}]}}}]
;    (let [actual-result (parse-data scene-data start-node-name)
;          expected-result {:a                     {:data        {:type "sequence-data"
;                                                                 :data [{:type    "test-var-scalar"
;                                                                         :success {:type "sequence-data"
;                                                                                   :data [{:type "set-variable" :var-name "var-1" :var-value 1}
;                                                                                          {:type    "test-var-scalar"
;                                                                                           :success {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                                                           :fail    {:type "set-variable" :var-name "var-1-2" :var-value 12}}]}
;                                                                         :fail    {:type "empty"}}
;                                                                        {:type    "test-var-scalar"
;                                                                         :success {:type "sequence-data"
;                                                                                   :data [{:type "set-variable" :var-name "var-2" :var-value 2}
;                                                                                          {:type    "test-var-scalar"
;                                                                                           :success {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                                                           :fail    {:type "set-variable" :var-name "var-2-2" :var-value 22}}]}
;                                                                         :fail    {:type "empty"}}]}
;                                                   :path        [:a]
;                                                   :connections #{{:previous :root
;                                                                   :name     "next"
;                                                                   :handler  :a-0
;                                                                   :sequence :a}}}
;
;                           :a-0                   {:data        {:type    "test-var-scalar"
;                                                                 :success {:type "sequence-data"
;                                                                           :data [{:type "set-variable" :var-name "var-1" :var-value 1}
;                                                                                  {:type    "test-var-scalar"
;                                                                                   :success {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                                                   :fail    {:type "set-variable" :var-name "var-1-2" :var-value 12}}]}
;                                                                 :fail    {:type "empty"}}
;                                                   :path        [:a 0]
;                                                   :connections #{{:previous :a
;                                                                   :name     "success"
;                                                                   :handler  :a-0-success}
;                                                                  {:previous :a
;                                                                   :name     "fail"
;                                                                   :handler  :a-0-fail}}}
;
;                           :a-0-success           {:data        {:type "sequence-data"
;                                                                 :data [{:type "set-variable" :var-name "var-1" :var-value 1}
;                                                                        {:type    "test-var-scalar"
;                                                                         :success {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                                         :fail    {:type "set-variable" :var-name "var-1-2" :var-value 12}}]}
;                                                   :path        [:a 0 :success]
;                                                   :connections #{{:previous :a-0
;                                                                   :name     "next"
;                                                                   :handler  :a-0-success-0
;                                                                   :sequence :a-0-success}}}
;
;                           :a-0-success-0         {:data        {:type "set-variable" :var-name "var-1" :var-value 1}
;                                                   :path        [:a 0 :success 0]
;                                                   :connections #{{:previous :a-0-success
;                                                                   :name     "next"
;                                                                   :handler  :a-0-success-1
;                                                                   :sequence :a-0-success}}}
;
;                           :a-0-success-1         {:data        {:type    "test-var-scalar"
;                                                                 :success {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                                 :fail    {:type "set-variable" :var-name "var-1-2" :var-value 12}}
;                                                   :path        [:a 0 :success 1]
;                                                   :connections #{{:previous :a-0-success-0
;                                                                   :name     "success"
;                                                                   :handler  :a-0-success-1-success}
;                                                                  {:previous :a-0-success-0
;                                                                   :name     "fail"
;                                                                   :handler  :a-0-success-1-fail}}}
;
;                           :a-0-success-1-success {:data        {:type "set-variable" :var-name "var-1-1" :var-value 11}
;                                                   :path        [:a 0 :success 1 :success]
;                                                   :connections #{{:previous :a-0-success-1
;                                                                   :name     "next"
;                                                                   :handler  :a-1}}}
;
;                           :a-0-success-1-fail    {:data        {:type "set-variable" :var-name "var-1-2" :var-value 12}
;                                                   :path        [:a 0 :success 1 :fail]
;                                                   :connections #{{:previous :a-0-success-1
;                                                                   :name     "next"
;                                                                   :handler  :a-1}}}
;
;                           :a-0-fail              {:data        {:type "empty"}
;                                                   :path        [:a 0 :fail]
;                                                   :connections #{{:previous :a-0
;                                                                   :name     "next"
;                                                                   :handler  :a-1}}}
;
;                           :a-1                   {:data        {:type    "test-var-scalar"
;                                                                 :success {:type "sequence-data"
;                                                                           :data [{:type "set-variable" :var-name "var-2" :var-value 2}
;                                                                                  {:type    "test-var-scalar"
;                                                                                   :success {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                                                   :fail    {:type "set-variable" :var-name "var-2-2" :var-value 22}}]}
;                                                                 :fail    {:type "empty"}}
;                                                   :path        [:a 1]
;                                                   :connections #{{:previous :a-0-success-1-success
;                                                                   :name     "success"
;                                                                   :handler  :a-1-success}
;                                                                  {:previous :a-0-success-1-success
;                                                                   :name     "fail"
;                                                                   :handler  :a-1-fail}
;                                                                  {:previous :a-0-success-1-fail
;                                                                   :name     "success"
;                                                                   :handler  :a-1-success}
;                                                                  {:previous :a-0-success-1-fail
;                                                                   :name     "fail"
;                                                                   :handler  :a-1-fail}
;                                                                  {:previous :a-0-fail
;                                                                   :name     "success"
;                                                                   :handler  :a-1-success}
;                                                                  {:previous :a-0-fail
;                                                                   :name     "fail"
;                                                                   :handler  :a-1-fail}}}
;
;                           :a-1-success           {:data        {:type "sequence-data"
;                                                                 :data [{:type "set-variable" :var-name "var-2" :var-value 2}
;                                                                        {:type    "test-var-scalar"
;                                                                         :success {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                                         :fail    {:type "set-variable" :var-name "var-2-2" :var-value 22}}]}
;                                                   :path        [:a 1 :success]
;                                                   :connections #{{:previous :a-1
;                                                                   :name     "next"
;                                                                   :handler  :a-1-success-0
;                                                                   :sequence :a-1-success}}}
;
;                           :a-1-success-0         {:data        {:type "set-variable" :var-name "var-2" :var-value 2}
;                                                   :path        [:a 1 :success 0]
;                                                   :connections #{{:previous :a-1-success
;                                                                   :name     "next"
;                                                                   :handler  :a-1-success-1
;                                                                   :sequence :a-1-success}}}
;
;                           :a-1-success-1         {:data        {:type    "test-var-scalar"
;                                                                 :success {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                                 :fail    {:type "set-variable" :var-name "var-2-2" :var-value 22}}
;                                                   :path        [:a 1 :success 1]
;                                                   :connections #{{:previous :a-1-success-0
;                                                                   :name     "success"
;                                                                   :handler  :a-1-success-1-success}
;                                                                  {:previous :a-1-success-0
;                                                                   :name     "fail"
;                                                                   :handler  :a-1-success-1-fail}}}
;
;                           :a-1-success-1-success {:data        {:type "set-variable" :var-name "var-2-1" :var-value 21}
;                                                   :path        [:a 1 :success 1 :success]
;                                                   :connections #{}}
;
;                           :a-1-success-1-fail    {:data        {:type "set-variable" :var-name "var-2-2" :var-value 22}
;                                                   :path        [:a 1 :success 1 :fail]
;                                                   :connections #{}}
;
;                           :a-1-fail              {:data        {:type "empty"}
;                                                   :path        [:a 1 :fail]
;                                                   :connections #{}}
;                           }]
;      (when-not (= actual-result expected-result)
;        (print-maps-comparison actual-result expected-result))
;      (is (= actual-result expected-result)))))
