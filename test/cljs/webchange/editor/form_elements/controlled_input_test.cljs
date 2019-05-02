(ns webchange.editor.form-elements.controlled-input-test
  (:require
    [cljs.test :refer [deftest testing is use-fixtures]]
    [cljsjs.enzyme]
    [reagent.core :as r]
    [utils.event-utils :refer [get-change-event-object]]
    [webchange.editor.form-elements.controlled-input :refer [controlled-input-core]]
    [webchange.editor.form-elements.controlled-input-test-snapshots :refer [initial-snapshot]]))

(deftest controlled-input-test

  ;
  ; Behavior testing
  ;

  (testing "should call callback when input is valid"
    (let [test-data {:input-value "300"
                     :parsed-value 300}
          callback-calls (atom [])
          constructor (controlled-input-core {:app-state (r/atom {:value      ""
                                                                  :last-value ""})
                                              :parser    (fn [] (:parsed-value test-data))})
          component [constructor {:value     "100"
                                  :on-change #(swap! callback-calls conj %)}]
          rendered-element (->> (r/as-element component)
                                (.mount js/enzyme))]
      (-> (.find rendered-element "input")
          (.simulate "change" (get-change-event-object (:input-value test-data))))
      (is (= @callback-calls [(:parsed-value test-data)]))
      ))

  (testing "should not call callback when input is invalid"
    (let [test-data {:input-value "test"
                     :parsed-value nil}
          callback-calls (atom [])
          constructor (controlled-input-core {:app-state (r/atom {:value      ""
                                                                  :last-value ""})
                                              :parser    (fn [] (:parsed-value test-data))})
          component [constructor {:value     "100"
                                  :on-change #(swap! callback-calls conj %)}]
          rendered-element (->> (r/as-element component)
                                (.mount js/enzyme))]
      (-> (.find rendered-element "input")
          (.simulate "change" (get-change-event-object (:input-value test-data))))
      (is (= @callback-calls []))
      ))

  ;
  ; Snapshots testing
  ;

  (testing "should render correct markup"
    (let [test-data {:input-value "300"
                     :parsed-value 300}
          callback-calls (atom [])
          constructor (controlled-input-core {:app-state (r/atom {:value      ""
                                                                  :last-value ""})
                                              :parser    (fn [] (:parsed-value test-data))})
          component [constructor {:value     "100"
                                  :on-change #(swap! callback-calls conj %)}]
          rendered-element (->> (r/as-element component)
                                (.shallow js/enzyme))]

      (is (= (.debug rendered-element) initial-snapshot))
      ))

  ;
  ; Inner state testing
  ;

  (testing "should set correct initial state"
    (let [app-state (r/atom {:value      ""
                             :last-value ""})
          constructor (controlled-input-core {:app-state app-state
                                              :parser    #()})
          component [constructor {:value     "100"
                                  :on-change #()}]
          _ (->> (r/as-element component)
                                (.mount js/enzyme))]
      (is (= (:value @app-state) "100"))
      (is (= (:last-value @app-state) "100"))
      ))

  (testing "should update initial state after user input"
    (let [test-data {:input-value "300"
                     :parsed-value 300}
          app-state (r/atom {:value      ""
                             :last-value ""})
          constructor (controlled-input-core {:app-state app-state
                                              :parser    (fn [] (:parsed-value test-data))})
          component [constructor {:value     "100"
                                  :on-change #()}]
          rendered-element (->> (r/as-element component)
                                (.mount js/enzyme))]
      (-> (.find rendered-element "input")
          (.simulate "change" (get-change-event-object (:input-value test-data))))
      (is (= (:value @app-state) (:input-value test-data)))
      (is (= (:last-value @app-state) "100"))
      ))
  )
