(ns webchange.admin.widgets.add-class-students.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.select-list.views :refer [select-list]]
    [webchange.admin.widgets.add-class-students.state :as state]
    [webchange.admin.widgets.student-form.views :refer [add-student-form]]
    [webchange.ui.index :as ui]))

(defn- add-student
  [{:keys [school-id]}]
  (r/with-let [dialog-open? (r/atom false)
               handle-close-click #(reset! dialog-open? false)
               handle-add-click #(reset! dialog-open? true)
               handle-save #(do (re-frame/dispatch [::state/load-students])
                                (reset! dialog-open? false))]
    [:<>
     [ui/button {:icon       "plus"
                 :color      "transparent"
                 :shape      "rounded"
                 :text-align "left"
                 :on-click   handle-add-click}
      "New Student Account"]
     (when @dialog-open?
       [ui/dialog {:title    "New Student Account"
                   :on-close handle-close-click}
        [add-student-form {:school-id school-id
                           :on-save   handle-save}]])]))

(defn- students-list
  []
  (let [students @(re-frame/subscribe [::state/students])
        handle-change #(re-frame/dispatch [::state/set-selected-students %])]
    [select-list {:data       students
                  :on-change  handle-change
                  :class-name "students-list"}]))

(defn- actions
  [{:keys [on-cancel]}]
  (let [data-loading? @(re-frame/subscribe [::state/data-loading?])
        data-saving? @(re-frame/subscribe [::state/data-saving?])
        handle-add-click #(re-frame/dispatch [::state/save])
        handle-cancel-click #(when (fn? on-cancel) (on-cancel))]
    [:div.actions
     [ui/button {:title      "Reset adding"
                 :class-name "cancel-button"
                 :color      "blue-1"
                 :on-click   handle-cancel-click}
      "Cancel"]
     [ui/button {:title      "Add selected students"
                 :class-name "add-button"
                 :disabled?  (or data-saving? data-loading?)
                 :loading?   data-saving?
                 :on-click   handle-add-click}
      "Add"]]))

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
     (fn [{:keys [show-actions? show-new-button?]
           :or   {show-actions?    true
                  show-new-button? true}
           :as   props}]
       (let [data-loading? @(re-frame/subscribe [::state/data-loading?])]
         [:div {:class-name "widget--add-class-students"}
          (if-not data-loading?
            [:<>
             (when show-new-button?
               [add-student props])
             [students-list props]
             (when show-actions?
               [actions props])]
            [ui/loading-overlay])]))}))
