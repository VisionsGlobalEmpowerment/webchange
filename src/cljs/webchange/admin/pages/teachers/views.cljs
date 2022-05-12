(ns webchange.admin.pages.teachers.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teachers.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [school-name @(re-frame/subscribe [::state/school-name])
        handle-add-click #(re-frame/dispatch [::state/add-teacher])]
    [page/header {:title   school-name
                  :icon    "school"
                  :actions [c/icon-button {:icon     "add"
                                           :on-click handle-add-click}
                            "New Teacher Account"]}]))

(defn- list-item
  [{:keys [id] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-teacher id])
        handle-remove-click #(re-frame/dispatch [::state/remove-teacher id])]
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
  (let [list-data @(re-frame/subscribe [::state/teachers])]
    [page/main-content {:title "Teachers"}
     [l/list {:class-name "teachers-list"}
      (for [{:keys [id] :as teacher-data} list-data]
        ^{:key id}
        [list-item teacher-data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--teachers"}
     [header]
     [content]]))
