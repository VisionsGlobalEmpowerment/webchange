(ns webchange.editor-v2.course-table.fields.lesson-comment.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.lesson-comment.state :as state]))

(defn edit-form
  [{:keys [data]}]
  (r/with-let [component-id (:idx data)
               _ (re-frame/dispatch [::state/init data component-id])]
    (let [handle-change #(re-frame/dispatch [::state/set-current-value component-id (.. % -target -value)])]
      [ui/text-field {:default-value (:comment data)
                      :on-change     handle-change
                      :placeholder   "Leave your comment here.."
                      :multiline     true
                      :auto-focus    true
                      :inputProps    {:style {:overflow "hidden"}}}])
    (finally
      (re-frame/dispatch [::state/save component-id]))))
