(ns webchange.editor.common.action-form.action-form-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [cljsjs.enzyme]
    [reagent.core :as r]
    [webchange.editor.common.action-form.action-form-test-snapshots :refer [dispatch-animation-sequence-snapshot
                                                                            main-action-form-general-snapshot]]
    [webchange.editor.common.actions.action-form :refer [dispatch
                                                         main-action-form]]))

(deftest dispatch-test
  (testing "should render correct markup for animation-sequence action"
    (let [props (r/atom {:type "animation-sequence"})
          component [dispatch props]
          rendered-element (->> (r/as-element component)
                                (.shallow js/enzyme))]

      (is (= (.debug rendered-element) dispatch-animation-sequence-snapshot)))))

(deftest main-action-form-test
  (testing "should render correct markup for general tab"
    (let [props (r/atom nil)
          params {:current-tab        :general
                  :change-current-tab #()}
          component [main-action-form props params]
          rendered-element (->> (r/as-element component)
                                (.shallow js/enzyme))]

      (is (= (.debug rendered-element) main-action-form-general-snapshot)))))
