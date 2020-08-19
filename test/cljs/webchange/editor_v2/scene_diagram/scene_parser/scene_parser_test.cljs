(ns webchange.editor-v2.scene-diagram.scene-parser.scene-parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-parser :refer [scene-data->actions-tracks]]
    [webchange.editor-v2.scene-diagram.scene-parser.home-scene-source :as source]))

(deftest test-get-action-children__with-id
  (let [scene-data source/data]
    (let [actual-result (scene-data->actions-tracks scene-data)
          expected-result {"Intro"   [[:senora-vaca-audio-2] [:senora-vaca-audio-1]]
                           "Etc"     [[:senora-vaca-audio-touch-third-box] [:senora-vaca-audio-touch-second-box] [:mari-finish]]
                           "Concept" [[:vaca-goodbye-var] [:concept-chant] [:concept-intro]]}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
