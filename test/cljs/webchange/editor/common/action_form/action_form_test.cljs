(ns webchange.editor.common.action-form.action-form-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [cljsjs.enzyme]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.common.action-form.action-form-test-snapshots :refer [dispatch-animation-sequence-snapshot
                                                                            main-action-form-general-snapshot]]
    [webchange.editor.common.actions.action-form :refer [action-form
                                                         dispatch
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


(deftest action-form-test
  (let [dispatch-calls (atom [])]
    (with-redefs [re-frame/subscribe (fn [sub]
                                       (atom
                                         (case sub
                                           [:webchange.subs/scene "test-scene"] {:objects {:background {:type "background"}
                                                                                           :mari       {:name "mari"}
                                                                                           :box1       {:scene-name "box1"}}})))
                  re-frame/dispatch (fn [event] (swap! dispatch-calls conj event))]

                 (testing "should call scene loading by id"
                   (let [init-props (r/atom nil)
                         init-params {:scene-id "test-scene"}
                         _ (action-form init-props init-params)]
                     (is (= @dispatch-calls [[:webchange.interpreter.events/load-scene "test-scene"]]))))

                 (testing "should get current scene from db"
                   (let [init-props (r/atom nil)
                         init-params {:scene-id "test-scene"}
                         [_ props {:keys [scene-id scene-objects]}] (action-form init-props init-params)]
                     (is (= props init-props))
                     (is (= scene-id "test-scene"))
                     (is (= scene-objects {:background {:type "background"}
                                           :mari       {:name "mari"}
                                           :box1       {:scene-name "box1"}})))))))
