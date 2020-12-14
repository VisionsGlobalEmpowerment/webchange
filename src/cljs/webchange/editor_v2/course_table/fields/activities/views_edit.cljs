(ns webchange.editor-v2.course-table.fields.activities.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.fields.activities.state :as state]))

(defn- activity-item
  [{:keys [data on-click]}]
  (let [{:keys [id name selected?]} data]
    [ui/list-item {:dense    true
                   :button   true
                   :selected selected?
                   :on-click #(on-click data)}
     [ui/list-item-text {:primary   name
                         :secondary id}]]))

(defn edit-form
  []
  (let [activities @(re-frame/subscribe [::state/activities])
        handle-item-click (fn [{:keys [id]}] (re-frame/dispatch [::state/reset-current-activity id]))
        handle-save (fn [] (re-frame/dispatch [::state/save-activity]))]
    [ui/list
     (for [{:keys [id] :as activity} activities]
       ^{:key id}
       [activity-item {:data     activity
                       :on-click handle-item-click}])]))
