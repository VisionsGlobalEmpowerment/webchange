(ns webchange.admin.widgets.layout.navigation.utils)

(defn set-navigation-items-active
  [navigation-tree current-route]
  (map (fn [{:keys [children route] :as item}]
         (let [updated-children (set-navigation-items-active children current-route)
               child-active? (some :active? updated-children)
               active? (or child-active?
                           (= (:page route) (:page current-route)))]
           (cond-> item
                   active? (assoc :active? active?)
                   :always (assoc :children updated-children))))
       navigation-tree))
