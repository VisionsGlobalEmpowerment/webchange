(ns webchange.editor.common.action-form.animation-sequence-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [cljsjs.enzyme]
    [reagent.core :as r]
    [webchange.editor.common.action-form.animation-sequence-panel-snapshots :refer [animation-sequence-item-snapshot
                                                                                    animation-sequence-items-snapshot
                                                                                    animation-sequence-panel-snapshot]]
    [webchange.editor.common.actions.action-forms.animation-sequence :refer [animation-sequence-item
                                                                             animation-sequence-items
                                                                             animation-sequence-panel]]))

(deftest animation-sequence-panel-test
  (testing "should render correct markup"
    (let [props (r/atom {:audio    "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a"
                         :data     [{:start 14.552 :end 16.639 :anim "talk"}
                                    {:start 17.341 :end 18.999 :anim "talk"}]
                         :duration 16.202
                         :offset   14.397
                         :start    14.397
                         :target   "mari"
                         :track    1})
          params {:scene-id      "sandbox"
                  :scene-objects {:background {:type "background"}
                                  :mari       {:name "mari"}
                                  :box1       {:scene-name "box1"}}}
          component [animation-sequence-panel props params]
          rendered-element (->> (r/as-element component)
                                (.shallow js/enzyme))]

      (is (= (.debug rendered-element) animation-sequence-panel-snapshot)))))

(deftest animation-sequence-items-test
  (testing "should render correct markup"
    (let [props (r/atom {:data [{:start 14.552 :end 16.639 :anim "talk"}
                                {:start 17.341 :end 18.999 :anim "talk"}]})
          component [animation-sequence-items props]
          rendered-element (->> (r/as-element component)
                                (.shallow js/enzyme))]

      (is (= (.debug rendered-element) animation-sequence-items-snapshot)))))

(deftest animation-sequence-item-test
  (testing "should render correct markup"
    (let [props {:start 14.552 :end 16.639 :anim "talk"}
          component [animation-sequence-item props]
          rendered-element (->> (r/as-element component)
                                (.mount js/enzyme))]

      (is (= (.debug rendered-element) animation-sequence-item-snapshot)))))
