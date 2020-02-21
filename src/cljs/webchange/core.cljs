(ns webchange.core
  (:require
    [cljsjs.material-ui]
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.views :as views]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.sw-utils.register :as sw]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn reset-viewport
  []
  (let [w (.-innerWidth js/window)
        h (.-innerHeight js/window)]
    (re-frame/dispatch [::events/change-viewport {:width w :height h}])))

(defn init-viewport
  []
  (.addEventListener js/window "resize" reset-viewport)
  (reset-viewport))

(defn ^:export init []
  (sw/setup config/use-cache "/service-worker.js")
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/init-current-school])
  (re-frame/dispatch [::events/init-current-user])
  (init-viewport)
  (dev-setup)
  (mount-root))
