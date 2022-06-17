(ns webchange.admin.pages.teacher-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teacher-profile.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.teacher-form.views :refer [edit-teacher-form]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [teacher-id]}]
    (let [open-teachers-list #(re-frame/dispatch [::state/open-teachers-list])]
      [page/page
       [page/_header {:title "Edit Teacher Account"
                     :icon  "teachers"}]
       [page/main-content
        [edit-teacher-form {:teacher-id teacher-id
                            :on-save    open-teachers-list
                            :on-remove  open-teachers-list}]]])))
