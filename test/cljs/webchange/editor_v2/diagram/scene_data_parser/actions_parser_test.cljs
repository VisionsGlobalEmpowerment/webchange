(ns webchange.editor-v2.diagram.scene-data-parser.actions-parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.scene-data-parser.actions-parser :refer [parse-action]]))

(deftest test-parse-action-test-var-scalar
  (testing ":click-on-box1"
    (let [action-name :click-on-box1
          action-data {:type     "test-var-scalar"
                       :var-name "current-box"
                       :value    "box1"
                       :success  "first-word"
                       :fail     "pick-wrong"}
          parent-action nil
          next-action nil]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:click-on-box1 {:type "test-var-scalar"
                              :next [:first-word
                                     :pick-wrong]
                              :data action-data}})))))

(deftest test-parse-action-sequence
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
          next-action nil]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:first-word {:type "sequence"
                           :next [:show-first-box-word]
                           :data action-data}}))))
  (testing ":introduce-word"
    (let [action-name :introduce-word
          action-data {:type "sequence"
                       :data ["empty-big"
                              "vaca-this-is-var"
                              "empty-small" Z
                              "vaca-can-you-say"
                              "vaca-question-var"
                              "empty-small"
                              "vaca-word-var"
                              "empty-big"]}
          parent-action :first-word
          next-action nil]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:introduce-word {:type   "sequence"
                               :parent :first-word
                               :next   [:empty-big]
                               :data   action-data}})))))

(deftest test-parse-action-sequence-data
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
          next-action :set-current-box2]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:bye-current-box                    {:type   "sequence-data"
                                                   :parent :first-word
                                                   :next   [:bye-current-box-0]}

              :bye-current-box-0                  {:type   "parallel"
                                                   :parent :bye-current-box
                                                   :next   [:bye-current-box-0-0
                                                            :bye-current-box-0-1]}

              :bye-current-box-0-0                {:type   "animation"
                                                   :parent :bye-current-box-0
                                                   :next   [:bye-current-box-1]}

              :bye-current-box-0-1                {:type   "transition"
                                                   :parent :bye-current-box-0
                                                   :next   [:bye-current-box-1]}

              :bye-current-box-1                  {:type   "state"
                                                   :parent :bye-current-box
                                                   :next   [:set-current-box2]}})))))

(deftest test-parse-action-parallel
  (testing ":show-first-box-word"
    (let [action-name :show-first-box-word
          action-data {:type "parallel"
                       :data [{:type "animation" :target "box1" :id "wood" :loop false}
                              {:type     "set-skin" :target "box1"
                               :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                              {:type "copy-variable" :var-name "current-word" :from "item-1"}
                              {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}
          parent-action :first-word
          next-action :introduce-word]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:show-first-box-word   {:type   "parallel"
                                      :parent :first-word
                                      :next   [:show-first-box-word-0
                                               :show-first-box-word-1
                                               :show-first-box-word-2
                                               :show-first-box-word-3]
                                      :data   action-data}
              :show-first-box-word-0 {:type   "animation"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   {:type "animation" :target "box1" :id "wood" :loop false}}
              :show-first-box-word-1 {:type   "set-skin"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   {:type     "set-skin" :target "box1"
                                               :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}}
              :show-first-box-word-2 {:type   "copy-variable"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   {:type "copy-variable" :var-name "current-word" :from "item-1"}}
              :show-first-box-word-3 {:type   "add-animation"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}}}))))
  (testing ":set-current-box2"
    (let [action-name :set-current-box2
          action-data {:type "parallel"
                       :data [{:type "set-variable" :var-name "current-box" :var-value "box2"}
                              {:type "set-variable" :var-name "current-position-x" :var-value 850}]}
          parent-action :first-word
          next-action :senora-vaca-audio-touch-second-box]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:set-current-box2   {:type   "parallel"
                                   :parent :first-word
                                   :next   [:set-current-box2-0
                                            :set-current-box2-1]
                                   :data   action-data}
              :set-current-box2-0 {:type   "set-variable"
                                   :parent :set-current-box2
                                   :next   [:senora-vaca-audio-touch-second-box]
                                   :data   {:type "set-variable" :var-name "current-box" :var-value "box2"}}
              :set-current-box2-1 {:type   "set-variable"
                                   :parent :set-current-box2
                                   :next   [:senora-vaca-audio-touch-second-box]
                                   :data   {:type "set-variable" :var-name "current-position-x" :var-value 850}}
              })))))

(deftest test-parse-action-animation
  (testing ":show-first-box-word-0"
    (let [action-name :show-first-box-word-0
          action-data {:type "animation" :target "box1" :id "wood" :loop false}
          parent-action :show-first-box-word
          next-action :introduce-word]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:show-first-box-word-0 {:type   "animation"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   action-data}})))))

(deftest test-parse-action-set-skin
  (testing ":show-first-box-word-1"
    (let [action-name :show-first-box-word-1
          action-data {:type     "set-skin" :target "box1"
                       :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
          parent-action :show-first-box-word
          next-action :introduce-word]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:show-first-box-word-1 {:type   "set-skin"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   action-data}})))))

(deftest test-parse-action-copy-variable
  (testing ":show-first-box-word-2"
    (let [action-name :show-first-box-word-2
          action-data {:type "copy-variable" :var-name "current-word" :from "item-1"}
          parent-action :show-first-box-word
          next-action :introduce-word]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:show-first-box-word-2 {:type   "copy-variable"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   action-data}})))))

(deftest test-parse-action-add-animation
  (testing ":show-first-box-word-3"
    (let [action-name :show-first-box-word-3
          action-data {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}
          parent-action :show-first-box-word
          next-action :introduce-word]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:show-first-box-word-3 {:type   "add-animation"
                                      :parent :show-first-box-word
                                      :next   [:introduce-word]
                                      :data   action-data}})))))

(deftest test-parse-action-add-action
  (testing ":vaca-this-is-var"
    (let [action-name :vaca-this-is-var
          action-data {:type "action" :from-var [{:var-name "current-word" :var-property "home-vaca-this-is-action"}]}
          parent-action :introduce-word
          next-action :empty-small-copy-1]
      (is (= (parse-action action-name action-data parent-action next-action)
             {:vaca-this-is-var {:type   "action"
                                 :parent :introduce-word
                                 :next   [:empty-small-copy-1]
                                 :data   action-data}}))))

  (deftest test-parse-action-animation-sequence
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
            next-action nil]
        (is (= (parse-action action-name action-data parent-action next-action)
               {:senora-vaca-audio-touch-second-box {:type   "animation-sequence"
                                                     :parent :first-word
                                                     :next   []
                                                     :data   action-data}}))))
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
        (is (= (parse-action action-name action-data parent-action next-action)
               {:vaca-can-you-say {:type   "animation-sequence"
                                   :parent :introduce-word
                                   :next   [:vaca-question-var]
                                   :data   action-data}}))))))
