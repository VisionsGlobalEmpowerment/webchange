(ns webchange.ui-framework.components.button.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color disabled? href on-click shape size state target variant]
    :or   {color     "primary"
           disabled? false
           on-click  #()
           shape     "default"
           size      "medium"
           target    "_blank"
           variant   "contained"}}]
  (let [this (r/current-component)
        handle-click (fn []
                       (if (some? href)
                         (js/window.open href target)
                         (on-click)))]
    (into [:button {:class-name (get-class-name (-> {"wc-button" true}
                                                    (assoc (str "variant-" variant) true)
                                                    (assoc (str "size-" size) true)
                                                    (assoc (str "color-" color) true)
                                                    (assoc (str "shape-" shape) true)
                                                    (assoc class-name (some? class-name))
                                                    (assoc state (some? state))))
                    :disabled   disabled?
                    :on-click   handle-click}]
          (r/children this))))
