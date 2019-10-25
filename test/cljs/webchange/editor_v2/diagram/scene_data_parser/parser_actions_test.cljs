(ns webchange.editor-v2.diagram.scene-data-parser.parser-actions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [compare-maps]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-actions :refer [get-chain-entries
                                                                          merge-actions
                                                                          get-action-data
                                                                          parse-actions-chain
                                                                          seq-intersection]]))

(deftest test-get-action-data-test-var-scalar
  (testing ":click-on-box1"
    (let [action-name :click-on-box1
          action-data {:type     "test-var-scalar"
                       :var-name "current-box"
                       :value    "box1"
                       :success  "first-word"
                       :fail     "pick-wrong"}
          parent-action nil
          next-action nil
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:click-on-box1 {:type        "test-var-scalar"
                              :connections {:previous-action {:handlers {:success [:first-word]
                                                                         :fail    [:pick-wrong]}}}
                              :data        action-data}})))))

(deftest test-get-action-data-sequence
  (testing ":first-word"
    (let [action-name :first-word
          action-data {:type       "sequence"
                       :data       ["show-first-box-word"
                                    "introduce-word"
                                    "bye-current-box"
                                    "set-current-box2"
                                    "senora-vaca-audio-touch-second-box"]
                       :unique-tag "box"}
          parent-action nil
          next-action nil
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:first-word {:type        "sequence"
                           :connections {:previous-action {:handlers {:next [:show-first-box-word]}}}
                           :data        action-data}}))))
  (testing ":introduce-word"
    (let [action-name :introduce-word
          action-data {:type "sequence"
                       :data ["empty-big"
                              "vaca-this-is-var"
                              "empty-small"
                              "vaca-can-you-say"
                              "vaca-question-var"
                              "empty-small"
                              "vaca-word-var"
                              "empty-big"]}
          parent-action :first-word
          next-action nil
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:introduce-word {:type        "sequence"
                               :connections {:previous-action {:parent   :first-word
                                                               :handlers {:next [:empty-big]}}}
                               :data        action-data}})))))

(deftest test-get-action-data-sequence-data
  (testing ":bye-current-box"
    (let [action-name :bye-current-box
          action-data {:type "sequence-data"
                       :data [{:type "parallel"
                               :data [{:type     "animation" :id "jump"
                                       :from-var [{:var-name "current-box" :action-property "target"}]}
                                      {:type     "transition" :to {:y -100 :duration 2}
                                       :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                  {:var-name "current-position-x" :action-property "to.x"}]}]}
                              {:type     "state" :id "default"
                               :from-var [{:var-name "current-box" :action-property "target"}]}]}
          parent-action :first-word
          next-action :set-current-box2
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:bye-current-box     {:type        "sequence-data"
                                    :connections {:previous-action {:parent   :first-word
                                                                    :handlers {:next [:bye-current-box-0]}}}
                                    :data        action-data}

              :bye-current-box-0   {:type        "parallel"
                                    :connections {:bye-current-box {:parent   :bye-current-box
                                                                    :handlers {:next [:bye-current-box-0-0
                                                                                      :bye-current-box-0-1]}}}
                                    :data        {:type "parallel"
                                                  :data [{:type     "animation" :id "jump"
                                                          :from-var [{:var-name "current-box" :action-property "target"}]}
                                                         {:type     "transition" :to {:y -100 :duration 2}
                                                          :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                                     {:var-name "current-position-x" :action-property "to.x"}]}]}}

              :bye-current-box-0-0 {:type        "animation"
                                    :connections {:bye-current-box-0 {:parent   :bye-current-box-0
                                                                      :handlers {:next [:bye-current-box-1]}}}
                                    :data        {:type     "animation" :id "jump"
                                                  :from-var [{:var-name "current-box" :action-property "target"}]}}

              :bye-current-box-0-1 {:type        "transition"
                                    :connections {:bye-current-box-0 {:parent   :bye-current-box-0
                                                                      :handlers {:next [:bye-current-box-1]}}}

                                    :data        {:type     "transition" :to {:y -100 :duration 2}
                                                  :from-var [{:var-name "current-box" :action-property "transition-id"}
                                                             {:var-name "current-position-x" :action-property "to.x"}]}}

              :bye-current-box-1   {:type        "state"

                                    :connections {:bye-current-box-0 {:parent   :bye-current-box
                                                                      :handlers {:next [:set-current-box2]}}}

                                    :data        {:type     "state" :id "default"
                                                  :from-var [{:var-name "current-box" :action-property "target"}]}}})))))

(deftest test-get-action-data-parallel
  (testing ":show-first-box-word"
    (let [action-name :show-first-box-word
          action-data {:type "parallel"
                       :data [{:type "animation" :target "box1" :id "wood" :loop false}
                              {:type     "set-skin" :target "box1"
                               :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                              {:type "copy-variable" :var-name "current-word" :from "item-1"}
                              {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}
          parent-action :first-word
          next-action :introduce-word
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:show-first-box-word   {:type        "parallel"
                                      :connections {:previous-action {:parent   :first-word
                                                                      :handlers {:next [:show-first-box-word-0
                                                                                        :show-first-box-word-1
                                                                                        :show-first-box-word-2
                                                                                        :show-first-box-word-3]}}}
                                      :data        action-data}
              :show-first-box-word-0 {:type        "animation"
                                      :connections {:show-first-box-word {:parent   :show-first-box-word
                                                                          :handlers {:next [:introduce-word]}}}
                                      :data        {:type "animation" :target "box1" :id "wood" :loop false}}
              :show-first-box-word-1 {:type        "set-skin"
                                      :connections {:show-first-box-word {:parent   :show-first-box-word
                                                                          :handlers {:next [:introduce-word]}}}
                                      :data        {:type     "set-skin" :target "box1"
                                                    :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}}
              :show-first-box-word-2 {:type        "copy-variable"
                                      :connections {:show-first-box-word {:parent   :show-first-box-word
                                                                          :handlers {:next [:introduce-word]}}}
                                      :data        {:type "copy-variable" :var-name "current-word" :from "item-1"}}
              :show-first-box-word-3 {:type        "add-animation"
                                      :connections {:show-first-box-word {:parent   :show-first-box-word
                                                                          :handlers {:next [:introduce-word]}}}
                                      :data        {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}}}))))
  (testing ":set-current-box2"
    (let [action-name :set-current-box2
          action-data {:type "parallel"
                       :data [{:type "set-variable" :var-name "current-box" :var-value "box2"}
                              {:type "set-variable" :var-name "current-position-x" :var-value 850}]}
          parent-action :first-word
          next-action :senora-vaca-audio-touch-second-box
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:set-current-box2   {:type        "parallel"
                                   :connections {:previous-action {:parent   :first-word
                                                                   :handlers {:next [:set-current-box2-0
                                                                                     :set-current-box2-1]}}}
                                   :data        action-data}
              :set-current-box2-0 {:type        "set-variable"
                                   :connections {:set-current-box2 {:parent   :set-current-box2
                                                                    :handlers {:next [:senora-vaca-audio-touch-second-box]}}}
                                   :data        {:type "set-variable" :var-name "current-box" :var-value "box2"}}
              :set-current-box2-1 {:type        "set-variable"
                                   :connections {:set-current-box2 {:parent   :set-current-box2
                                                                    :handlers {:next [:senora-vaca-audio-touch-second-box]}}}
                                   :data        {:type "set-variable" :var-name "current-position-x" :var-value 850}}
              })))))

(deftest test-get-action-data-animation
  (testing ":show-first-box-word-0"
    (let [action-name :show-first-box-word-0
          action-data {:type "animation" :target "box1" :id "wood" :loop false}
          parent-action :show-first-box-word
          next-action :introduce-word
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:show-first-box-word-0 {:type        "animation"
                                      :connections {:previous-action {:parent   :show-first-box-word
                                                                      :handlers {:next [:introduce-word]}}}
                                      :data        action-data}})))))

(deftest test-get-action-data-set-skin
  (testing ":show-first-box-word-1"
    (let [action-name :show-first-box-word-1
          action-data {:type     "set-skin" :target "box1"
                       :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
          parent-action :show-first-box-word
          next-action :introduce-word
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:show-first-box-word-1 {:type        "set-skin"
                                      :connections {:previous-action {:parent   :show-first-box-word
                                                                      :handlers {:next [:introduce-word]}}}
                                      :data        action-data}})))))

(deftest test-get-action-data-copy-variable
  (testing ":show-first-box-word-2"
    (let [action-name :show-first-box-word-2
          action-data {:type "copy-variable" :var-name "current-word" :from "item-1"}
          parent-action :show-first-box-word
          next-action :introduce-word
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:show-first-box-word-2 {:type        "copy-variable"
                                      :connections {:previous-action {:parent   :show-first-box-word
                                                                      :handlers {:next [:introduce-word]}}}
                                      :data        action-data}})))))

(deftest test-get-action-data-add-animation
  (testing ":show-first-box-word-3"
    (let [action-name :show-first-box-word-3
          action-data {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}
          parent-action :show-first-box-word
          next-action :introduce-word
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:show-first-box-word-3 {:type        "add-animation"
                                      :connections {:previous-action {:parent   :show-first-box-word
                                                                      :handlers {:next [:introduce-word]}}}
                                      :data        action-data}})))))

(deftest test-get-action-data-add-action
  (testing ":vaca-this-is-var"
    (let [action-name :vaca-this-is-var
          action-data {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-this-is-action"}]}
          parent-action :introduce-word
          next-action :empty-small-copy-1
          prev-action :previous-action]
      (is (= (get-action-data {:action-name   action-name
                               :action-data   action-data
                               :parent-action parent-action
                               :next-action   next-action
                               :prev-action   prev-action})
             {:vaca-this-is-var {:type        "action"
                                 :connections {:previous-action {:parent   :introduce-word
                                                                 :handlers {:next [:empty-small-copy-1]}}}
                                 :data        action-data}}))))

  (deftest test-get-action-data-animation-sequence
    (testing ":senora-vaca-audio-touch-second-box"
      (let [action-name :senora-vaca-audio-touch-second-box
            action-data {:type     "animation-sequence",
                         :target   "senoravaca",
                         :track    1,
                         :offset   52.453,
                         :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                         :data     [{:start 52.6, :end 53.467, :duration 0.867, :anim "talk"}
                                    {:start 54.36, :end 56.307, :duration 1.947, :anim "talk"}
                                    {:start 56.987, :end 59.173, :duration 2.186, :anim "talk"}],
                         :start    52.453,
                         :duration 6.987}
            parent-action :first-word
            next-action nil
            prev-action :previous-action]
        (is (= (get-action-data {:action-name   action-name
                                 :action-data   action-data
                                 :parent-action parent-action
                                 :next-action   next-action
                                 :prev-action   prev-action})
               {:senora-vaca-audio-touch-second-box {:type        "animation-sequence"
                                                     :connections {:previous-action {:parent   :first-word
                                                                                     :handlers {}}}
                                                     :data        action-data}}))))
    (testing ":vaca-can-you-say"
      (let [action-name :vaca-can-you-say
            action-data {:type     "animation-sequence",
                         :target   "senoravaca",
                         :track    1,
                         :offset   20.28,
                         :audio    "/raw/audio/english/l1/a1/vaca.m4a",
                         :data     [{:start 20.363, :end 20.98, :duration 0.617, :anim "talk"}],
                         :start    20.28,
                         :duration 0.813}
            parent-action :introduce-word
            next-action :vaca-question-var]
        (is (= (get-action-data {:action-name   action-name
                                 :action-data   action-data
                                 :parent-action parent-action
                                 :next-action   next-action
                                 :prev-action   nil})
               {:vaca-can-you-say {:type     "animation-sequence"
                                   :parent   :introduce-word
                                   :handlers {:next [:vaca-question-var]}
                                   :data     action-data}}))))))

;; ---

(deftest test-parse-actions-chain-parallel
  (testing "simple parallel"
    (let [actions-data {:simple-parallel {:type "parallel"
                                          :data [{:type "action"}
                                                 {:type "action"}]}}
          start-node-name :simple-parallel
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action :next-action
          prev-action :previous-action]
      (is (= (parse-actions-chain actions-data {:action-name   start-node-name
                                                :action-data   start-node-data
                                                :parent-action parent-action
                                                :next-action   next-action
                                                :prev-action   prev-action})
             {:simple-parallel   {:type        "parallel"
                                  :data        {:type "parallel"
                                                :data [{:type "action"}
                                                       {:type "action"}]}
                                  :connections {:previous-action {:handlers {:next [:simple-parallel-0
                                                                                    :simple-parallel-1]}}}}
              :simple-parallel-0 {:type        "action"
                                  :data        {:type "action"}
                                  :connections {:simple-parallel {:parent   :simple-parallel
                                                                  :handlers {:next [:next-action]}}}
                                  }
              :simple-parallel-1 {:type        "action"
                                  :data        {:type "action"}
                                  :connections {:simple-parallel {:parent   :simple-parallel
                                                                  :handlers {:next [:next-action]}}}}}))
      ))
  (testing "sequence with double duplicates"
    (let [actions-data {:introduce-word    {:type "sequence"
                                            :data ["empty-small"
                                                   "empty-small"
                                                   "group-3-times-var"
                                                   "empty-small"]}
                        :group-3-times-var {:type "action"}
                        :empty-small       {:type "empty"}}
          start-node-name :introduce-word
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action :next-action
          prev-action :previous-action]
      (let [actual-result (parse-actions-chain actions-data {:action-name   start-node-name
                                                             :action-data   start-node-data
                                                             :parent-action parent-action
                                                             :next-action   next-action
                                                             :prev-action   prev-action})
            expected-result {:introduce-word    {:type        "sequence"
                                                 :connections {:previous-action {:handlers {:next [:empty-small]}}}
                                                 :data        (:introduce-word actions-data)}
                             :empty-small       {:type        "empty"
                                                 :connections {:introduce-word    {:handlers {:next [:empty-small]}
                                                                                   :parent   :introduce-word}
                                                               :empty-small       {:handlers {:next [:group-3-times-var]}
                                                                                   :parent   :introduce-word}
                                                               :group-3-times-var {:handlers {:next [:next-action]}
                                                                                   :parent   :introduce-word}}
                                                 :data        (:empty-small actions-data)}
                             :group-3-times-var {:type        "action"
                                                 :connections {:empty-small {:handlers {:next [:empty-small]}
                                                                             :parent   :introduce-word}}
                                                 :data        (:group-3-times-var actions-data)}}]
        (is (= actual-result expected-result))))))

(deftest test-parse-actions-chain-sequence
  (testing "simple sequence"
    (let [actions-data {:simple-seq   {:type "sequence"
                                       :data ["sub-action-0"
                                              "sub-action-1"
                                              "sub-action-2"]}
                        :sub-action-0 {:type "action"}
                        :sub-action-1 {:type "action"}
                        :sub-action-2 {:type "action"}}
          start-node-name :simple-seq
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action :next-action
          prev-action :previous-action]
      (is (= (parse-actions-chain actions-data {:action-name   start-node-name
                                                :action-data   start-node-data
                                                :parent-action parent-action
                                                :next-action   next-action
                                                :prev-action   prev-action})
             {:simple-seq   {:type        "sequence"
                             :data        {:type "sequence"
                                           :data ["sub-action-0"
                                                  "sub-action-1"
                                                  "sub-action-2"]}
                             :connections {:previous-action {:handlers {:next [:sub-action-0]}}}}
              :sub-action-0 {:type        "action"
                             :data        {:type "action"}
                             :connections {:simple-seq {:parent   :simple-seq
                                                        :handlers {:next [:sub-action-1]}}}}
              :sub-action-1 {:type        "action"
                             :data        {:type "action"}
                             :connections {:sub-action-0 {:parent   :simple-seq
                                                          :handlers {:next [:sub-action-2]}}}}
              :sub-action-2 {:type        "action"
                             :data        {:type "action"}
                             :connections {:sub-action-1 {:parent   :simple-seq
                                                          :handlers {:next [:next-action]}}}}}))
      ))

  (testing "sequence in sequence"
    (let [actions-data {:action-seq-1   {:type "sequence"
                                         :data ["action-seq-1-1"
                                                "action-seq-2"
                                                "action-seq-1-2"]}
                        :action-seq-2   {:type "sequence"
                                         :data ["action-seq-2-1"
                                                "action-seq-2-2"]}
                        :action-seq-1-1 {:type "empty"}
                        :action-seq-1-2 {:type "empty"}
                        :action-seq-2-1 {:type "empty"}
                        :action-seq-2-2 {:type "empty"}}
          start-node-name :action-seq-1
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action :next-action
          prev-action :previous-action]
      (let [actual-result (parse-actions-chain actions-data {:action-name   start-node-name
                                                             :action-data   start-node-data
                                                             :parent-action parent-action
                                                             :next-action   next-action
                                                             :prev-action   prev-action})
            expected-result {:action-seq-1   {:type        "sequence"
                                              :connections {:previous-action {:handlers {:next [:action-seq-1-1]}}}
                                              :data        {:type "sequence"
                                                            :data ["action-seq-1-1"
                                                                   "action-seq-2"
                                                                   "action-seq-1-2"]}}
                             :action-seq-2   {:type        "sequence"
                                              :connections {:action-seq-1-1 {:handlers {:next [:action-seq-2-1]}
                                                                             :parent   :action-seq-1}}
                                              :data        {:type "sequence"
                                                            :data ["action-seq-2-1"
                                                                   "action-seq-2-2"]}}
                             :action-seq-1-1 {:type        "empty"
                                              :connections {:action-seq-1 {:handlers {:next [:action-seq-2]}
                                                                           :parent   :action-seq-1}}
                                              :data        {:type "empty"}}
                             :action-seq-1-2 {:type        "empty"
                                              :connections {:action-seq-2-2 {:handlers {:next [:next-action]}
                                                                             :parent   :action-seq-1}}
                                              :data        {:type "empty"}}
                             :action-seq-2-1 {:type        "empty"
                                              :connections {:action-seq-2 {:handlers {:next [:action-seq-2-2]}
                                                                           :parent   :action-seq-2}}
                                              :data        {:type "empty"}}
                             :action-seq-2-2 {:type        "empty"
                                              :connections {:action-seq-2-1 {:handlers {:next [:action-seq-1-2]}
                                                                             :parent   :action-seq-2}}
                                              :data        {:type "empty"}}}]
        ; (is (= actual-result expected-result))

        ;; should be fixed for :action-seq-1-2
        ;; actual :connections {:action-seq-2
        ;; expected :connections {:action-seq-2-2
        ;; (:action-seq-2-2 is last item of sub-sequence :action-seq-2)
        ))))


(deftest test-parse-actions-chain-test-var-scalar
  (testing "simple test-var-scalar"
    (let [actions-data {:simple-test    {:type     "test-var-scalar"
                                         :var-name "var-name"
                                         :value    "value"
                                         :success  "success-action"
                                         :fail     "fail-action"}
                        :success-action {:type "action"}
                        :fail-action    {:type "action"}}
          start-node-name :simple-test
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action nil
          prev-action :previous-action]
      (is (= (parse-actions-chain actions-data {:action-name   start-node-name
                                                :action-data   start-node-data
                                                :parent-action parent-action
                                                :next-action   next-action
                                                :prev-action   prev-action})
             {:simple-test    {:type        "test-var-scalar"
                               :data        {:type     "test-var-scalar"
                                             :var-name "var-name"
                                             :value    "value"
                                             :success  "success-action"
                                             :fail     "fail-action"}
                               :connections {:previous-action {:handlers {:success [:success-action]
                                                                          :fail    [:fail-action]}}}}
              :success-action {:type        "action"
                               :data        {:type "action"}
                               :connections {:simple-test {:handlers {}}}}
              :fail-action    {:type        "action"
                               :data        {:type "action"}
                               :connections {:simple-test {:handlers {}}}}})))))

(deftest test-merge-actions-map
  (testing "seq-intersection"
    (is (= (seq-intersection [1 2 3] [2 3 4])
           [2 3])))

  (testing "simple-merge"
    (let [map-1 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-1]}}}}}
          map-2 {:action-2 {:type        "action"
                            :connections {:connect-2 {:handlers {:next [:next-2]}}}}}]
      (is (= (merge-actions map-1 map-2)
             {:action-1 {:type        "action"
                         :connections {:connect-1 {:handlers {:next [:next-1]}}}}
              :action-2 {:type        "action"
                         :connections {:connect-2 {:handlers {:next [:next-2]}}}}}))))

  (testing "simple-merge"
    (let [map-1 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-1]}}}}}
          map-2 {:action-1 {:type        "action"
                            :connections {:connect-2 {:handlers {:next [:next-2]}}}}}]
      (is (= (merge-actions map-1 map-2)
             {:action-1 {:type        "action"
                         :connections {:connect-1 {:handlers {:next [:next-1]}}
                                       :connect-2 {:handlers {:next [:next-2]}}}}}))))
  )

(deftest test-get-chain-entries
  (testing "getting entries"
    (let [objects-data {:door        {:type        "object"
                                      :connections {:root {:handlers {}}}}
                        :senora-vaca {:type        "object"
                                      :connections {:root {:handlers {:next [:restart]}}}}
                        :box1        {:type        "object"
                                      :connections {:root {:handlers {:next [:click-on-box1
                                                                             :click-on-box2]}}}}}]
      (let [actual-result (get-chain-entries objects-data)
            expected-result [[:restart :senora-vaca]
                             [:click-on-box1 :box1]
                             [:click-on-box2 :box1]]]
        (is (= actual-result expected-result))))))
