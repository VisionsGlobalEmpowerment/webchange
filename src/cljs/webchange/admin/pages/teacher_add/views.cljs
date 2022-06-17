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
      [page/page
       [page/_header {:title "Create Teacher Account"
                     :icon  "teachers"}]
       [page/main-content
        [add-teacher-form {:school-id school-id
                           :on-save   open-teachers-list
                           :on-remove open-teachers-list}]]])))
