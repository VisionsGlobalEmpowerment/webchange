(ns webchange.editor-v2.course-table.views-edit-form-tags
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.edit-tags :as tags-state]
    [webchange.editor-v2.course-table.views-edit-menu :refer [edit-menu]]
    [webchange.editor-v2.course-table.views-edit-form-tags-appointment :refer [tags-appointment-form]]
    [webchange.editor-v2.course-table.views-edit-form-tags-restriction :refer [tags-restriction-form]]))

(defn tags-form
  []
  (let []
    [edit-menu {:on-save #(re-frame/dispatch [::tags-state/save-tags])}
     [ui/typography {:variant "h6"} "Tags appointment"]
     [tags-appointment-form]
     [ui/divider {:style {:margin "16px 0"}}]
     [ui/typography {:variant "h6"} "Tags restriction"]
     [tags-restriction-form]]))
