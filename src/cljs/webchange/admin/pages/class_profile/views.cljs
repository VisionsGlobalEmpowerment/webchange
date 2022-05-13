(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.counter.views :refer [counter]]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.add-class-students.views :refer [add-class-students]]
    [webchange.admin.widgets.class-form.views :refer [class-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c :refer [button input]]))

(defn- class-counter
  []
  (let [handle-add-student-click #(re-frame/dispatch [::state/open-add-student-form])]
    [counter {:items [#_{:id     :teachers
                         :value  0
                         :title  "Teachers"
                         :action {:title "Teachers"
                                  :icon  "add"}}
                      {:id      :students
                       :value   0
                       :title   "Students"
                       :icon    "students"
                       :color   "blue"
                       :actions [{:title    "Add Student"
                                  :icon     "add"
                                  :on-click handle-add-student-click}]}
                      #_{:id     :events
                         :value  0
                         :title  "Events"
                         :action {:title "Events"
                                  :icon  "add"}}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- side-bar-class-form
  [{:keys [class-id school-id]}]
  (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable (not form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/set-class-data %])]
    [page/side-bar {:title   "Class Info"
                    :actions [:<>
                              [c/icon-button {:icon     "edit"
                                              :variant  "light"
                                              :on-click handle-edit-click}]]}
     [class-form {:class-id  class-id
                  :school-id school-id
                  :editable? form-editable?
                  :on-save   handle-data-save}]]))

(defn- side-bar-add-student
  [{:keys [class-id school-id]}]
  (let [handle-cancel-click #(re-frame/dispatch [::state/open-class-form])]
    [page/side-bar {:title "Add Student"}
     [add-class-students {:class-id  class-id
                          :school-id school-id
                          :on-cancel handle-cancel-click}]]))

(defn- side-bar
  [props]
  (let [side-bar-content @(re-frame/subscribe [::state/side-bar])]
    (case side-bar-content
      :class-form [side-bar-class-form props]
      :add-student [side-bar-add-student props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/main-content {:title "Class Profile"}
      [class-counter]
      [statistics]]
     [side-bar props]]))
