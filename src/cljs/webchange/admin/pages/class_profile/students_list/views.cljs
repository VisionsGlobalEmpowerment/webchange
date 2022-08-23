(ns webchange.admin.pages.class-profile.students-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.class-profile.students-list.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [id name]}]
  (let [removing? @(re-frame/subscribe [::state/student-removing? id])
        handle-click #(re-frame/dispatch [::state/open-student-profile id])
        handle-edit-click #(re-frame/dispatch [::state/edit-student id])
        handle-remove-click #(re-frame/dispatch [::state/remove-student id])]
    [ui/list-item {:avatar  nil
                   :name    name
                   :dense?  true
                   :on-click handle-click
                   :actions [{:icon     "trash"
                              :title    "Remove from class"
                              :loading? removing?
                              :on-click handle-remove-click}
                             {:icon     "edit"
                              :title    "Edit"
                              :on-click handle-edit-click}]}]))

(defn- student-list
  []
  (let [students @(re-frame/subscribe [::state/students])]
    [ui/list {:class-name "students-list"}
     (for [{:keys [id] :as student-data} students]
       ^{:key id}
       [list-item student-data])]))

(defn class-students-list
  []
  (r/create-class
    {:display-name "Class student List"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn []
       (let [loading? @(re-frame/subscribe [::state/loading?])
             handle-close-click #(re-frame/dispatch [::state/close])]
         [page/side-bar {:title    "Class students"
                         :icon     "students"
                         :focused? true
                         :actions  [{:icon     "close"
                                     :on-click handle-close-click}]}
          (if-not loading?
            [student-list]
            [ui/loading-overlay])]))}))
