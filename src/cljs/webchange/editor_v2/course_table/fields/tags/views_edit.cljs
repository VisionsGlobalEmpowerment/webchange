(ns webchange.editor-v2.course-table.fields.tags.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.tags.state :as tags-state]
    [webchange.editor-v2.course-table.fields.tags.views-edit-appointment :refer [tags-appointment-form]]
    [webchange.editor-v2.course-table.fields.tags.views-edit-restriction :refer [tags-restriction-form]]))

(defn edit-form
  [{:keys [data]}]
  (r/with-let [component-id (:idx data)
               _ (re-frame/dispatch [::tags-state/init data component-id])]
    [:div
     [:div.tags-block
      [:span.tags-title "Tags restriction:"]
      [tags-restriction-form {:component-id component-id}]]
     [:div.tags-block
      [:span.tags-title "Tags appointment:"]
      [tags-appointment-form {:component-id component-id}]]]
    (finally
      (re-frame/dispatch [::tags-state/save-tags component-id]))))

