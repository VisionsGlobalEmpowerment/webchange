(ns webchange.ui-framework.components.text-input.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name disabled? id value on-click on-change placeholder]
    :or   {disabled?   false
           on-change   #()
           placeholder ""}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:input (cond-> {:class-name  (get-class-name (-> {"wc-text-input" true}
                                                      (assoc class-name (some? class-name))))
                     :value       value
                     :disabled    disabled?
                     :placeholder placeholder
                     :on-change   handle-change}
                    (some? id) (assoc :id id)
                    (fn? on-click) (assoc :on-click on-click))]))
