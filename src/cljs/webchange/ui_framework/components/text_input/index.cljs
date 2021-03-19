(ns webchange.ui-framework.components.text-input.index)

(defn component
  [{:keys [disabled? id value on-change]
    :or   {disabled? false
           on-change #()}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:input (cond-> {:class-name "wc-text-input"
                     :value      value
                     :disabled   disabled?
                     :on-change  handle-change}
                    (some? id) (assoc :id id))]))
