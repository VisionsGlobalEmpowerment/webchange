(ns webchange.admin.pages.students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.students.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [school-name @(re-frame/subscribe [::state/school-name])
        handle-add-click #(re-frame/dispatch [::state/add-student])]
    [page/header {:title   school-name
                  :icon    "school"
                  :actions [c/icon-button {:icon       "add"
                                           :title      "New Student Account"
                                           :on-click   handle-add-click}
                            "New Student Account"]}]))

(defn- list-item
  [{:keys [id] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-student id])
        handle-remove-click #(re-frame/dispatch [::state/remove-student id])]
    [l/list-item (merge props
                        {:actions [:<>
                                   [c/icon-button {:icon     "remove"
                                                   :title    "Remove"
                                                   :variant  "light"
                                                   :on-click handle-remove-click}]
                                   [c/icon-button {:icon     "edit"
                                                   :title    "Edit"
                                                   :variant  "light"
                                                   :on-click handle-edit-click}]]})]))

(defn- content
  []
  (let [list-data @(re-frame/subscribe [::state/students])]
    [page/main-content {:title "Students"}
     [l/list {:class-name "students-list"}
      (for [{:keys [id] :as data} list-data]
        ^{:key id}
        [list-item data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--students"}
     [header]
     [content]]))
