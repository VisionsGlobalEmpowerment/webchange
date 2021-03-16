(ns webchange.ui-framework.components.text-input.index)

(defn component
  [{:keys [disabled? value on-change]
    :or   {disabled? false
           on-change #()}}]
  (let [handle-change #(-> % (.. -target -value) (on-change))]
    [:input {:class-name "wc-text-input"
             :value      value
             :disabled   disabled?
             :on-change  handle-change}]))
