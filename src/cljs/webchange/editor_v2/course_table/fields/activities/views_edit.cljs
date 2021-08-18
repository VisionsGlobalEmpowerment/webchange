(ns webchange.editor-v2.course-table.fields.activities.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.activities.state :as state]))

(defn- placeholder-warning
  [current-activity id]
  (if (= current-activity id)
    [ui/icon-button {:on-click #(do
                                  (.stopPropagation %)
                                  (re-frame/dispatch [::state/open-configured-wizard id]))}
     [ic/warning]]
    [ic/warning]))

(defn edit-form
  [{:keys [data]}]
  (r/with-let [component-id (:idx data)
               _ (re-frame/dispatch [::state/init data component-id])
               new-activity-name (r/atom "")]
    (let [errors @(re-frame/subscribe [::state/errors component-id])
          activities @(re-frame/subscribe [::state/activities])
          current-activity @(re-frame/subscribe [::state/current-activity component-id])
          handle-item-click (fn [event]
                              (let [activity-id (->> event .-target .-value)]
                                (re-frame/dispatch [::state/reset-current-activity activity-id component-id])))]
      [:div
       [ui/select
        {:value     (or current-activity "")
         :on-change handle-item-click
         :on-wheel  #(.stopPropagation %)}
        (for [{:keys [id name is-placeholder]} activities]
          ^{:key id}
          [ui/menu-item {:value id} name
           (when is-placeholder [placeholder-warning current-activity id])])
        [ui/menu-item
         [ui/text-field {:placeholder "New Activity"
                         :required true
                         :error (when (:new-activity errors) true)
                         :on-click    #(.stopPropagation %)
                         :on-change   #(reset! new-activity-name (->> % .-target .-value))}]
         [ui/icon-button {:on-click #(re-frame/dispatch [::state/create @new-activity-name component-id])}
          [ic/add]]]]])
    (finally
      (re-frame/dispatch [::state/save component-id]))))
