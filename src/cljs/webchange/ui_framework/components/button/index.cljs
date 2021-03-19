(ns webchange.ui-framework.components.button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color disabled? href on-click target variant]
    :or   {color     "primary"
           disabled? false
           on-click  #()
           target    "_blank"
           variant   "contained"}}]
  (let [this (r/current-component)
        handle-click (fn []
                       (if (some? href)
                         (js/window.open href target)
                         (on-click)))]
    (into [:button {:class-name (get-class-name (-> {"wc-button" true}
                                                    (assoc variant true)
                                                    (assoc color true)
                                                    (assoc class-name (some? class-name))))
                    :disabled   disabled?
                    :on-click   handle-click}]
          (r/children this))))
