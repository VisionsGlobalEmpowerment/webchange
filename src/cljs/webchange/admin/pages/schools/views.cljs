(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.components.list.views :as l]
            [webchange.admin.pages.schools.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.ui.index :as ui]
            [webchange.ui-framework.components.index :as c]))

(defn- classes
  [{:keys [id stats]}]
  (let [{:keys [classes]} stats
        handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-classes id]))]
    [ui/chip {:counter  classes
              :icon     "classes"
              :on-click handle-click}
     "Classes"]))

(defn- courses
  [{:keys [id stats]}]
  (let [{:keys [courses]} stats
        handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-courses id]))]
    [ui/chip {:counter  courses
              :icon     "courses"
              :on-click handle-click}
     "Courses"]))


(defn- students
  [{:keys [id stats]}]
  (let [{:keys [students]} stats
        handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-students id]))]
    [ui/chip {:counter  students
              :icon     "students"
              :on-click handle-click}
     "Students"]))

(defn- teachers
  [{:keys [id stats]}]
  (let [{:keys [teachers]} stats
        handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/manage-teachers id]))]
    [ui/chip {:counter  teachers
              :icon     "teachers"
              :on-click handle-click}
     "Teachers"]))

(defn school-item
  [{:keys [id] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-school id])]
    [l/list-item (merge props
                        {:actions [{:icon     "edit"
                                    :title    "Edit school"
                                    :on-click handle-edit-click}]})
     [l/content-right {:class-name "item-content-right"}
      [students props]
      [teachers props]
      [classes props]
      [courses props]]]))

(defn- schools-list
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
    (let [handle-add-click #(re-frame/dispatch [::state/add-school])]
      [page/single-page {:class-name "page--schools"
                         :header     {:title   "Schools"
                                      :icon    "school"
                                      :actions [{:text     "Add School"
                                                 :icon     "plus"
                                                 :on-click handle-add-click}]}}
       [schools-list]])))
