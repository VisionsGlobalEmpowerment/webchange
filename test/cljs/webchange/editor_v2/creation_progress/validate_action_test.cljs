(ns webchange.editor-v2.creation-progress.validate-action-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.dialog.utils.dialog-action :as actions]))

(deftest test-origin-text-defined__default-text
  (let [action-data {:phrase-text actions/default-phrase-text}]
    (let [actual-result (validate/origin-text-defined? action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-origin-text-defined__empty-text
  (let [action-data {:phrase-text ""}]
    (let [actual-result (validate/origin-text-defined? action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-origin-text-defined__valid
  (let [action-data {:phrase-text "Some phrase text"}]
    (let [actual-result (validate/origin-text-defined? action-data)
          expected-result true]
      (is (= actual-result expected-result)))))

;;
;; Validate diagram node inside "Dialog" form.
;;

(deftest test-validate-inner-action__audio-undefined
  (let [action-data {:data           [{:type     "empty"
                                       :duration 0}
                                      {:type                   "animation-sequence"
                                       :audio                  nil
                                       :phrase-text            "Some phrase text"
                                       :phrase-text-translated "Some phrase text translated"}]
                     :type           "sequence-data"
                     :concept-action true}]
    (let [actual-result (validate/validate-phrase-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-validate-inner-action__phrase-undefined
  (let [action-data {:data [{:type     "empty"
                             :duration 0}
                            {:type        "animation-sequence"
                             :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                             :phrase-text actions/default-phrase-text
                             :start       0.857
                             :duration    0.244}]
                     :type "sequence-data"}]
    (let [actual-result (validate/validate-phrase-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-validate-inner-action__valid
  (let [action-data {:data [{:type     "empty"
                             :duration 0}
                            {:type                   "animation-sequence"
                             :audio                  "/upload/SYPWAJMSQFPCKEDP.mp3"
                             :phrase-text            "Some phrase text"
                             :phrase-text-translated "Some phrase text translated"
                             :start                  0.857
                             :duration               0.244}]
                     :type "sequence-data"}]
    (let [actual-result (validate/validate-phrase-action action-data)
          expected-result true]
      (is (= actual-result expected-result)))))

;;
;; Validate node for full scene diagram.
;;

(deftest test-validate-phrase-action__default-is-valid
  (let [default-action actions/default-action
        action-data {:data               [default-action]
                     :type               "sequence-data"
                     :phrase             "after-box2"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :phrase-description "After third box"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__action-added
  (let [default-action actions/default-action
        action-data {:data               [default-action
                                          {:type     "action"
                                           :from-var [{:var-name     "current-word"
                                                       :var-property "dialog-field-2291a1d6-d8e2-44a2-828f-0a0023282bd8"}]}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__phrase-changed
  (let [action-data {:data               [{:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       nil
                                                   :phrase-text "Some phrase text"}]
                                           :type "sequence-data"}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__audio-added
  (let [action-data {:data               [{:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                                                   :start       0.857
                                                   :duration    0.244
                                                   :phrase-text "New action"}]
                                           :type "sequence-data"}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__simple-action-filled
  (let [action-data {:data               [{:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                                                   :start       0.857
                                                   :duration    0.244
                                                   :phrase-text "Some phrase text"}]
                                           :type "sequence-data"}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__action-filled-with-concept
  (let [action-data {:data               [{:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                                                   :start       0.857
                                                   :duration    0.244
                                                   :phrase-text "Some phrase text"}]
                                           :type "sequence-data"}
                                          ;; ToDo: validate concept action data
                                          {:type     "action"
                                           :from-var [{:var-name     "current-word"
                                                       :var-property "dialog-field-2291a1d6-d8e2-44a2-828f-0a0023282bd8"}]}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-validate-phrase-action__one-of-two-not-filled
  (let [action-data {:data               [{:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                                                   :start       0.857
                                                   :duration    0.244
                                                   :phrase-text "Some phrase text"}]
                                           :type "sequence-data"}
                                          {:data [{:type     "empty"
                                                   :duration 0}
                                                  {:type        "animation-sequence"
                                                   :audio       "/upload/SYPWAJMSQFPCKEDP.mp3"
                                                   :start       0.857
                                                   :duration    0.244
                                                   :phrase-text actions/default-phrase-text}]
                                           :type "sequence-data"}]
                     :type               "sequence-data"
                     :phrase             "pick-wrong"
                     :concept-var        "current-word"
                     :editor-type        "dialog"
                     :dialog-track       "4 Wrong box"
                     :phrase-description "Wrong box picket"}]
    (let [actual-result (validate/validate-dialog-action action-data)
          expected-result false]
      (is (= actual-result expected-result)))))
