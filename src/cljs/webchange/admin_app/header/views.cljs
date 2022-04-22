(ns webchange.admin-app.header.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin-app.header.state :as state]
    [webchange.admin-dashboard.index :refer [button]]))

(defn header
  []
  (let [handle-home-click #(re-frame/dispatch [::state/open-dashboard])
        handle-schools-click #(re-frame/dispatch [::state/open-schools])
        handle-school-profile-click #(re-frame/dispatch [::state/open-school-profile "yyy"])]
    [:div
     [button {:on-click handle-home-click} "Dashboard"]
     [button {:on-click handle-schools-click} "Schools"]
     [button {:on-click handle-school-profile-click} "School Profile"]]))
