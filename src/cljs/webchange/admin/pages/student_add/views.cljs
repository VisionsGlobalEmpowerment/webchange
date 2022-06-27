(ns webchange.admin.pages.student-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.student-add.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.student-form.views :refer [add-student-form]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [open-students-list #(re-frame/dispatch [::state/open-students-list])]
      [page/single-page {:class-name        "page--add-student"
                         :header            {:title    "Add Student to School"
                                             :icon     "students"
                                             :on-close open-students-list}
                         :background-image? true
                         :form-container?   true}
       [add-student-form {:school-id school-id
                          :on-save   open-students-list
                          :on-remove open-students-list}]])))
