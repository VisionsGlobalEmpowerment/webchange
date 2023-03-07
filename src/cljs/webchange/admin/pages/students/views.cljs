(ns webchange.admin.pages.students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.students.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [active? email id last-login name]}]
  (let [handle-click #(re-frame/dispatch [::state/open-student-profile id])
        handle-edit-click #(re-frame/dispatch [::state/edit-student id])
        handle-active-click #(re-frame/dispatch [::state/set-student-status id (not active?)])
        readonly? @(re-frame/subscribe [::state/readonly?])
        determinate? (boolean? active?)
        loading? (= active? :loading)]
    [ui/list-item {:avatar   nil
                   :name     name
                   :info     [{:key   "Email"
                               :value email}
                              {:key   "Last Login"
                               :value last-login}]
                   :on-click handle-click
                   :controls (when-not readonly?
                               [ui/switch {:label          (cond
                                                             loading? "Saving.."
                                                             (not determinate?) "..."
                                                             active? "Active"
                                                             :else "Inactive")

                                           :checked?       active?
                                           :indeterminate? (not determinate?)
                                           :disabled?      loading?
                                           :on-change      handle-active-click
                                           :class-name     "active-switch"}])
                   :actions  (when-not readonly?
                               [{:icon     "edit"
                                 :title    "Edit student"
                                 :on-click handle-edit-click}])}]))

(defn- students-list
  []
  (let [students @(re-frame/subscribe [::state/students])]
    [ui/list {:class-name "students-list"}
     (for [{:keys [id] :as student} students]
       ^{:key id}
       [list-item student])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])
          readonly? @(re-frame/subscribe [::state/readonly?])
          students-number @(re-frame/subscribe [::state/students-number])
          handle-add-click #(re-frame/dispatch [::state/add-student])
          handle-school-click #(re-frame/dispatch [::state/open-school-profile])]
      [page/single-page {:class-name "page--students"
                         :header     {:title    school-name
                                      :icon     "school"
                                      :on-click (when-not readonly?
                                                  handle-school-click)
                                      :stats    [{:icon    "students"
                                                  :counter students-number
                                                  :label   "Students"}]
                                      :actions  (when-not readonly?
                                                  [{:text     "Add Student to School"
                                                    :icon     "plus"
                                                    :on-click handle-add-click}])}}
       [students-list]])))
