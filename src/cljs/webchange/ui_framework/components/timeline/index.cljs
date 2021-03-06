(ns webchange.ui-framework.components.timeline.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- time-line-item
  [{:keys [active? completed? disabled? idx title]
    :or   {active?    false
           completed? false
           disabled?  false}}]
  [:div {:class-name (get-class-name {"time-line-item" true
                                      "active"         active?
                                      "completed"      completed?
                                      "disabled"       disabled?})}
   [:span.idx (inc idx)]
   [:span.title title]])

(defn component
  [{:keys [items]}]
  [:div.wc-time-line
   (for [[idx item] (map-indexed vector items)]
     ^{:key idx}
     [time-line-item (merge item
                            {:idx idx})])])
