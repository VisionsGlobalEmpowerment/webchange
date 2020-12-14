(ns webchange.editor-v2.course-table.fields.tags.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.fields.tags.state :as tags-state]
    [webchange.editor-v2.course-table.fields.tags.views-edit-appointment :refer [tags-appointment-form]]
    [webchange.editor-v2.course-table.fields.tags.views-edit-restriction :refer [tags-restriction-form]]))

(defn edit-form
  []
  (let [handle-save #(re-frame/dispatch [::tags-state/save-tags])]
    [:div
     [ui/typography {:variant "h6"} "Tags appointment"]
     [tags-appointment-form]
     [ui/divider {:style {:margin "16px 0"}}]
     [ui/typography {:variant "h6"} "Tags restriction"]
     [tags-restriction-form]]))
