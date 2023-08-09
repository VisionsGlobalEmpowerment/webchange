(ns webchange.lesson-builder.tools.template-options-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [webchange.lesson-builder.tools.template-options.state :as state]))

(deftest form-options
  (let [activity-data
        {:objects {:text-object {:text "some text"}
                   :img-object {:src "some-src.jpg"}}
         :metadata {:saved-props
                    {:template-options
                     {:some-vector-field
                      [{:text {:linked-object "text-object" :linked-attribute "text"}}
                       {:img {:src {:linked-object "img-object" :linked-attribute "src"}}}]}}}}
        form-data (state/get-form-data activity-data)]
    (testing "form fields can be retrieved from objects"
      (is (= form-data {:some-vector-field
                        [{:text "some text"}
                         {:img {:src "some-src.jpg"}}]})))))
