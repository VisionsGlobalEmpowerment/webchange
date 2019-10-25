(ns webchange.editor-v2.diagram.scene-data-parser.parser-globals-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-globals :refer [parse-globals]]))

(deftest test-parse-globals
  (testing "triggers parsing"
    (let [scene-data {:triggers {:music {:on "start" :action "start-background-music"}
                                 :start {:on "start" :action "intro"}
                                 :back  {:on "back" :action "stop-activity"}}}]
      (let [actual-result (parse-globals scene-data)
            expected-result {:triggers {:type        "trigger"
                                        :connections {:root {:handlers {:start [:start-background-music :intro]
                                                                        :back  [:stop-activity]}}}
                                        :data        {:music {:on "start" :action "start-background-music"}
                                                      :start {:on "start" :action "intro"}
                                                      :back  {:on "back" :action "stop-activity"}}}}]
        (is (= actual-result expected-result))))))
