(ns webchange.ui-framework.components.checkbox.index
  (:require
    [webchange.ui-framework.components.label.index :as label-component]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [checked? class-name disabled? label on-change value]
    :or   {checked?  false
           disabled? false}}]
  (let [handle-change (fn [event]
                        (on-change {:value    value
                                    :checked? (.. event -target -checked)}))]
    [:div {:class-name (get-class-name (-> {"wc-checkbox" true}
                                           (assoc class-name (some? class-name))))}
     (when (some? label)
       [label-component/component {:class-name "wc-checkbox-label"}
        label])
     [:input (cond-> {:type     "checkbox"
                      :checked  checked?
                      :disabled disabled?}
                     (fn? on-change) (assoc :on-change handle-change))]]))