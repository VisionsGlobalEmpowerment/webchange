(ns webchange.admin.widgets.counter.views)

#_{:id     :teachers
   :value  0
   :title  "Teachers"
   :action {:title "Teachers"
            :icon  "add"}}

(defn- counter-item
  [{:keys [title value]}]
  [:div.counter-item
   [:div.display
    [:div.value value]
    [:div.title title]]])

(defn counter
  [{:keys [items]}]
  [:div.widget-counter
   (for [{:keys [id] :as item} items]
     ^{:key id}
     [counter-item item])])