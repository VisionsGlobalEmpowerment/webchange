(ns webchange.editor-v2.scene-diagram.scene-parser.action-children-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.scene-diagram.scene-parser.action-children :refer [get-action-children]]))

(deftest test-get-action-children__with-id
  (let [action-path [:a]
        action-data {:type "action"
                     :id   "b"}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-children__from-var
  (let [action-path [:a]
        action-data {:type     "action"
                     :from-var [{:var-name        "some-var"
                                 :action-property "id"
                                 :possible-values ["b-next-1" :b-next-2]}]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b-next-1] [:b-next-2]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-children__from-params
  (let [action-path [:a]
        action-data {:type        "action"
                     :from-params [{:var-name        "some-var"
                                    :action-property "id"
                                    :possible-values ["b-next-1" :b-next-2]}]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b-next-1] [:b-next-2]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-action-children__with-template
  (let [action-path [:a]
        action-data {:type        "action"
                     :from-params [{:template        "b-next-%"
                                    :action-property "id"
                                    :possible-values [1 "2" :3]}]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b-next-1] [:b-next-2] [:b-next-3]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-case-children
  (let [action-path [:a]
        action-data {:type    "case"
                     :options {:case-1 {:type "action" :id "b"}
                               :case-2 {:type "empty"}}}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:a :options :case-1] [:a :options :case-2]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-provider-children__with-id
  (let [action-path [:a]
        action-data {:type   "lesson-var-provider"
                     :on-end "b"}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-provider-children__with-data
  (let [action-path [:a]
        action-data {:type   "lesson-var-provider"
                     :on-end {:type "empty"}}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:a :on-end]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-sequence-children__sequence
  (let [action-path [:a]
        action-data {:type "sequence"
                     :data ["b" "c"]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b] [:c]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-sequence-children__sequence-data
  (let [action-path [:a]
        action-data {:type "sequence-data"
                     :data [{:type "empty"}
                            {:type "empty"}]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:a :data 0] [:a :data 1]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-sequence-children__parallel
  (let [action-path [:a]
        action-data {:type "parallel"
                     :data [{:type "empty"}
                            {:type "empty"}]}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:a :data 0] [:a :data 1]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-test-children__with-id
  (let [action-path [:a]
        action-data {:type    "test-var-scalar"
                     :success "b"
                     :fail    "c"}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:b] [:c]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-test-children__with-data
  (let [action-path [:a]
        action-data {:type    "test-var-scalar"
                     :success {:type "empty"}
                     :fail    {:type "empty"}}]
    (let [actual-result (get-action-children {:action-path action-path
                                              :action-data action-data})
          expected-result [[:a :success] [:a :fail]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
