(ns webchange.admin.pages.class-profile.students-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.students-add.state :as state]
    [webchange.admin.widgets.add-class-students.views :refer [add-class-students]]
    [webchange.admin.widgets.page.views :as page]))

(defn class-students-add
  [{:keys [class-id school-id]}]
  (let [handle-save #(re-frame/dispatch [::state/close-and-update])
        handle-cancel #(re-frame/dispatch [::state/close])]
    [page/side-bar {:title    "Add students"
                    :icon     "students"
                    :focused? true}
     [add-class-students {:class-id  class-id
                          :school-id school-id
                          :on-save   handle-save
                          :on-cancel handle-cancel}]]))
