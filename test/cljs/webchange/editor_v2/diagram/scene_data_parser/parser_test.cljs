(ns webchange.editor-v2.diagram.scene-data-parser.parser-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.scene-data-parser.parser :refer [parse-scene-data]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-test-scene-1 :as scene-1]
    [webchange.editor-v2.diagram.scene-data-parser.parser-test-scene-1-parsed :as parsed-scene-1]))

(deftest test-scene-data-parsing
  (is (=  (parse-scene-data scene-1/data) parsed-scene-1/data)))
