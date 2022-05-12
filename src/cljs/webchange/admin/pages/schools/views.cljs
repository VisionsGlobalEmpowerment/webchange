(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.components.list.views :as l]
            [webchange.admin.pages.schools.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [handle-add-click #(re-frame/dispatch [::state/add-school])
        handle-archived-click #(re-frame/dispatch [::state/open-archived-schools])]
    [page/header {:title   "Schools"
                  :icon    "school"
                  :actions [c/icon-button {:icon       "add"
                                           :title      "Add School"
                                           :class-name "manage-students"
                                           :on-click   handle-add-click}
                            "Add School"]}
     [c/icon-button {:icon     "restore"
                     :title    "Archived Schools"
                     :on-click handle-archived-click}
      "Archived Schools"]]))

(defn- archive-button
  [{:keys [id]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/archive-school id]))]
    [c/icon-button {:icon     "archive"
                    :title    "Archive"
                    :variant  "light"
                    :on-click handle-click}]))

(defn- manage-teachers-button
  [{:keys [id]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-teachers id]))]
    [c/icon-button {:icon       "teachers"
                    :title      "Manage Teachers"
                    :variant    "light"
                    :class-name "manage-teachers"
                    :on-click   handle-click}
     "Manage Teachers"]))

(defn- manage-students-button
  [{:keys [id]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-students id]))]
    [c/icon-button {:icon       "students"
                    :title      "Manage Students"
                    :variant    "light"
                    :class-name "manage-students"
                    :on-click   handle-click}
     "Manage Students"]))

(defn school-item
  [{:keys [id] :as props}]
  (let [handle-item-click #(re-frame/dispatch [::state/edit-school id])]
    [l/list-item (merge props
                        {:actions  [archive-button {:id id}]
                         :on-click handle-item-click})
     [l/content-right {:class-name "item-content-right"}
      [manage-teachers-button {:id id}]
      [manage-students-button {:id id}]]]))

(defn- content
  []
  (let [schools @(re-frame/subscribe [::state/schools-list])]
    [page/main-content
     [l/list {:class-name "schools-list"}
      (for [school schools]
        ^{:key (:id school)}
        [school-item school])]]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [page/page {:class-name "page--schools"}
     [header]
     [content]]))
