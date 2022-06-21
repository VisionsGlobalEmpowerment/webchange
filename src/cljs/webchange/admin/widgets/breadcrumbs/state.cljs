(ns webchange.admin.widgets.breadcrumbs.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.utils.map :refer [map->list]]))

(def path-to-db :widget/breadcrumbs)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def custom-current-node-key :custom-current-node)

(defn- set-custom-current-node
  [db value]
  (assoc db custom-current-node-key value))

(re-frame/reg-event-fx
  ::set-current-node
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ node-data]]
    {:db (set-custom-current-node db node-data)}))

(re-frame/reg-event-fx
  ::reset-current-node
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-custom-current-node db nil)}))

(re-frame/reg-sub
  ::custom-current-node
  :<- [path-to-db]
  #(get % custom-current-node-key))

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
  :<- [::custom-current-node]
  (fn [[{:keys [handler props]} current-node]]
    (if-not (= handler :dashboard)
      (let [path (get-path handler routes/sitemap)
            path-length (count path)]
        (->> path
             (map (fn [step]
                    {:route {:page  step
                             :props props}
                     :text  (routes/get-title {:handler step
                                               :props   props}
                                              {:with-root? false})}))
             (map-indexed (fn [idx step]
                            (let [last-item? (-> path-length (dec) (= idx))]
                              (if last-item?
                                (cond-> (assoc step :last-item? true)
                                        (some? current-node) (merge current-node))
                                step))))))
      [])))

(re-frame/reg-event-fx
  ::go-to-route
  (fn [{:keys [_]} [_ {:keys [page props]}]]
    {:dispatch (-> [::routes/redirect page] (concat (map->list props)) (vec))}))
