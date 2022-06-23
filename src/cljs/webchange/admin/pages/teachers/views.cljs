(ns webchange.admin.pages.teachers.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teachers.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [id] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-teacher id])]
    [l/list-item (merge props
                        {:actions [{:icon     "edit"
                                    :title    "Edit teacher"
                                    :on-click handle-edit-click}]})]))

(defn- teacher-list
  []
  (let [teachers @(re-frame/subscribe [::state/teachers])]
    [l/list {:class-name "teachers-list"}
     (for [{:keys [id] :as teacher-data} teachers]
       ^{:key id}
       [list-item teacher-data])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])
          teachers-number @(re-frame/subscribe [::state/teachers-number])
          handle-add-click #(re-frame/dispatch [::state/add-teacher])]
      [page/single-page {:class-name "page--schools"
                         :header     {:title   school-name
                                      :icon    "school"
                                      :stats   [{:icon    "teachers"
                                                 :counter teachers-number
                                                 :label   "Teachers"}]
                                      :actions [{:text     "Add Teacher to School"
                                                 :icon     "plus"
                                                 :on-click handle-add-click}]}}
       [teacher-list]])))
