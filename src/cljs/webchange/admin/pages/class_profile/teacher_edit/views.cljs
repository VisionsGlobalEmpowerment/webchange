(ns webchange.admin.pages.class-profile.teacher-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.teacher-edit.state :as state]
    [webchange.admin.widgets.teacher-form.views :refer [edit-teacher-form]]
    [webchange.admin.widgets.page.views :as page]))

(defn class-teacher-edit
  [{:keys [class-id teacher-id]}]
  (let [handle-save #(re-frame/dispatch [::state/close-and-update])
        handle-cancel #(re-frame/dispatch [::state/close])
        handle-remove-click #(re-frame/dispatch [::state/remove-from-class teacher-id class-id])]
    [page/side-bar {:title    "Teacher Account"
                    :icon     "teachers"
                    :focused? true
                    :actions  [{:icon     "close"
                                :on-click handle-cancel}]}
     [edit-teacher-form {:teacher-id teacher-id
                         :on-save    handle-save
                         :on-cancel  handle-cancel
                         :actions    {:remove-from-class {:label    "Remove From Class"
                                                          :type     :action
                                                          :icon     "account-remove"
                                                          :on-click handle-remove-click}}}]]))
