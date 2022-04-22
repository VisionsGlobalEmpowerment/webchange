(ns webchange.admin-school.views
  (:require
    [reagent.core :as r]
    [webchange.admin-dashboard.index :refer [button]]
    ;[webchange.utils.module-router :as router]
    ))

(def routes {"/"  :home
             "/a" :a
             "/b" :b})

(defn- dispatch-route
  [params]
  (print "--- dispatch school route" params))

(defn index
  []
  (r/create-class
    {:display-name "Admin School Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route] :as props} (r/props this)]
         ;(router/init! (:path route) routes dispatch-route)
         ))

     :reagent-render
     (fn []
       [:div
        "Admin School"
        [button]])}))
