(ns webchange.ui-framework.components.card.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name on-click]}]
  (r/with-let [this (r/current-component)]
    (into [:div (cond-> {:class-name (get-class-name (cond-> {"wc-card" true}
                                                             (some? class-name) (assoc class-name true)))}
                        (fn? on-click) (assoc :on-click on-click))]
          (r/children this))))
