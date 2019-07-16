(ns webchange.interpreter.variables.events-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.interpreter.variables.events :refer [filter-property-values]]))

(deftest dispatch-test
  (testing "should return filtered list"
    (let [exclude-property-values {:concept-name ["ardilla" "oso"]}
          items [{:concept-name "ardilla"}
                 {:concept-name "iman"}
                 {:concept-name "oso"}
                 {:concept-name "uvas"}]]
      (is (= (filter-property-values exclude-property-values items) [{:concept-name "iman"}
                                                                     {:concept-name "uvas"}]))))
  (testing "should return empty list if items empty"
    (let [exclude-property-values {:concept-name ["ardilla" "oso"]}
          items []]
      (is (= (filter-property-values exclude-property-values items) []))))
  (testing "should return initial list if filter is empty"
    (let [exclude-property-values {:concept-name []}
          items [{:concept-name "ardilla"}
                 {:concept-name "iman"}
                 {:concept-name "oso"}
                 {:concept-name "uvas"}]]
      (is (= (filter-property-values exclude-property-values items) [{:concept-name "ardilla"}
                                                                     {:concept-name "iman"}
                                                                     {:concept-name "oso"}
                                                                     {:concept-name "uvas"}]))))
  (testing "should return empty list if all excluded"
    (let [exclude-property-values {:concept-name ["ardilla" "iman" "oso" "uvas"]}
          items [{:concept-name "ardilla"}
                 {:concept-name "iman"}
                 {:concept-name "oso"}
                 {:concept-name "uvas"}]]
      (is (= (filter-property-values exclude-property-values items) [])))))
