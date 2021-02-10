(ns webchange.test.common.image-manipulation
  (:require
    [clojure.test :refer :all]
    [webchange.common.image-manipulation :as im]))

(deftest test-scale
  []
  (let [scale-max (im/calculate-scale 1920 1080 {:max-width 500 :max-height 500 :min-width 100 :min-height 100})
        scale-min (im/calculate-scale 192 108 {:max-width 500 :max-height 500 :min-width 400 :min-height 400})]
    (is (= scale-max 25/96))
    (is (= scale-min 12/25))))

