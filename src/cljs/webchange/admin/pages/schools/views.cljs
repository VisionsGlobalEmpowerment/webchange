(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.schools.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.ui.index :as ui]))

(defn school-item
  [{:keys [id name stats]}]
  (let [{:keys [classes courses students teachers]} stats
        handle-edit-click #(re-frame/dispatch [::state/edit-school id])
        handle-classes-click #(re-frame/dispatch [::state/manage-classes id])
        handle-courses-click #(re-frame/dispatch [::state/manage-courses id])
        handle-students-click #(re-frame/dispatch [::state/manage-students id])
        handle-teachers-click #(re-frame/dispatch [::state/manage-teachers id])]
    [ui/list-item {:name    name
                   :stats   [{:counter  students
                              :icon     "students"
                              :text     "Students"
                              :on-click handle-students-click}
                             {:counter  teachers
                              :icon     "teachers"
                              :text     "Teachers"
                              :on-click handle-teachers-click}
                             {:counter  classes
                              :icon     "classes"
                              :text     "Classes"
                              :on-click handle-classes-click}
                             {:counter  courses
                              :icon     "courses"
                              :text     "Courses"
                              :on-click handle-courses-click}]
                   :actions [{:icon     "edit"
                              :title    "Edit school"
                              :on-click handle-edit-click}]}]))

(defn- schools-list
  []
  (let [schools @(re-frame/subscribe [::state/schools-list])]
    [page/main-content
     [ui/list {:class-name "schools-list"}
      (for [school schools]
        ^{:key (:id school)}
        [school-item school])]]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [handle-add-click #(re-frame/dispatch [::state/add-school])]
      [page/single-page {:class-name "page--schools"
                         :header     {:title   "Schools"
                                      :icon    "school"
                                      :actions [{:text     "Add School"
                                                 :icon     "plus"
                                                 :on-click handle-add-click}]}}
       [schools-list]])))
