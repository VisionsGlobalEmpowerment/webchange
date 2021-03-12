(ns webchange.ui-framework.components.button.index
  (:require
    [clojure.string :refer [join]]
    [reagent.core :as r]))

(defn get-class-name
  [class-names]
  (->> class-names
       (filter (fn [[_ condition]] condition))
       (map first)
       (join " ")))

(defn component
  [{:keys [color variant on-click]
    :or   {color   "primary"
           variant "contained"}}]
  (let [this (r/current-component)]
    (into [:button (cond-> {:class-name (get-class-name (-> {"wc-button" true}
                                                            (assoc variant true)
                                                            (assoc color true)))}
                           (some? on-click) (assoc :on-click on-click))]
          (r/children this))))
