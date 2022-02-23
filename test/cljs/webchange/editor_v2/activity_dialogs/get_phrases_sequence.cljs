(ns webchange.editor-v2.activity-dialogs.get-phrases-sequence
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.activity-dialogs.stub-case-1-concept-data :refer [concept-data]]
    [webchange.editor-v2.activity-dialogs.stub-scene-data :refer [scene-data]]
    [webchange.editor-v2.activity-dialogs.form.utils.get-phrases-sequence :refer [get-phrases-sequence]]))

(deftest test-prepare-phrase-actions
  (let [params {:action-path  [:introduce-big-small]
                :scene-data   scene-data
                :concept-data concept-data}
        actual-result (get-phrases-sequence params)
        expected-result [{:scene-action-path [:introduce-big-small :data 0]
                          :parallel-mark     :none}
                         {:scene-action-path [:introduce-big-small :data 1]
                          :parallel-mark     :none}
                         {:scene-action-path [:introduce-big-small :data 2]
                          :parallel-mark     :none}
                         {:scene-action-path   [:introduce-big-small :data 3]
                          :concept-action-path [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0]
                          :parallel-mark       :none}
                         {:scene-action-path [:introduce-big-small :data 4]
                          :parallel-mark     :none}
                         {:scene-action-path [:introduce-big-small :data 5 :data 0]
                          :parallel-mark     :start}
                         {:scene-action-path [:introduce-big-small :data 5 :data 1]
                          :parallel-mark     :end}
                         {:scene-action-path [:introduce-big-small :data 6]
                          :parallel-mark     :none}
                         {:scene-action-path [:introduce-big-small :data 7 :data 0]
                          :parallel-mark     :start}
                         {:scene-action-path [:introduce-big-small :data 7 :data 1]
                          :parallel-mark     :end}
                         {:scene-action-path [:introduce-big-small :data 8]
                          :parallel-mark     :none}
                         {:scene-action-path [:introduce-big-small :data 9]
                          :parallel-mark     :none}
                         {:scene-action-path   [:introduce-big-small :data 10]
                          :concept-action-path [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0]
                          :parallel-mark       :none}]]

    (is (= (count actual-result)
           (count expected-result)))
    (doseq [idx (range (min (count actual-result)
                            (count expected-result)))]
      (let [actual-phrase (nth actual-result idx)
            expected-phrase (nth expected-result idx)]
        (is (= actual-phrase expected-phrase))
        (when-not (= actual-phrase expected-phrase)
          (print "--- --- ---")
          (print "idx:" idx)
          (print-maps-comparison actual-phrase expected-phrase))))))
