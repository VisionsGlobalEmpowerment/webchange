(ns webchange.lesson-builder.stage-actions.activity-mock
  (:require
    [cljs.test :refer-macros [deftest testing is]]
    [day8.re-frame.test :as rf-test]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]))

(def data {:assets        []
           :objects       {:background  {:type "background"
                                         :src  "/raw/img/park/slide/background2.jpg"}
                           :text-object {:text        "Letter"
                                         :font-family "Tabschool"}}
           :scene-objects [["background"]]
           :actions       {:intro            {:type "sequence-data"
                                              :data [{:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Initial phrase text"}]}]
                                              :tags ["tag-1"]}
                           :dialog-1         {:type "sequence-data"
                                              :data [{:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 1"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 2"}]}]}
                           :dialog-2         {:type "sequence-data"
                                              :data [{:type "parallel"
                                                      :data [{:type "sequence-data"
                                                              :data [{:type     "empty"
                                                                      :duration 1000}
                                                                     {:type        "animation-sequence"
                                                                      :target      "guide"
                                                                      :phrase-text "Phrase 1"}]}
                                                             {:type "sequence-data"
                                                              :data [{:type     "empty"
                                                                      :duration 1000}
                                                                     {:type        "animation-sequence"
                                                                      :target      "guide"
                                                                      :phrase-text "Phrase 2"}]}
                                                             {:type "sequence-data"
                                                              :data [{:type     "empty"
                                                                      :duration 1000}
                                                                     {:type        "animation-sequence"
                                                                      :target      "guide"
                                                                      :phrase-text "Phrase 3"}]}]}]}
                           :dialog-3         {:type "sequence-data"
                                              :data [{:type "parallel"
                                                      :data [{:type "sequence-data"
                                                              :data [{:type     "empty"
                                                                      :duration 1000}
                                                                     {:type        "animation-sequence"
                                                                      :target      "guide"
                                                                      :phrase-text "Phrase 1"}]}
                                                             {:type "sequence-data"
                                                              :data [{:type     "empty"
                                                                      :duration 1000}
                                                                     {:type        "animation-sequence"
                                                                      :target      "guide"
                                                                      :phrase-text "Phrase 2"}]}]}]}
                           :simple-action    {:type        "animation-sequence"
                                              :target      "guide"
                                              :phrase-text "Phrase 1"}
                           :insert-action    {:type "sequence-data"
                                              :data [{:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 1"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 2"}]}]}
                           :reorder-action-1 {:type "sequence-data"
                                              :data [{:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 1"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 2"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 3"}]}]}
                           :reorder-action-2 {:type "sequence-data"
                                              :data [{:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 1"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 2"}]}
                                                     {:type "sequence-data"
                                                      :data [{:type     "empty"
                                                              :duration 1000}
                                                             {:type        "animation-sequence"
                                                              :target      "guide"
                                                              :phrase-text "Phrase 3"}]}]}}
           :triggers      {}
           :metadata      {}})

(defn prepare-and-dispatch
  [dispatch-data callback]
  (re-frame/dispatch [::state/set-activity-data data])
  (rf-test/wait-for [::state/set-activity-data]
                    (re-frame/dispatch dispatch-data)
                    (rf-test/wait-for [::state/set-activity-data]
                                      (callback))))

(defn dispatch-and-compare
  [dispatch-data expected-result]
  (re-frame/dispatch [::state/set-activity-data data])
  (rf-test/wait-for [::state/set-activity-data]
                    (re-frame/dispatch dispatch-data)
                    (rf-test/wait-for [::state/set-activity-data]
                                      (let [current-data @(re-frame/subscribe [::state/activity-data])
                                            diff (clojure.data/diff data current-data)]
                                        (when-not (= (take 2 diff) expected-result)
                                          (print ">>" (take 2 diff)))
                                        (is (= (take 2 diff) expected-result))))))
