(ns webchange.admin.pages.classes.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.classes.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [id name stats]}]
  (let [{:keys [students teachers]} stats
        readonly? @(re-frame/subscribe [::state/readonly?])
        handle-click #(re-frame/dispatch [::state/open-class-profile id])
        handle-edit-click #(re-frame/dispatch [::state/edit-class id])
        handle-students-click #(re-frame/dispatch [::state/show-class-students id])
        handle-teachers-click #(re-frame/dispatch [::state/edit-class-teachers id])]
    [ui/list-item {:name     name
                   :on-click handle-click
                   :stats    [{:counter  students
                               :icon     "students"
                               :text     "Students"
                               :on-click handle-students-click}
                              {:counter  teachers
                               :icon     "teachers"
                               :text     "Teachers"
                               :on-click handle-teachers-click}]
                   :actions  (when-not readonly?
                               [{:icon     "edit"
                                 :title    "Edit class"
                                 :on-click handle-edit-click}])}]))

(defn- classes-list
  []
  (let [classes @(re-frame/subscribe [::state/classes-list-data])]
    [ui/list {:class-name "classes-list"}
     (for [{:keys [id] :as class-data} classes]
       ^{:key id}
       [list-item class-data])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])
          readonly? @(re-frame/subscribe [::state/readonly?])
          classes-number @(re-frame/subscribe [::state/classes-number])
          handle-add-click #(re-frame/dispatch [::state/add-class])
          handle-school-click #(re-frame/dispatch [::state/open-school-profile])]
      [page/single-page {:class-name "page--classes"
                         :header     {:title    school-name
                                      :icon     "school"
                                      :on-click handle-school-click
                                      :stats    [{:icon    "classes"
                                                  :counter classes-number
                                                  :label   "Classes"}]
                                      :actions  (when-not readonly?
                                                  [{:text     "Add New Class"
                                                    :icon     "plus"
                                                    :on-click handle-add-click}])}}
       [classes-list]])))
