(ns webchange.editor-v2.diagram.scene-data-parser.parser-objects-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-objects :refer [parse-object]]))

(deftest test-parse-objects
  (testing "object with handler parsing"
    (let [object-name :senora-vaca
          object-data {:type    "animation" :x 655 :y 960 :name "senoravaca" :anim "idle" :speed 0.3 :skin "vaca"
                       :width   351 :height 717 :scale {:x 1 :y 1} :start true
                       :actions {:click {:type "action" :id "restart" :on "click"}}}]
      (let [actual-result (parse-object object-name object-data)
            expected-result {:senora-vaca {:type        "object"
                                           :connections {:root {:handlers {:click [:restart]}}}
                                           :data        object-data}}]
        (is (= actual-result expected-result)))))

  (testing "object without handler parsing"
    (let [object-name :vera
          object-data {:type  "animation" :x 1128 :y 960 :name "vera" :anim "idle" :speed 0.3
                       :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}]
      (let [actual-result (parse-object object-name object-data)
            expected-result {:vera {:type        "object"
                                    :connections {:root {:handlers {}}}
                                    :data        object-data}}]
        (is (= actual-result expected-result))))))
