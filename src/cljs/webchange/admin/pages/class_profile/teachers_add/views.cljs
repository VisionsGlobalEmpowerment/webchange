(ns webchange.admin.pages.class-profile.teachers-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.teachers-add.state :as state]
    [webchange.admin.widgets.add-class-teachers.views :refer [add-class-teachers]]
    [webchange.admin.widgets.page.views :as page]))

(defn class-teachers-add
  [{:keys [class-id school-id]}]
  (let [handle-save #(re-frame/dispatch [::state/close-and-update])
        handle-cancel #(re-frame/dispatch [::state/close])]
    [page/side-bar {:title    "Add Teachers"
                    :icon     "teachers"
                    :focused? true
                    :actions  [{:icon     "close"
                                :on-click handle-cancel}]}
     [add-class-teachers {:class-id  class-id
                          :school-id school-id
                          :on-save   handle-save
                          :on-cancel handle-cancel}]]))
