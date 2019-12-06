(ns webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-actions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-actions :refer [merge-actions
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
                              :data        {:type     "test-var-scalar"
                                            :var-name "current-box"
                                            :value    "box1"
                                            :success  "first-word"
                                            :fail     "pick-wrong"}
                              :path        [:click-on-box1]
                              :connections {:previous-action {:handlers {:success [:first-word]
                                                                         :fail    [:pick-wrong]}}}}})))))

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
                           :data        {:type       "sequence"
                                         :data       ["show-first-box-word"
                                                      "introduce-word"
                                                      "bye-current-box"
                                                      "set-current-box2"
                                                      "senora-vaca-audio-touch-second-box"]
                                         :unique-tag "box"}
                           :path        [:first-word]
                           :connections {:previous-action {:handlers {:next [:show-first-box-word]}}}}}))))
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
                               :data        {:type "sequence"
                                             :data ["empty-big"
                                                    "vaca-this-is-var"
                                                    "empty-small"
                                                    "vaca-can-you-say"
                                                    "vaca-question-var"
                                                    "empty-small"
                                                    "vaca-word-var"
                                                    "empty-big"]}
                               :path        [:introduce-word]
                               :connections {:previous-action {:handlers {:next [:empty-big]}
                                                               :parent   :first-word}}}})))))

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
                                    :data        {:type "sequence-data"
                                                  :data [{:type "parallel"
                                                          :data [{:type     "animation"
                                                                  :id       "jump"
                                                                  :from-var [{:var-name        "current-box"
                                                                              :action-property "target"}]}
                                                                 {:type     "transition"
                                                                  :to       {:y        -100
                                                                             :duration 2}
                                                                  :from-var [{:var-name        "current-box"
                                                                              :action-property "transition-id"}
                                                                             {:var-name        "current-position-x"
                                                                              :action-property "to.x"}]}]}
                                                         {:type     "state"
                                                          :id       "default"
                                                          :from-var [{:var-name        "current-box"
                                                                      :action-property "target"}]}]}
                                    :path        [:bye-current-box]
                                    :connections {:previous-action {:handlers {:next [:bye-current-box-0]}
                                                                    :parent   :first-word}}}
              :bye-current-box-0   {:type        "parallel"
                                    :data        {:type "parallel"
                                                  :data [{:type     "animation"
                                                          :id       "jump"
                                                          :from-var [{:var-name        "current-box"
                                                                      :action-property "target"}]}
                                                         {:type     "transition"
                                                          :to       {:y        -100
                                                                     :duration 2}
                                                          :from-var [{:var-name        "current-box"
                                                                      :action-property "transition-id"}
                                                                     {:var-name        "current-position-x"
                                                                      :action-property "to.x"}]}]}
                                    :path        [:bye-current-box 0]
                                    :connections {:bye-current-box {:handlers {:next [:bye-current-box-0-0 :bye-current-box-0-1]}
                                                                    :parent   :bye-current-box}}}
              :bye-current-box-0-0 {:type        "animation"
                                    :data        {:type     "animation"
                                                  :id       "jump"
                                                  :from-var [{:var-name        "current-box"
                                                              :action-property "target"}]}
                                    :path        [:bye-current-box 0 0]
                                    :connections {:bye-current-box-0 {:handlers {:next [:bye-current-box-1]}
                                                                      :parent   :bye-current-box-0}}}
              :bye-current-box-0-1 {:type        "transition"
                                    :data        {:type     "transition"
                                                  :to       {:y        -100
                                                             :duration 2}
                                                  :from-var [{:var-name        "current-box"
                                                              :action-property "transition-id"}
                                                             {:var-name        "current-position-x"
                                                              :action-property "to.x"}]}
                                    :path        [:bye-current-box 0 1]
                                    :connections {:bye-current-box-0 {:handlers {:next [:bye-current-box-1]}
                                                                      :parent   :bye-current-box-0}}}
              :bye-current-box-1   {:type        "state"
                                    :data        {:type     "state"
                                                  :id       "default"
                                                  :from-var [{:var-name        "current-box"
                                                              :action-property "target"}]}
                                    :path        [:bye-current-box 1]
                                    :connections {:bye-current-box-0 {:handlers {:next [:set-current-box2]}
                                                                      :parent   :bye-current-box}}}})))))

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
                                      :data        {:type "parallel"
                                                    :data [{:type   "animation"
                                                            :target "box1"
                                                            :id     "wood"
                                                            :loop   false}
                                                           {:type     "set-skin"
                                                            :target   "box1"
                                                            :from-var [{:var-name        "item-1"
                                                                        :action-property "skin"
                                                                        :var-property    "skin"}]}
                                                           {:type     "copy-variable"
                                                            :var-name "current-word"
                                                            :from     "item-1"}
                                                           {:type   "add-animation"
                                                            :target "box1"
                                                            :id     "idle_fly1"
                                                            :loop   true}]}
                                      :path        [:show-first-box-word]
                                      :connections {:previous-action {:handlers {:next [:show-first-box-word-0
                                                                                        :show-first-box-word-1
                                                                                        :show-first-box-word-2
                                                                                        :show-first-box-word-3]}
                                                                      :parent   :first-word}}}
              :show-first-box-word-0 {:type        "animation"
                                      :data        {:type   "animation"
                                                    :target "box1"
                                                    :id     "wood"
                                                    :loop   false}
                                      :path        [:show-first-box-word 0]
                                      :connections {:show-first-box-word {:handlers {:next [:introduce-word]}
                                                                          :parent   :show-first-box-word}}}
              :show-first-box-word-1 {:type        "set-skin"
                                      :data        {:type     "set-skin"
                                                    :target   "box1"
                                                    :from-var [{:var-name        "item-1"
                                                                :action-property "skin"
                                                                :var-property    "skin"}]}
                                      :path        [:show-first-box-word 1]
                                      :connections {:show-first-box-word {:handlers {:next [:introduce-word]}
                                                                          :parent   :show-first-box-word}}}
              :show-first-box-word-2 {:type        "copy-variable"
                                      :data        {:type     "copy-variable"
                                                    :var-name "current-word"
                                                    :from     "item-1"}
                                      :path        [:show-first-box-word 2]
                                      :connections {:show-first-box-word {:handlers {:next [:introduce-word]}
                                                                          :parent   :show-first-box-word}}}
              :show-first-box-word-3 {:type        "add-animation"
                                      :data        {:type   "add-animation"
                                                    :target "box1"
                                                    :id     "idle_fly1"
                                                    :loop   true}
                                      :path        [:show-first-box-word 3]
                                      :connections {:show-first-box-word {:handlers {:next [:introduce-word]}
                                                                          :parent   :show-first-box-word}}}}))))
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
                                   :data        {:type "parallel"
                                                 :data [{:type      "set-variable"
                                                         :var-name  "current-box"
                                                         :var-value "box2"}
                                                        {:type      "set-variable"
                                                         :var-name  "current-position-x"
                                                         :var-value 850}]}
                                   :path        [:set-current-box2]
                                   :connections {:previous-action {:handlers {:next [:set-current-box2-0 :set-current-box2-1]}
                                                                   :parent   :first-word}}}
              :set-current-box2-0 {:type        "set-variable"
                                   :data        {:type      "set-variable"
                                                 :var-name  "current-box"
                                                 :var-value "box2"}
                                   :path        [:set-current-box2 0]
                                   :connections {:set-current-box2 {:handlers {:next [:senora-vaca-audio-touch-second-box]}
                                                                    :parent   :set-current-box2}}}
              :set-current-box2-1 {:type        "set-variable"
                                   :data        {:type      "set-variable"
                                                 :var-name  "current-position-x"
                                                 :var-value 850}
                                   :path        [:set-current-box2 1]
                                   :connections {:set-current-box2 {:handlers {:next [:senora-vaca-audio-touch-second-box]}
                                                                    :parent   :set-current-box2}}}})))))

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
                                      :data        {:type   "animation"
                                                    :target "box1"
                                                    :id     "wood"
                                                    :loop   false}
                                      :path        [:show-first-box-word-0]
                                      :connections {:previous-action {:handlers {:next [:introduce-word]}
                                                                      :parent   :show-first-box-word}}}})))))

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
                                      :data        {:type     "set-skin"
                                                    :target   "box1"
                                                    :from-var [{:var-name        "item-1"
                                                                :action-property "skin"
                                                                :var-property    "skin"}]}
                                      :path        [:show-first-box-word-1]
                                      :connections {:previous-action {:handlers {:next [:introduce-word]}
                                                                      :parent   :show-first-box-word}}}})))))

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
                                      :data        {:type     "copy-variable"
                                                    :var-name "current-word"
                                                    :from     "item-1"}
                                      :path        [:show-first-box-word-2]
                                      :connections {:previous-action {:handlers {:next [:introduce-word]}
                                                                      :parent   :show-first-box-word}}}})))))

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
                                      :data        {:type   "add-animation"
                                                    :target "box1"
                                                    :id     "idle_fly1"
                                                    :loop   true}
                                      :path        [:show-first-box-word-3]
                                      :connections {:previous-action {:handlers {:next [:introduce-word]}
                                                                      :parent   :show-first-box-word}}}})))))

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
                                 :data        {:type     "action"
                                               :from-var [{:var-name     "current-word"
                                                           :var-property "home-vaca-this-is-action"}]}
                                 :path        [:vaca-this-is-var]
                                 :connections {:previous-action {:handlers {:next [:empty-small-copy-1]}
                                                                 :parent   :introduce-word}}}})))))

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
                                                   :data        {:type     "animation-sequence"
                                                                 :target   "senoravaca"
                                                                 :track    1
                                                                 :offset   52.453
                                                                 :audio    "/raw/audio/english/l1/a1/vaca.m4a"
                                                                 :data     [{:start 52.6, :end 53.467, :duration 0.867, :anim "talk"}
                                                                            {:start 54.36, :end 56.307, :duration 1.947, :anim "talk"}
                                                                            {:start 56.987, :end 59.173, :duration 2.186, :anim "talk"}]
                                                                 :start    52.453
                                                                 :duration 6.987}
                                                   :path        [:senora-vaca-audio-touch-second-box]
                                                   :connections {:previous-action {:handlers {}
                                                                                   :parent   :first-word}}}}))))
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
          prev-action :prev-action
          next-action :vaca-question-var]
      (let [actual-result (get-action-data {:action-name   action-name
                                            :action-data   action-data
                                            :parent-action parent-action
                                            :next-action   next-action
                                            :prev-action   prev-action})
            expected-result {:vaca-can-you-say {:type        "animation-sequence"
                                                :data        {:type     "animation-sequence"
                                                              :target   "senoravaca"
                                                              :track    1
                                                              :offset   20.28
                                                              :audio    "/raw/audio/english/l1/a1/vaca.m4a"
                                                              :data     [{:start    20.363
                                                                          :end      20.98
                                                                          :duration 0.617
                                                                          :anim     "talk"}]
                                                              :start    20.28
                                                              :duration 0.813}
                                                :path        [:vaca-can-you-say]
                                                :connections {:prev-action {:handlers {:next [:vaca-question-var]}
                                                                            :parent   :introduce-word}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))

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
                                                :data [{:type "action"} {:type "action"}]}
                                  :path        [:simple-parallel]
                                  :connections {:previous-action {:handlers {:next [:simple-parallel-0
                                                                                    :simple-parallel-1]}}}}
              :simple-parallel-0 {:type        "action"
                                  :data        {:type "action"}
                                  :path        [:simple-parallel 0]
                                  :connections {:simple-parallel {:handlers {:next [:next-action]}
                                                                  :parent   :simple-parallel}}}
              :simple-parallel-1 {:type        "action"
                                  :data        {:type "action"}
                                  :path        [:simple-parallel 1]
                                  :connections {:simple-parallel {:handlers {:next [:next-action]}
                                                                  :parent   :simple-parallel}}}}))))
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
                                                 :data        {:type "sequence"
                                                               :data ["empty-small"
                                                                      "empty-small"
                                                                      "group-3-times-var"
                                                                      "empty-small"]}
                                                 :path        [:introduce-word]
                                                 :connections {:previous-action {:handlers {:next [:empty-small]}}}}
                             :empty-small       {:type        "empty"
                                                 :data        {:type "empty"}
                                                 :path        [:empty-small]
                                                 :connections {:introduce-word    {:handlers {:next [:empty-small]}
                                                                                   :parent   :introduce-word}
                                                               :empty-small       {:handlers {:next [:group-3-times-var]}
                                                                                   :parent   :introduce-word}
                                                               :group-3-times-var {:handlers {:next [:next-action]}
                                                                                   :parent   :introduce-word}}}
                             :group-3-times-var {:type        "action"
                                                 :data        {:type "action"}
                                                 :path        [:group-3-times-var]
                                                 :connections {:empty-small {:handlers {:next [:empty-small]}
                                                                             :parent   :introduce-word}}}}]
        (is (= actual-result expected-result)))))

  (testing "children after parallel"
    (let [actions-data {:a {:type "sequence"
                            :data ["b" "c"]}
                        :b {:type "parallel"
                            :data [{:type "action"}
                                   {:type "action"}]}
                        :c {:type "empty"}}
          start-node-name :a
          start-node-data (get actions-data start-node-name)
          parent-action nil
          next-action :next-action
          prev-action :prev-action]
      (let [actual-result (parse-actions-chain actions-data {:action-name   start-node-name
                                                             :action-data   start-node-data
                                                             :parent-action parent-action
                                                             :next-action   next-action
                                                             :prev-action   prev-action})
            expected-result {:a   {:type        "sequence"
                                   :data        {:type "sequence"
                                                 :data ["b" "c"]}
                                   :path        [:a]
                                   :connections {:prev-action {:handlers {:next [:b]}}}}
                             :b   {:type        "parallel"
                                   :data        {:type "parallel"
                                                 :data [{:type "action"}
                                                        {:type "action"}]}
                                   :path        [:b]
                                   :connections {:a {:handlers {:next [:b-0 :b-1]}
                                                     :parent   :a}}}
                             :b-0 {:type        "action"
                                   :data        {:type "action"}
                                   :path        [:b 0]
                                   :connections {:b {:handlers {:next [:c]}
                                                     :parent   :b}}}
                             :b-1 {:type        "action"
                                   :data        {:type "action"}
                                   :path        [:b 1]
                                   :connections {:b {:handlers {:next [:c]}
                                                     :parent   :b}}}
                             :c   {:type        "empty"
                                   :data        {:type "empty"}
                                   :path        [:c]
                                   :connections {:b-0 {:handlers {:next [:next-action]}
                                                       :parent   :a}
                                                 :b-1 {:handlers {:next [:next-action]}
                                                       :parent   :a}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
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
                             :path        [:simple-seq]
                             :connections {:previous-action {:handlers {:next [:sub-action-0]}}}}
              :sub-action-0 {:type        "action"
                             :data        {:type "action"}
                             :path        [:sub-action-0]
                             :connections {:simple-seq {:handlers {:next [:sub-action-1]}
                                                        :parent   :simple-seq}}}
              :sub-action-1 {:type        "action"
                             :data        {:type "action"}
                             :path        [:sub-action-1]
                             :connections {:sub-action-0 {:handlers {:next [:sub-action-2]}
                                                          :parent   :simple-seq}}}
              :sub-action-2 {:type        "action"
                             :data        {:type "action"}
                             :path        [:sub-action-2]
                             :connections {:sub-action-1 {:handlers {:next [:next-action]}
                                                          :parent   :simple-seq}}}}))))

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
            ;; ToDo: Fix getting :path field
            expected-result {:action-seq-1   {:type        "sequence"
                                              :data        {:type "sequence"
                                                            :data ["action-seq-1-1"
                                                                   "action-seq-2"
                                                                   "action-seq-1-2"]}
                                              :path        [:action-seq-1]
                                              :connections {:previous-action {:handlers {:next [:action-seq-1-1]}}}}
                             :action-seq-1-1 {:type        "empty"
                                              :data        {:type "empty"}
                                              :path        [:action-seq-1-1]
                                              :connections {:action-seq-1 {:handlers {:next [:action-seq-2]}
                                                                           :parent   :action-seq-1}}}
                             :action-seq-2   {:type        "sequence"
                                              :data        {:type "sequence"
                                                            :data ["action-seq-2-1"
                                                                   "action-seq-2-2"]}
                                              :path        [:action-seq-2]
                                              :connections {:action-seq-1-1 {:handlers {:next [:action-seq-2-1]}
                                                                             :parent   :action-seq-1}}}
                             :action-seq-2-1 {:type        "empty"
                                              :data        {:type "empty"}
                                              :path        [:action-seq-2-1]
                                              :connections {:action-seq-2 {:handlers {:next [:action-seq-2-2]}
                                                                           :parent   :action-seq-2}}}
                             :action-seq-2-2 {:type        "empty"
                                              :data        {:type "empty"}
                                              :path        [:action-seq-2-2]
                                              :connections {:action-seq-2-1 {:handlers {:next [:action-seq-1-2]}
                                                                             :parent   :action-seq-2}}}
                             :action-seq-1-2 {:type        "empty"
                                              :data        {:type "empty"}
                                              :path        [:action-seq-1-2]
                                              :connections {:action-seq-2-2 {:handlers {:next [:next-action]}
                                                                             :parent   :action-seq-1}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))

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
                                                                          :fail    [:fail-action]}}}
                               :path        [:simple-test]}
              :success-action {:type        "action"
                               :data        {:type "action"}
                               :connections {:simple-test {:handlers {}}}
                               :path        [:success-action]}
              :fail-action    {:type        "action"
                               :data        {:type "action"}
                               :connections {:simple-test {:handlers {}}}
                               :path        [:fail-action]}})))))

(deftest test-merge-actions-map
  (testing "seq-intersection"
    (is (= (seq-intersection [1 2 3] [2 3 4])
           [2 3])))

  (testing "simple 2 actions merge"
    (let [map-1 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-1]}}}}}
          map-2 {:action-2 {:type        "action"
                            :connections {:connect-2 {:handlers {:next [:next-2]}}}}}]
      (is (= (merge-actions map-1 map-2)
             {:action-1 {:type        "action"
                         :connections {:connect-1 {:handlers {:next [:next-1]}}}}
              :action-2 {:type        "action"
                         :connections {:connect-2 {:handlers {:next [:next-2]}}}}}))))

  (testing "merge different connections of one action"
    (let [map-1 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-1]}}}}}
          map-2 {:action-1 {:type        "action"
                            :connections {:connect-2 {:handlers {:next [:next-2]}}}}}]
      (is (= (merge-actions map-1 map-2)
             {:action-1 {:type        "action"
                         :connections {:connect-1 {:handlers {:next [:next-1]}}
                                       :connect-2 {:handlers {:next [:next-2]}}}}}))))

  (testing "merge similar connections of one action"
    (let [map-1 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-1]}}}}}
          map-2 {:action-1 {:type        "action"
                            :connections {:connect-1 {:handlers {:next [:next-2]}}}}}]
      (is (= (merge-actions map-1 map-2)
             {:action-1 {:type        "action"
                         :connections {:connect-1 {:handlers {:next [:next-1 :next-2]}}}}})))))
