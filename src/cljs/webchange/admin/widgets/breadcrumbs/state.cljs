(ns webchange.admin.widgets.breadcrumbs.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.utils.map :refer [map->list]]))

(defn- get-path
  ([target-node sitemap]
   (get-path target-node (:dashboard sitemap) [:dashboard]))
  ([target-node sitemap path]
   (let [current-node (last path)]
     (if (= current-node target-node)
       path
       (let [children (if (map? sitemap)
                        (->> (keys sitemap) (filter keyword?))
                        [])]
         (some (fn [child]
                 (get-path target-node (get sitemap child) (conj path child)))
               children))))))

(re-frame/reg-sub
  ::breadcrumbs
  :<- [::state/current-page]
  (fn [{:keys [handler props]}]
    (let [path (get-path handler routes/sitemap)
          path-length (count path)]
      (->> path
           (map (fn [step]
                  {:id    step
                   :route {:page  step
                           :props props}
                   :title (routes/get-title {:handler step
                                             :props   props}
                                            {:with-root? false})}))
           (map-indexed (fn [idx step]
                          (assoc step :last-item? (= idx (dec path-length)))))))))

(re-frame/reg-event-fx
  ::go-to-route
  (fn [{:keys [_]} [_ {:keys [page props]}]]
    {:dispatch (-> [::routes/redirect page] (concat (map->list props)) (vec))}))
