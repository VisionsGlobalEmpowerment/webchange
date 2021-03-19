(ns webchange.ui-framework.components.text-area.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [value placeholder variant on-change rows]
    :or   {placeholder ""
           on-change   #()
           rows        3}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:div {:class-name (get-class-name (-> {"wc-text-area" true}
                                           (assoc variant (some? variant))))}
     [:textarea {:value       value
                 :on-change   handle-change
                 :placeholder placeholder
                 :rows        rows}]]))
