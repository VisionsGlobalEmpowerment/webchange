(ns webchange.ui-framework.components.text-area.index)

(defn component
  [{:keys [value]}]
  [:div.wc-text-area
   [:textarea {:default-value value}]])
