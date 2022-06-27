(ns webchange.admin.pages.student-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.student-edit.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.student-form.views :refer [edit-student-form]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id student-id]}]
    (let [open-students-list #(re-frame/dispatch [::state/open-students-list school-id])]
      [page/single-page {:class-name        "page--edit-student"
                         :header            {:title    "Student Account"
                                             :icon     "students"
                                             :on-close open-students-list}
                         :background-image? true
                         :form-container?   true}
       [edit-student-form {:student-id student-id
                           :school-id  school-id
                           :on-save    open-students-list
                           :on-remove  open-students-list}]])))
