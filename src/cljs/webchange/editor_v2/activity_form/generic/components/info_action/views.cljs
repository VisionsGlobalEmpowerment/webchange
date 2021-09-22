(ns webchange.editor-v2.activity-form.generic.components.info-action.views
  (:require
    [webchange.ui-framework.components.index :refer [dialog]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.info-action.state :as state]
    [webchange.editor-v2.course-dashboard.views-course-info :refer [course-info]]))

(defn info-window
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog {:open?    open?
               :on-close close
               :title    "Course Info"}
       [course-info {:title "Choose Your Topic"}]])))
