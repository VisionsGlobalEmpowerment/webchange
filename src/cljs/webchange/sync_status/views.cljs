(ns webchange.sync-status.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.sync-status.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.index :refer [icon]]))

(defn sync-status
  [{:keys [class-name]}]
  (let [show? @(re-frame/subscribe [::state/show?])]
    [:div {:class-name (get-class-name (cond-> {"sync-status" true}
                                               (some? class-name) (assoc class-name true)))}
     [:div {:class-name (get-class-name {"wrapper" true
                                         "visible" show?})}
      [icon {:icon    "sync"
             :rotate? true}]
      [:span "Saving Process.."]]]))
