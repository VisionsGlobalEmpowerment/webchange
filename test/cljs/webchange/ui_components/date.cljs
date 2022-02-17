(ns webchange.ui-components.date
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.ui-framework.components.date.utils :refer [apply-mask parse-date]]))

(deftest test-apply-mask--usa-date
  (let [mask {:blocks    ["m" "d" "y"]
              :delimiter "/"}]
    (is (= (apply-mask {:year  2014
                        :month 9
                        :day   19} mask)
           "9/19/2014"))

    (is (= (apply-mask {:year  nil
                        :month 9
                        :day   1} mask)
           "9/1"))

    (is (= (apply-mask {:year  nil
                        :month 9
                        :day   nil} mask)
           "9"))

    (is (= (apply-mask {:year  nil
                        :month 10
                        :day   nil} mask)
           "10/"))))

(defn compare-dates
  [actual-date expected-date]
  (doseq [key [:year :month :day]]
    (is (= (get actual-date key)
           (get expected-date key)))))

(deftest test-parse-date
  (let [mask {:blocks    ["y" "m" "d"]
              :delimiter "-"}]
    (compare-dates (parse-date "2014-09-19" mask)
                   {:year  2014
                    :month 9
                    :day   19})
    (compare-dates (parse-date "2020-12-" mask)
                   {:year  2020
                    :month 12
                    :day   nil})
    (compare-dates (parse-date "2020-" mask)
                   {:year  2020
                    :month nil
                    :day   nil})
    (compare-dates (parse-date "20" mask)
                   {:year  20
                    :month nil
                    :day   nil})
    (compare-dates (parse-date "2014-9" mask)
                   {:year  2014
                    :month 9
                    :day   nil}))
  (let [mask {:blocks    ["m" "d" "y"]
              :delimiter "/"}]
    (compare-dates (parse-date "12/31/2022" mask)
                   {:year  2022
                    :month 12
                    :day   31})
    (compare-dates (parse-date "10/13" mask)
                   {:year  nil
                    :month 10
                    :day   13})))
