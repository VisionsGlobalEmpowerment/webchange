(ns webchange.editor-v2.course-table.fields.activities.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.activities.state :as state]))

(defn edit-form
  [{:keys [data]}]
  (r/with-let [component-id (:idx data)
               _ (re-frame/dispatch [::state/init data component-id])]
    (let [activities @(re-frame/subscribe [::state/activities])
          current-activity @(re-frame/subscribe [::state/current-activity component-id])
          handle-item-click (fn [event]
                              (let [activity-id (->> event .-target .-value)]
                                (re-frame/dispatch [::state/reset-current-activity activity-id component-id])))]
      [ui/select
       {:value     (or current-activity "")
        :on-change handle-item-click
        :on-wheel  #(.stopPropagation %)}
       (for [{:keys [id name]} activities]
         ^{:key id}
         [ui/menu-item {:value id} name])])
    (finally
      (re-frame/dispatch [::state/save component-id]))))
