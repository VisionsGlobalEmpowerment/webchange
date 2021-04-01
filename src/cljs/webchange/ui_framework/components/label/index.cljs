(ns webchange.ui-framework.components.label.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name]}]
  (let [this (r/current-component)]
    (into [:span {:class-name (get-class-name (-> {"wc-label" true}
                                                  (assoc class-name (some? class-name))))}]
          (r/children this))))
