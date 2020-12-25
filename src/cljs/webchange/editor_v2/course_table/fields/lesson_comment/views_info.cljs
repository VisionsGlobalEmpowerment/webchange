(ns webchange.editor-v2.course-table.fields.lesson-comment.views-info
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn info-from
  [{:keys [data]}]
  [ui/text-field {:value       (:comment data)
                  :placeholder "Leave your comment here.."
                  :multiline   true
                  :disabled    true
                  :inputProps  {:style {:overflow "hidden"}}}])
