(ns webchange.admin.pages.school-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.counter.views :refer [counter]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.school-form.views :refer [school-form]]
    [webchange.admin.pages.school-profile.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- school-counter
  []
  (let [{:keys [stats]} @(re-frame/subscribe [::state/school-data])]
    [counter {:items [{:id       :teachers
                       :value    (:teachers stats)
                       :title    "Teachers"
                       :on-click #(re-frame/dispatch [::state/open-teachers])
                       :actions  [{:title    "Teachers"
                                   :icon     "add"
                                   :on-click #(re-frame/dispatch [::state/open-add-teacher])}]}
                      {:id       :students
                       :value    (:students stats)
                       :title    "Students"
                       :on-click #(re-frame/dispatch [::state/open-students])
                       :actions  [{:title    "Students"
                                   :icon     "add"
                                   :on-click #(re-frame/dispatch [::state/open-add-student])}]}
                      {:id       :courses
                       :value    (:courses stats)
                       :title    "Courses"
                       :on-click #(re-frame/dispatch [::state/open-courses])
                       :actions  [{:title    "Courses"
                                   :icon     "add"
                                   :on-click #(re-frame/dispatch [::state/open-add-course])}]}
                      {:id       :classes
                       :value    (:classes stats)
                       :title    "Classes"
                       :on-click #(re-frame/dispatch [::state/open-classes])
                       :actions  [{:title    "Classes"
                                   :icon     "add"
                                   :on-click #(re-frame/dispatch [::state/open-add-class])}]}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- side-bar
  [{:keys [school-id]}]
  (let [school-form-editable? @(re-frame/subscribe [::state/school-form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-school-form-editable (not school-form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/set-school-data %])]
    [page/side-bar {:title   "School Info"
                    :actions [:<>
                              [c/icon-button {:icon     "edit"
                                              :variant  "light"
                                              :on-click handle-edit-click}]]}
     [school-form {:id        school-id
                   :editable? school-form-editable?
                   :on-save   handle-data-save}]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [school-name @(re-frame/subscribe [::state/school-name])]
      [page/page
       [page/main-content {:title school-name}
        [school-counter]
        [statistics]]
       [side-bar {:school-id school-id}]])))
