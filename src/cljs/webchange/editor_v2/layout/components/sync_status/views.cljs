(ns webchange.editor-v2.layout.components.sync-status.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sync-status.history.views :refer [history-button]]
    [webchange.editor-v2.layout.components.sync-status.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.components.index :refer [icon]]))

(defn- sync-progress
  []
  [:div.sync-progress
   [icon {:icon       "sync"
          :rotate?    true
          :class-name "sync-icon"}]
   [:span "Saving Process.."]])

(defn sync-status
  [{:keys [class-name]}]
  (let [sync-in-progress? @(re-frame/subscribe [::state/sync-in-progress?])]
    [:div {:class-name (get-class-name (cond-> {"sync-status" true}
                                               (some? class-name) (assoc class-name true)))}
     (if sync-in-progress?
       [sync-progress]
       [history-button])]))
