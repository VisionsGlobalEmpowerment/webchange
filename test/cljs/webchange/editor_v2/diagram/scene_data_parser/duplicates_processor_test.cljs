(ns webchange.editor-v2.diagram.scene-data-parser.duplicates-processor-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [compare-maps]]
    [webchange.editor-v2.diagram.scene-data-parser.duplicates-processor :refer [fix-next-usage
                                                                                fix-next-usages
                                                                                fix-prev-usage
                                                                                process-duplicates]]))

(deftest test-fix-prev-usage
  (testing "test duplicated node parent replacement"
    (let [data {:introduce-word {:type        "sequence"
                                 :connections {:show-first-box-word {:parent :first-word
                                                                     :next   [:empty-big]}}
                                 :data        {:type "sequence"
                                               :data ["empty-big"
                                                      "vaca-this-is-var"]}}}
          usage-prev :introduce-word
          duplicate-name :empty-big
          new-name :empty-big-copy-0]
      (let [actual-result (fix-prev-usage data usage-prev duplicate-name new-name)
            expected-result {:introduce-word {:type        "sequence"
                                              :connections {:show-first-box-word {:parent :first-word
                                                                                  :next   [:empty-big-copy-0]}}
                                              :data        {:type "sequence"
                                                            :data ["empty-big"
                                                                   "vaca-this-is-var"]}}}]
        (is (= actual-result expected-result))))))

(deftest test-fix-next-usage
  (testing "test duplicated node child data replacement"
    (let [usage-data {:type        "action"
                      :connections {:empty-big {:parent :introduce-word
                                                :next   [:empty-small]}}
                      :data        {:type "action"}}
          duplicate-name :empty-big
          new-duplicate-name :empty-big-copy-0]
      (let [actual-result (fix-next-usage usage-data duplicate-name new-duplicate-name)
            expected-result {:type        "action"
                             :connections {:empty-big-copy-0 {:parent :introduce-word
                                                              :next   [:empty-small]}}
                             :data        {:type "action"}}]
        (is (= actual-result expected-result))))))

(deftest test-fix-next-usages
  (testing "test duplicated node children replacement"
    (let [data {:vaca-this-is-var {:type        "action"
                                   :connections {:empty-big {:parent :introduce-word
                                                             :next   [:empty-small]}}
                                   :data        {:type "action"}}}
          usages-names [:vaca-this-is-var]
          duplicate-name :empty-big
          new-duplicate-name :empty-big-copy-0]
      (let [actual-result (fix-next-usages data usages-names duplicate-name new-duplicate-name)
            expected-result {:vaca-this-is-var {:type        "action"
                                                :connections {:empty-big-copy-0 {:parent :introduce-word
                                                                                 :next   [:empty-small]}}
                                                :data        {:type "action"}}}]
        (is (= actual-result expected-result))))))

(deftest test-process-duplicates
  (testing "test duplicates copying"
    (let [data {:introduce-word   {:type        "sequence"
                                   :connections {:show-first-box-word {:parent :first-word
                                                                       :next   [:empty-big]}}
                                   :data        {:type "sequence"
                                                 :data ["empty-big"
                                                        "vaca-this-is-var"]}}
                :vaca-word-var    {:type        "action"
                                   :connections {:empty-small {:parent :introduce-word
                                                               :next   [:empty-big]}}
                                   :data        {:type "action"}}

                :empty-big        {:type        "empty"
                                   :connections {:introduce-word {:parent :introduce-word
                                                                  :next   [:vaca-this-is-var]}
                                                 :vaca-word-var  {:parent :introduce-word
                                                                  :next   [:bye-current-box]}}
                                   :data        {:type "empty" :duration 1000}}

                :vaca-this-is-var {:type        "action"
                                   :connections {:empty-big {:parent :introduce-word
                                                             :next   [:empty-small]}}
                                   :data        {:type "action"}}
                :bye-current-box  {:type        "sequence-data"
                                   :connections {:empty-big {:parent :first-word
                                                             :next   [:bye-current-box-0]}}
                                   :data        []}}]
      (let [actual-result (process-duplicates data)
            expected-result {:introduce-word   {:type        "sequence"
                                                :connections {:show-first-box-word {:parent :first-word
                                                                                    :next   [:empty-big-copy-0]}}
                                                :data        {:type "sequence"
                                                              :data ["empty-big"
                                                                     "vaca-this-is-var"]}}
                             :empty-big-copy-0 {:type        "empty"
                                                :original    :empty-big
                                                :connections {:introduce-word {:parent :introduce-word
                                                                               :next   [:vaca-this-is-var]}}
                                                :data        {:type "empty" :duration 1000}}
                             :vaca-this-is-var {:type        "action"
                                                :connections {:empty-big-copy-0 {:parent :introduce-word
                                                                                 :next   [:empty-small]}}
                                                :data        {:type "action"}}

                             :vaca-word-var    {:type        "action"
                                                :connections {:empty-small {:parent :introduce-word
                                                                            :next   [:empty-big-copy-1]}}
                                                :data        {:type "action"}}
                             :empty-big-copy-1 {:type        "empty"
                                                :original    :empty-big
                                                :connections {:vaca-word-var {:parent :introduce-word
                                                                              :next   [:bye-current-box]}}
                                                :data        {:type "empty" :duration 1000}}
                             :bye-current-box  {:type        "sequence-data"
                                                :connections {:empty-big-copy-1 {:parent :first-word
                                                                                 :next   [:bye-current-box-0]}}
                                                :data        []}}]
        (is (= actual-result expected-result)))))

  ;; Should be restored when duplicates processing is fixed for references from copy to copy
  ;; Example: :introduce-word action in english/home scene

  ;(testing "sequence with double duplicates"
  ;  (let [data {:introduce-word    {:type        "sequence"
  ;                                  :connections {:previous-action {:next [:empty-small]}}
  ;                                  :data        {}}
  ;              :empty-small       {:type        "empty"
  ;                                  :connections {:introduce-word    {:next   [:empty-small]
  ;                                                                    :parent :introduce-word}
  ;                                                :empty-small       {:next   [:group-3-times-var]
  ;                                                                    :parent :introduce-word}
  ;                                                :group-3-times-var {:next   [:next-action]
  ;                                                                    :parent :introduce-word}}
  ;                                  :data        {}}
  ;              :group-3-times-var {:type        "action"
  ;                                  :connections {:empty-small {:next   [:empty-small]
  ;                                                              :parent :introduce-word}}
  ;                                  :data        {}}}]
  ;    (let [actual-result (process-duplicates data)
  ;          expected-result {:introduce-word     {:type        "sequence"
  ;                                                :connections {:previous-action {:next [:empty-small-copy-0]}}
  ;                                                :data        {}}
  ;                           :group-3-times-var  {:type        "action"
  ;                                                :connections {:empty-small-copy-1 {:next   [:empty-small-copy-2]
  ;                                                                                   :parent :introduce-word}}
  ;                                                :data        {}}
  ;                           :empty-small-copy-0 {:type        "empty"
  ;                                                :connections {:introduce-word {:next   [:empty-small-copy-1]
  ;                                                                               :parent :introduce-word}}
  ;                                                :data        {}}
  ;                           :empty-small-copy-1 {:type        "empty"
  ;                                                :connections {:empty-small-copy-0 {:next   [:group-3-times-var]
  ;                                                                                   :parent :introduce-word}}
  ;                                                :data        {}}
  ;                           :empty-small-copy-2 {:type        "empty"
  ;                                                :connections {:group-3-times-var {:next   [:next-action]
  ;                                                                                  :parent :introduce-word}}
  ;                                                :data        {}}}]
  ;      (when-not (= actual-result expected-result)
  ;        (println (compare-maps actual-result expected-result)))
  ;      (is (= actual-result expected-result)))))
  )
