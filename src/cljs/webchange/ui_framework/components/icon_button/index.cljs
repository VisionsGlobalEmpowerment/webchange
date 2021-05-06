(ns webchange.ui-framework.components.icon-button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color icon disabled? on-click ref size variant]
    :or   {disabled? false
           color     "default"
           on-click  #()
           size      "default"
           variant   "default"}}]
  (let [this (r/current-component)
        children (r/children this)
        handle-ref (fn [el] (when (some? el) (ref el)))]
    [:button (cond-> {:class-name (get-class-name (-> {"wc-icon-button" true
                                                       "with-text"      (some? children)}
                                                      (assoc class-name (some? class-name))
                                                      (assoc color true)
                                                      (assoc icon true)
                                                      (assoc size true)
                                                      (assoc variant true)))
                      :disabled   disabled?
                      :on-click   on-click}
                     (fn? ref) (assoc :ref handle-ref))
     [icon/component {:icon icon}]
     (when (some? children)
       (into [:span]
             children))]))
