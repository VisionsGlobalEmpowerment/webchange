(ns webchange.ui-framework.components.chip.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [color label on-click variant]}]
  [:div (cond-> {:class-name (get-class-name (-> {"wc-chip"   true
                                                  "clickable" (some? on-click)}
                                                 (assoc (str "variant-" variant) true)
                                                 (assoc (str "color-" color) true)))}
                (some? on-click) (assoc :on-click on-click))
   label])
