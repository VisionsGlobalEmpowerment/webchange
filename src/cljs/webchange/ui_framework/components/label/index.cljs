(ns webchange.ui-framework.components.label.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name for]}]
  (let [this (r/current-component)]
    (into [:label (cond-> {:class-name (get-class-name {"wc-label" true
                                                        class-name (some? class-name)})}
                          (some? for) (assoc :for for))]
          (r/children this))))
