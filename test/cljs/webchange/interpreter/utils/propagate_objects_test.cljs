(ns webchange.interpreter.utils.propagate-objects-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.interpreter.utils.propagate-objects :refer [get-propagated-objects
                                                           replace-object]]))

(deftest test-replace-object
  (let [scene-objects [["item-1" "cards" "item-2"]
                       ["layer-2"]]
        object-to-replace "cards"
        new-objects ["card-a" "card-b"]]
    (let [actual-result (replace-object scene-objects object-to-replace new-objects)
          expected-result [["item-1" "card-a" "card-b" "item-2"]
                           ["layer-2"]]]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-propagated-objects
  (let [propagate-data {:type      "propagate"
                        :x         342
                        :y         119
                        :width     1238
                        :height    668
                        :el-width  100
                        :el-height 150
                        :el        [{:name "card-{concept-name}"
                                     :data {:type     "group"
                                            :dx       0
                                            :dy       0
                                            :width    100
                                            :height   164
                                            :children ["card-{concept-name}-image"
                                                       "card-{concept-name}-text"]
                                            :states   {:normal  {:dx      0
                                                                 :dy      0
                                                                 :scale-x 1
                                                                 :scale-y 1}
                                                       :pointed {:dx      -15
                                                                 :dy      -25
                                                                 :scale-x 1.3
                                                                 :scale-y 1.3}}}}
                                    {:name                 "card-{concept-name}-image"
                                     :add-to-scene-objects false
                                     :data                 {:type       "image"
                                                            :x          50
                                                            :y          50
                                                            :width      100
                                                            :height     100
                                                            :origin     {:type "center-top"}
                                                            :transition "card-{concept-name}"
                                                            :brightness -0.25
                                                            :filter     "brighten"
                                                            :src        "{image-src}"}}
                                    {:name                 "card-{concept-name}-text"
                                     :add-to-scene-objects false
                                     :data                 {:type           "text"
                                                            :x              0
                                                            :y              0
                                                            :width          100
                                                            :height         50
                                                            :align          "center"
                                                            :fill           "black"
                                                            :font-family    "Lexend Deca"
                                                            :font-size      64
                                                            :text           "{letter}"
                                                            :vertical-align "middle"}}]}
        items [{:concept-name "ardilla"
                :letter       "a"
                :image-src    "a-image"}
               {:concept-name "oso"
                :letter       "o"
                :image-src    "o-image"}]]
    (let [actual-result (get-propagated-objects propagate-data items)
          expected-result {:objects       {:card-ardilla       {:type     "group"
                                                                :width    100
                                                                :height   164
                                                                :children ["card-ardilla-image" "card-ardilla-text"]
                                                                :states   {:normal  {:scale-x 1
                                                                                     :scale-y 1
                                                                                     :x       601.5
                                                                                     :y       378}
                                                                           :pointed {:scale-x 1.3
                                                                                     :scale-y 1.3
                                                                                     :x       586.5
                                                                                     :y       353}}
                                                                :x        601.5
                                                                :y        378}
                                           :card-ardilla-image {:y          50
                                                                :transition "card-ardilla"
                                                                :width      100
                                                                :type       "image"
                                                                :brightness -0.25
                                                                :src        "a-image"
                                                                :filter     "brighten"
                                                                :origin     {:type "center-top"}
                                                                :x          50
                                                                :height     100}
                                           :card-ardilla-text  {:y              0
                                                                :align          "center"
                                                                :vertical-align "middle"
                                                                :font-size      64
                                                                :fill           "black"
                                                                :width          100
                                                                :type           "text"
                                                                :x              0
                                                                :font-family    "Lexend Deca"
                                                                :height         50
                                                                :text           "a"}
                                           :card-oso           {:type     "group"
                                                                :width    100
                                                                :height   164
                                                                :children ["card-oso-image" "card-oso-text"]
                                                                :states   {:normal  {:scale-x 1
                                                                                     :scale-y 1
                                                                                     :x       1220.5
                                                                                     :y       378}
                                                                           :pointed {:scale-x 1.3
                                                                                     :scale-y 1.3
                                                                                     :x       1205.5
                                                                                     :y       353}}
                                                                :x        1220.5
                                                                :y        378}
                                           :card-oso-image     {:y          50
                                                                :transition "card-oso"
                                                                :width      100
                                                                :type       "image"
                                                                :brightness -0.25
                                                                :src        "o-image"
                                                                :filter     "brighten"
                                                                :origin     {:type "center-top"}
                                                                :x          50
                                                                :height     100}
                                           :card-oso-text      {:y              0
                                                                :align          "center"
                                                                :vertical-align "middle"
                                                                :font-size      64
                                                                :fill           "black"
                                                                :width          100
                                                                :type           "text"
                                                                :x              0
                                                                :font-family    "Lexend Deca"
                                                                :height         50
                                                                :text           "o"}}
                           :scene-objects ["card-ardilla" "card-oso"]}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
