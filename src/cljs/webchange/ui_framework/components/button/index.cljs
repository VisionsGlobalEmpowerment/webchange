(ns webchange.ui-framework.components.button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

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
