(ns webchange.teacher.widgets.header.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.teacher.widgets.header.state :as state]))

(defn header
  []
  (let [handle-home-click #(re-frame/dispatch [::state/open-dashboard])
        handle-classes-click #(re-frame/dispatch [::state/open-classes])]
    [:div
     [:button {:on-click handle-home-click} "Dashboard"]
     [:button {:on-click handle-classes-click} "Classes"]]))
