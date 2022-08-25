(ns webchange.admin.widgets.navigation.utils)

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

(defn- item-visible-for
  [{:keys [visible-for]} type]
  (or (empty? visible-for)
      (some #{type} visible-for)))

(defn hide-navigation-items-by-user-type
  [navigation-tree {:keys [type] :as user}]
  (->> navigation-tree
       (filter #(item-visible-for % type))
       (map (fn [{:keys [children] :as item}]
              (if children
                (assoc item :children (hide-navigation-items-by-user-type children user))
                children)))))
