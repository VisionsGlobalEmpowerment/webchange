(ns webchange.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.views :as views]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.utils.browser-history :as history]
    [webchange.state.warehouse :as warehouse]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render
   [views/main-panel]
   (.getElementById js/document "app")))

(defn- handle-history-back
  []
  (re-frame/dispatch [::warehouse/send-system-log {:type :back}]))

(defn reset-viewport
  []
  (let [w (.-innerWidth js/window)
        h (.-innerHeight js/window)]
    (re-frame/dispatch [::events/change-viewport {:width w :height h}])))

(defn init-viewport
  []
  (.addEventListener js/window "resize" reset-viewport)
  (reset-viewport))

(defn ^:export init
  [student-page]
                                        ;(sw/setup (and service-worker-enabled config/use-cache) "/service-worker.js")
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (warehouse/init!)
  (re-frame/dispatch [::events/init-current-school])
  (re-frame/dispatch [::events/init-current-user])
  (init-viewport)
  (when student-page
    (history/add-event-handler :back handle-history-back))
  (dev-setup)
  (mount-root))
