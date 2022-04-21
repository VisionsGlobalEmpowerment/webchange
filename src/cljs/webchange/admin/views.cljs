(ns webchange.admin.views
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [reagent.core :as r]))

(defn get-routes
  [root]
  [root {"/"  :home
         "/a" :a
         "/b" :b}])

(defn- dispatch-route
  [params]
  (print "--- dispatch-route" params))

(defn start!
  [root-url get-routes dispatch]
  (let [routes (get-routes root-url)
        parse-url (partial bidi/match-route routes)
        history (pushy/pushy dispatch parse-url)]
    (pushy/start! history)))

(defn index
  [props]
  (r/create-class
    {:display-name "Admin Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (start! (:url route) get-routes dispatch-route)))

     :reagent-render
     (fn []
       [:div "Admin Page"])}))
