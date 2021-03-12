(ns webchange.ui-framework.components.select.index)

(defn component
  [{:keys [options value]
    :or   {options []}
    :as   props}]
  [:div.wc-select
   [:select
    (for [{:keys [text value]} options]
      ^{:key value}
      [:option {:value    value
                :selected (= value (:value props))}
       text])]
   [:div.controls
    [:button]
    [:button]]])
