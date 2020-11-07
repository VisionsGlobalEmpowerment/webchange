(ns webchange.ui.with-styles
  (:require
    [reagent.core :as r]
    ["@material-ui/core/styles" :as mui-styles]))

(defn- class-names [& args]
  (clojure.string/join
    " "
    (mapv name
          (reduce (fn [arr arg]
                    (cond
                      (or (string? arg)
                          (symbol? arg)
                          (keyword? arg)) (conj arr arg)
                      (vector? arg) (vec (concat arr arg))
                      (map? arg) (vec (concat
                                        arr
                                        (reduce-kv (fn [map-arr key value]
                                                     (if (true? value)
                                                       (conj map-arr key)
                                                       map-arr)) [] arg)))
                      :else arr)) [] args))))

(defn use-class
  [classes class-name]
  (let [f (aget classes class-name)]
    (class-names f)))

(defn with-styles
  [component styles]
  (let [reactified (r/reactify-component component)
        hoc (.withStyles mui-styles (clj->js styles))
        wrapped (hoc reactified)
        adapted (r/adapt-react-class wrapped)]
    adapted))
