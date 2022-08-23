(ns webchange.admin.pages.teachers.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teachers.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [active? email id last-login name]}]
  (let [handle-click #(re-frame/dispatch [::state/open-teacher-profile id])
        handle-edit-click #(re-frame/dispatch [::state/edit-teacher id])
        handle-active-click #(re-frame/dispatch [::state/set-teacher-status id (not active?)])
        determinate? (boolean? active?)
        loading? (= active? :loading)]
    [ui/list-item {:avatar   nil
                   :name     name
                   :info     [{:key   "Email"
                               :value email}
                              {:key   "Last Login"
                               :value last-login}]
                   :on-click handle-click
                   :controls [ui/switch {:label          (cond
                                                           loading? "Saving.."
                                                           (not determinate?) "..."
                                                           active? "Active"
                                                           :default "Inactive")

                                         :checked?       active?
                                         :indeterminate? (not determinate?)
                                         :disabled?      loading?
                                         :on-change      handle-active-click
                                         :class-name     "active-switch"}]
                   :actions  [{:icon     "edit"
                               :title    "Edit teacher"
                               :on-click handle-edit-click}]}]))

(defn- teacher-list
  []
  (let [teachers @(re-frame/subscribe [::state/teachers])]
    [ui/list {:class-name "teachers-list"}
     (for [{:keys [id] :as teacher-data} teachers]
       ^{:key id}
       [list-item teacher-data])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])
          teachers-number @(re-frame/subscribe [::state/teachers-number])
          handle-add-click #(re-frame/dispatch [::state/add-teacher])
          handle-school-click #(re-frame/dispatch [::state/open-school-profile])]
      [page/single-page {:class-name "page--teachers"
                         :header     {:title    school-name
                                      :icon     "school"
                                      :on-click handle-school-click
                                      :stats    [{:icon    "teachers"
                                                  :counter teachers-number
                                                  :label   "Teachers"}]
                                      :actions  [{:text     "Add Teacher to School"
                                                  :icon     "plus"
                                                  :on-click handle-add-click}]}}
       [teacher-list]])))
