(ns webchange.editor-v2.components.activity-tracks.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.activity-tracks.state :as state]
    [webchange.editor-v2.components.activity-tracks.track.views :refer [track]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- node-click-handler
  [{:keys [action-path track-link type]}]
  (case type
    "dialog" (re-frame/dispatch [::state/open-dialog-window action-path])
    "track" (re-frame/dispatch [::state/set-second-track-name track-link])))

(defn activity-tracks
  [{:keys [class-name]}]
  (let [main-track @(re-frame/subscribe [::state/main-track])
        second-track @(re-frame/subscribe [::state/second-track])
        second-track-name @(re-frame/subscribe [::state/second-track-display-name])
        handle-node-click #(node-click-handler %)
        handle-main-node-click #(node-click-handler %)]
    (into [:div {:class-name (get-class-name (cond-> {"activity-track" true}
                                                     (some? class-name) (assoc class-name true)))}]
          (cond-> [[track {:nodes         main-track
                           :on-node-click handle-main-node-click}]]
                  (some? second-track) (concat [[:div.track-name second-track-name]
                                                [track {:nodes         second-track
                                                        :on-node-click handle-node-click}]])))))
