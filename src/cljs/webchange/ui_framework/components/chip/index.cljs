(ns webchange.ui-framework.components.chip.index
  (:require
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name color label on-click on-remove value variant]}]
  (let [handle-click (fn [event]
                       (when (fn? on-click)
                         (on-click (or value event))))
        handle-remove (fn [event]
                        (when (fn? on-remove)
                          (on-remove (or value event))))]
    [:div {:class-name (get-class-name (-> {"wc-chip"                true
                                            (str "variant-" variant) true
                                            (str "color-" color)     true
                                            class-name               (some? class-name)
                                            "clickable"              (some? on-click)}))
           :on-click   handle-click}

     label
     (when (fn? on-remove)
       [icon-button/component {:icon     "close"
                               :on-click handle-remove}])]))
