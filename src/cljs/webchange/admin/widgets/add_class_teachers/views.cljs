(ns webchange.admin.widgets.add-class-teachers.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.select-list.views :refer [select-list]]
    [webchange.admin.widgets.add-class-teachers.state :as state]
    [webchange.admin.widgets.create-teacher.views :refer [create-teacher]]
    [webchange.ui-framework.components.index :as ui]))

(defn- add-teacher
  [{:keys [class-id]}]
  (r/with-let [dialog-open? (r/atom true)]
    (let [handle-click #(print "click" class-id)]
      [:<>
       [ui/icon-button {:icon       "add"
                        :variant    "light"
                        :class-name "new-teacher-button"
                        :direction  "revert"
                        :on-click   handle-click}
        "New Teacher account"]
       [ui/dialog {:open? @dialog-open?
                   :title "New Teacher account"}
        [create-teacher]]])))

(defn- teachers-list
  []
  (let [teachers @(re-frame/subscribe [::state/teachers])
        handle-change #(re-frame/dispatch [::state/set-selected-teachers %])]
    [select-list {:data       teachers
                  :on-change  handle-change
                  :class-name "teachers-list"}]))

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
     [ui/button {:title      "Add selected teachers"
                 :class-name "add-button"
                 :disabled?  (or data-saving? data-loading?)
                 :loading?   data-saving?
                 :on-click   handle-add-click}
      "Add"]]))

(defn- loading-indicator
  []
  [:div.loading-indicator
   [ui/circular-progress]])

(defn add-class-teachers
  []
  (r/create-class
    {:display-name "Add Class Teachers"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [props]
       (let [data-loading? @(re-frame/subscribe [::state/data-loading?])]
         [:div {:class-name "widget--add-class-teachers"}
          [add-teacher props]
          (if-not data-loading?
            [teachers-list props]
            [loading-indicator])
          [actions props]]))}))
