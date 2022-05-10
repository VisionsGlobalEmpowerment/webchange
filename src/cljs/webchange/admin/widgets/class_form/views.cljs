(ns webchange.admin.widgets.class-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.class-form.state :as state]
    [webchange.admin.widgets.utils :refer [get-uid]]
    [webchange.ui-framework.components.index :as c]))

(defn- name-control
  [{:keys [disabled?]}]
  (let [id "name"
        value @(re-frame/subscribe [::state/class-name])
        error @(re-frame/subscribe [::state/class-name-error])
        handle-change #(re-frame/dispatch [::state/set-class-name %])]
    [:<>
     [c/label {:for id} "Class Name"]
     [c/input {:id        id
               :value     value
               :error     error
               :disabled? disabled?
               :on-change handle-change}]]))

(defn- course-control
  [{:keys [disabled?]}]
  (let [id "course"
        value @(re-frame/subscribe [::state/course])
        options @(re-frame/subscribe [::state/course-options])
        error @(re-frame/subscribe [::state/course-error])
        handle-change #(re-frame/dispatch [::state/set-course %])]
    [:<>
     [c/label {:for id} "Assign Course"]
     [c/select {:id        id
                :value     value
                :options   options
                :error     error
                :disabled? disabled?
                :on-change handle-change
                :type      "int"}]]))

(defn- submit
  [{:keys [disabled?]}]
  (let [loading? @(re-frame/subscribe [::state/data-saving?])
        handle-click #(re-frame/dispatch [::state/save])]
    [c/button {:on-click   handle-click
               :disabled?  (or disabled? loading?)
               :class-name "submit"}
     (if-not loading?
       "Save"
       [c/circular-progress])]))

(defn- data-loading-indicator
  []
  [:div.data-loading-indicator
   [c/circular-progress]])

(defn class-form
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable?]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])]
      [:div.widget--class-form
       (if-not loading?
         [:div.controls
          [name-control {:disabled? (not editable?)}]
          [course-control {:disabled? (not editable?)}]]
         [data-loading-indicator])
       (when editable?
         [submit {:disabled? loading?}])])))