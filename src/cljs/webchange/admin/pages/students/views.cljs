(ns webchange.admin.pages.students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.students.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :refer [icon icon-button]]))

(defn- header
  []
  (let [students-number @(re-frame/subscribe [::state/students-number])
        handle-add-student #(re-frame/dispatch [::state/add-student])]
    [:div.header
     [:div.students-number
      [icon {:icon       "students"
             :class-name "students-icon"}]
      [:div.text (str students-number " Students")]]
     [icon-button {:icon       "add"
                   :variant    "light"
                   :class-name "add-student"
                   :on-click   handle-add-student}
      "Add Student"]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])]
      [page/page {:class-name "page--students"}
       [page/header {:title school-name
                     :icon  "school"}
        [header]]
       [page/main-content
        "Content"]])))
