(ns webchange.ui-framework.components.text-area.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name
           error
           id
           label
           on-change
           placeholder
           rows
           value
           variant]
    :or   {placeholder ""
           on-change   #()
           rows        3}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))
        has-label? (some? label)
        id (or id (when has-label? (random-uuid)))]
    [:div {:class-name (get-class-name (-> {"wc-text-area" true}
                                           (assoc (str "variant-" variant) true)
                                           (assoc class-name (some? class-name))))}
     (when has-label?
       [:label {:for        id
                :class-name "wc-text-area-label"}
        label])
     [:textarea (cond-> {:value       value
                         :on-change   handle-change
                         :placeholder placeholder
                         :rows        rows}
                        (some? id) (assoc :id id))]
     (when (some? error)
       [:label.wc-error error])]))
