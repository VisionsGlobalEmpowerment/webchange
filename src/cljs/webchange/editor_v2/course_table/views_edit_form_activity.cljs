(ns webchange.editor-v2.course-table.views-edit-form-activity
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.edit-activity :as activity-state]
    [webchange.editor-v2.course-table.views-edit-menu :refer [edit-menu]]))

(defn- activity-item
  [{:keys [data on-click]}]
  (let [{:keys [id name selected?]} data]
    [ui/list-item {:dense    true
                   :button   true
                   :selected  selected?
                   :on-click #(on-click data)}
     [ui/list-item-text {:primary name
                         :secondary id}]]))

(defn activity-form
  []
  (let [activities @(re-frame/subscribe [::activity-state/activities])
        handle-item-click (fn [{:keys [id]}] (re-frame/dispatch [::activity-state/reset-current-activity id]))
        handle-save (fn [] (re-frame/dispatch [::activity-state/save-activity]))]
    [edit-menu {:on-save handle-save}
     [ui/list
      (for [{:keys [id] :as activity} activities]
        ^{:key id}
        [activity-item {:data     activity
                        :on-click handle-item-click}])]]))
