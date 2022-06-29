(ns webchange.admin.pages.class-profile.teachers-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.class-profile.teachers-list.state :as state]
    [webchange.admin.widgets.page.side-bar-page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [id name]}]
  (let [removing? @(re-frame/subscribe [::state/teacher-removing? id])
        handle-edit-click #(re-frame/dispatch [::state/edit-teacher id])
        handle-remove-click #(re-frame/dispatch [::state/remove-teacher id])]
    [ui/list-item {:avatar  nil
                   :name    name
                   :dense?  true
                   :actions [{:icon     "trash"
                              :title    "Remove from class"
                              :loading? removing?
                              :on-click handle-remove-click}
                             {:icon     "edit"
                              :title    "Edit"
                              :on-click handle-edit-click}]}]))

(defn- teacher-list
  []
  (let [teachers @(re-frame/subscribe [::state/teachers])]
    [ui/list {:class-name "teachers-list"}
     (for [{:keys [id] :as teacher-data} teachers]
       ^{:key id}
       [list-item teacher-data])]))

(defn class-teachers-list
  []
  (r/create-class
    {:display-name "Class Teacher List"

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
         [page/side-bar {:title    "Class Teachers"
                         :icon     "teachers"
                         :focused? true
                         :actions  [{:icon     "close"
                                     :on-click handle-close-click}]}
          (if-not loading?
            [teacher-list]
            [ui/loading-overlay])]))}))
