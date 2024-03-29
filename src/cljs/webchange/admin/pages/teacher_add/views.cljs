(ns webchange.admin.pages.teacher-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teacher-add.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.teacher-form.views :refer [add-teacher-form]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [open-teachers-list #(re-frame/dispatch [::state/open-teachers-list])]
      [page/single-page {:class-name        "page--add-teacher"
                         :header            {:title    "Add Teacher to School"
                                             :icon     "teachers"
                                             :on-close open-teachers-list}
                         :background-image? true
                         :form-container?   true}
       [add-teacher-form {:school-id school-id
                          :on-save   open-teachers-list
                          :on-remove open-teachers-list}]])))
