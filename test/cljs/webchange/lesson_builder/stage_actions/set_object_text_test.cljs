(ns webchange.lesson-builder.stage-actions.set-object-text-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [webchange.lesson-builder.stage-actions.activity-mock :refer [dispatch-and-compare]]
            [webchange.lesson-builder.stage-actions :as stage-actions]))

(deftest set-object-text
  (rf-test/run-test-async
   (testing "change object (keyword) text"
     (dispatch-and-compare [::stage-actions/set-object-text {:object-name :text-object
                                                             :text        "New Text"
                                                             :chunks      []}]
                           [{:objects {:text-object {:text "Letter"}}}
                            {:objects {:text-object {:text "New Text"
                                                     :chunks []}}}])))

  (rf-test/run-test-async
   (testing "change object (text) text"
     (dispatch-and-compare [::stage-actions/set-object-text {:object-name "text-object"
                                                             :text        "New Text"
                                                             :chunks      []}]
                           [{:objects {:text-object {:text "Letter"}}}
                            {:objects {:text-object {:text "New Text"
                                                     :chunks []}}}]))))
