(ns webchange.editor-v2.scene-diagram.scene-parser.scene-parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-parser :refer [scene-data->actions-tracks]]
    [webchange.editor-v2.scene-diagram.scene-parser.home-scene-source :as source]))

(deftest test-get-action-children__with-id
  (let [scene-data source/data]
    (let [actual-result (scene-data->actions-tracks scene-data)
          expected-result [{:title "Etc"
                            :nodes [{:type      "dialog"
                                     :action-id :senora-vaca-audio-touch-third-box}
                                    {:type      "dialog"
                                     :action-id :mari-finish}
                                    {:type      "dialog"
                                     :action-id :senora-vaca-audio-touch-second-box}]}
                           {:title "Concept"
                            :nodes [{:type      "dialog"
                                     :action-id :concept-chant}
                                    {:type      "dialog"
                                     :action-id :vaca-goodbye-var}
                                    {:type      "dialog"
                                     :action-id :concept-intro}]}
                           {:title "Intro"
                            :nodes [{:type      "dialog"
                                     :action-id :senora-vaca-audio-1}
                                    {:type      "dialog"
                                     :action-id :senora-vaca-audio-2}]}]]
      (testing "Action tracks can be retrieved (order should not matter)"
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
