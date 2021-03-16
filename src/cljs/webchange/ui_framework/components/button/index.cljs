(ns webchange.ui-framework.components.button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [color disabled? variant on-click class-name]
    :or   {color     "primary"
           disabled? false
           on-click  #()
           variant   "contained"}}]
  (let [this (r/current-component)]
    (into [:button {:class-name (get-class-name (-> {"wc-button" true}
                                                    (assoc variant true)
                                                    (assoc color true)
                                                    (assoc class-name (some? class-name))))
                    :disabled   disabled?
                    :on-click   on-click}]
          (r/children this))))
