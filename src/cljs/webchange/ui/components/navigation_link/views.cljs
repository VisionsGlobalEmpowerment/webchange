(ns webchange.ui.components.navigation-link.views
  (:require
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- get-href
  [router route-params]
  (when (and (some? router)
             (some? route-params))
    (apply (:get-path router) route-params)))

(defn- redirect
  [router route-params]
  (when (and (some? router)
             (some? route-params))
    (apply (:redirect-to router) route-params)))

(defn navigation-link
  [{:keys [class-name route router]}]
  (r/with-let [href (get-href router route)
               handle-click (fn [event]
                              (.preventDefault event)
                              (.stopPropagation event)
                              (redirect router route))
               handle-ref (fn [ref]
                            (when (some? ref)
                              (.addEventListener ref "click" handle-click)))]
    (->> (r/current-component)
         (r/children)
         (into [:a (cond-> {:class-name (get-class-name {class-name (some? class-name)})
                            :ref        handle-ref}
                           (some? href) (assoc :href href))]))))
