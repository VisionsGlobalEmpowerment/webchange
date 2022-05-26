(ns webchange.ui-framework.components.icon-button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.circular-progress.index :as circular-progress]
    [webchange.ui-framework.components.icon.index :as icon]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color icon direction disabled? loading? on-click ref size title variant]
    :or   {disabled? false
           color     "default"
           loading?  false
           on-click  #()
           size      "normal"
           variant   "default"}}]
  (let [this (r/current-component)
        children (r/children this)
        handle-ref (fn [el] (when (some? el) (ref el)))]
    [:button (cond-> {:class-name (get-class-name (-> {"wc-icon-button"             true
                                                       "with-text"                  (some? children)
                                                       "with-loading"               loading?
                                                       (str "variant-" variant)     (some? variant)
                                                       (str "direction-" direction) (some? direction)}
                                                      (assoc class-name (some? class-name))
                                                      (assoc color true)
                                                      (assoc icon true)
                                                      (assoc (str "size-" size) true)))
                      :disabled   disabled?
                      :on-click   on-click}
                     (fn? ref) (assoc :ref handle-ref)
                     (some? title) (assoc :title title))
     (if loading?
       [circular-progress/component]
       [icon/component {:icon icon}])
     (when (some? children)
       (into [:span]
             children))]))
