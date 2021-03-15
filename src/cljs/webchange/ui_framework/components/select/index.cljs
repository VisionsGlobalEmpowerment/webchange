(ns webchange.ui-framework.components.select.index
  (:require
    [webchange.ui-framework.components.select.icon-down :as icon-down]
    [webchange.ui-framework.components.select.icon-up :as icon-up]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [options value class-name]
    :or   {options []}
    :as   props}]
  [:div {:class-name (get-class-name (-> {"wc-select" true}
                                         (assoc class-name (some? class-name))))}
   [:select
    (for [{:keys [text value]} options]
      ^{:key value}
      [:option {:value    value
                :selected (= value (:value props))}
       text])]
   [:div.controls
    [:button
     [icon-up/data]]
    [:button
     [icon-down/data]]]])
