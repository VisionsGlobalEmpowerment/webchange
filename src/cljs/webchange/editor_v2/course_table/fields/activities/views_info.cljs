(ns webchange.editor-v2.course-table.fields.activities.views-info
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]))

(defn info-from
  [{:keys [data]}]
  (let [{:keys [name]} @(re-frame/subscribe [::data-state/course-activity (-> data :activity keyword)])]
    [:div
     [ui/typography {:variant "body1"
                     :style   {:padding "2px 0"}}
      name]
     (when (:is-placeholder data)
       [ic/warning])]))
