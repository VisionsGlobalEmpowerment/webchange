(ns webchange.admin.widgets.add-class-students.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as list]
    [webchange.admin.widgets.add-class-students.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn- students-list-item
  [{:keys [avatar id name selected?]}]
  (let [handle-click #(re-frame/dispatch [::state/select-student id])]
    [list/list-item {:avatar     avatar
                     :name       name
                     :on-click   handle-click
                     :class-name "students-list-item"
                     :actions    [ui/icon {:icon       "check"
                                           :class-name (ui/get-class-name {"check-icon" true
                                                                           "selected"   selected?})}]}]))

(defn- students-list
  []
  (let [students @(re-frame/subscribe [::state/students])]
    [list/list {:class-name "students-list"}
     (for [{:keys [id] :as student} students]
       ^{:key id}
       [students-list-item student])]))

(defn- actions
  [{:keys [class-id on-cancel on-save]}]
  (let [data-loading? @(re-frame/subscribe [::state/data-loading?])
        data-saving? @(re-frame/subscribe [::state/data-saving?])
        handle-add-click #(re-frame/dispatch [::state/save class-id on-save])
        handle-cancel-click #(when (fn? on-cancel) (on-cancel))]
    [:div.actions
     [ui/button {:title      "Reset adding"
                 :class-name "cancel-button"
                 :on-click   handle-cancel-click}
      "Cancel"]
     [ui/button {:title      "Add selected students"
                 :class-name "add-button"
                 :disabled?  (or data-saving? data-loading?)
                 :loading?   data-saving?
                 :on-click   handle-add-click}
      "Add"]]))

(defn- loading-indicator
  []
  [:div.loading-indicator
   [ui/circular-progress]])

(defn add-class-students
  []
  (r/create-class
    {:display-name "Add Class Students"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [props]
       (let [data-loading? @(re-frame/subscribe [::state/data-loading?])]
         [:div {:class-name "widget--add-class-students"}
          (if-not data-loading?
            [students-list props]
            [loading-indicator])
          [actions props]]))}))
