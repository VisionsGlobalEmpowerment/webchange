(ns webchange.sandbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.components :refer [interpreter]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.sandbox.state :as state]))

(defn activity
  [{:keys [scene-id]}]
  (re-frame/dispatch [::state/init scene-id])
  (fn []
    [:div {:style {:position "fixed"
                   :top      0
                   :left     0
                   :width    "100%"
                   :height   "100%"}}
     [:style "html, body {margin: 0; max-width: 100%; overflow: hidden; background-color: black;}"]
     [interpreter {:mode ::modes/sandbox}]]))
