(ns webchange.editor-v2.graph-builder.filters.phrases-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.filters.phrases :refer [get-phrases-graph]]))

(deftest test-get-phrases-graph--simple
  (let [graph {:01 {:entity      :object
                    :connections #{{:previous :root
                                    :name     "next"
                                    :handler  :03}}}
               :02 {:entity      :object
                    :connections #{{:previous :root
                                    :name     "next"
                                    :handler  :04}}}
               :03 {:entity      :action
                    :connections #{{:previous :01
                                    :name     "true"
                                    :handler  :05}
                                   {:previous :01
                                    :name     "false"
                                    :handler  :07}}}
               :04 {:entity      :action
                    :connections #{{:previous :02
                                    :name     "true"
                                    :handler  :06}
                                   {:previous :02
                                    :name     "false"
                                    :handler  :07}}}
               :05 {:entity      :action
                    :connections #{{:previous :03
                                    :name     "next"
                                    :handler  :08}}}
               :06 {:entity      :action
                    :connections #{{:previous :04
                                    :name     "next"
                                    :handler  :08}}}
               :07 {:entity      :action
                    :connections #{{:previous :03
                                    :name     "next"
                                    :handler  :09}
                                   {:previous :04
                                    :name     "next"
                                    :handler  :09}}
                    :data        {:phrase true}}
               :08 {:entity      :action
                    :connections #{{:previous :05
                                    :name     "next"
                                    :handler  :10}
                                   {:previous :06
                                    :name     "next"
                                    :handler  :10}}}
               :09 {:entity      :action
                    :connections #{}}
               :10 {:entity      :action
                    :connections #{{:previous :08
                                    :name     "next"
                                    :handler  :11}}
                    :data        {:phrase true}}
               :11 {:entity      :action
                    :connections #{{:previous :10
                                    :name     "next"
                                    :handler  :12}}}
               :12 {:entity      :action
                    :connections #{{:previous :11
                                    :name     "next"
                                    :handler  :13}}
                    :data        {:phrase true}}
               :13 {:entity      :action
                    :connections #{}}}]
    (let [actual-result (get-phrases-graph graph)
          expected-result {:01 {:entity      :object
                                :connections #{{:handler :07
                                                :name    "next"}
                                               {:handler :10
                                                :name    "next"}}}
                           :02 {:entity      :object
                                :connections #{{:handler :07
                                                :name    "next"}
                                               {:handler :10
                                                :name    "next"}}}
                           :07 {:entity      :action
                                :connections #{}
                                :data        {:phrase true}}
                           :10 {:entity      :action
                                :connections #{{:handler :12
                                                :name    "next"}}
                                :data        {:phrase true}}
                           :12 {:entity      :action
                                :connections #{}
                                :data        {:phrase true}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-phrases-graph--no-phrases
  (let [graph {:01 {:entity      :object
                    :connections #{{:previous :root
                                    :name     "next"
                                    :handler  :03}}}
               :02 {:entity      :object
                    :connections #{{:previous :root
                                    :name     "next"
                                    :handler  :04}}}
               :03 {:entity      :action
                    :connections #{{:previous :01
                                    :name     "true"
                                    :handler  :05}
                                   {:previous :01
                                    :name     "false"
                                    :handler  :07}}}
               :04 {:entity      :action
                    :connections #{{:previous :02
                                    :name     "true"
                                    :handler  :06}
                                   {:previous :02
                                    :name     "false"
                                    :handler  :07}}}
               :05 {:entity      :action
                    :connections #{{:previous :03
                                    :name     "next"
                                    :handler  :08}}}
               :06 {:entity      :action
                    :connections #{{:previous :04
                                    :name     "next"
                                    :handler  :08}}}
               :07 {:entity      :action
                    :connections #{{:previous :03
                                    :name     "next"
                                    :handler  :09}
                                   {:previous :04
                                    :name     "next"
                                    :handler  :09}}}
               :08 {:entity      :action
                    :connections #{{:previous :05
                                    :name     "next"
                                    :handler  :10}
                                   {:previous :06
                                    :name     "next"
                                    :handler  :10}}}
               :09 {:entity      :action
                    :connections #{}}
               :10 {:entity      :action
                    :connections #{{:previous :08
                                    :name     "next"
                                    :handler  :11}}}
               :11 {:entity      :action
                    :connections #{{:previous :10
                                    :name     "next"
                                    :handler  :12}}}
               :12 {:entity      :action
                    :connections #{{:previous :11
                                    :name     "next"
                                    :handler  :13}}}
               :13 {:entity      :action
                    :connections #{}}}]
    (let [actual-result (get-phrases-graph graph)
          expected-result {}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
