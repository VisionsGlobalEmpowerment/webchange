(ns webchange.dashboard.classes.class-form.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.class-form.state :as state]
    [webchange.ui-framework.components.index :refer [input label select]]))

(defn- class-name
  []
  (let [value @(re-frame/subscribe [::state/name])
        handle-change #(re-frame/dispatch [::state/set-name %])]
    [:div
     [label "Name"]
     [input {:value     value
             :on-change handle-change}]]))

(defn- course-selector
  []
  (let [value @(re-frame/subscribe [::state/course-id])
        options @(re-frame/subscribe [::state/course-options])
        handle-change #(re-frame/dispatch [::state/set-course-id %])]
    [:div
     [label "Course"]
     [select {:value       value
              :options     options
              :on-change   handle-change
              :type        "int"
              :placeholder "Select course"
              :variant     "outlined"}]]))

(defn- class-form
  []
  [:div
   [class-name]
   [course-selector]])

(defn class-form-modal
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        title @(re-frame/subscribe [::state/window-title])
        handle-close #(re-frame/dispatch [::state/reset])
        handle-save #(re-frame/dispatch [::state/save])]
    [ui/dialog
     {:open     (boolean open?)
      :on-close handle-close}
     [:form
      [ui/dialog-title title]
      [ui/dialog-content
       [class-form]]
      [ui/dialog-actions
       [ui/button
        {:on-click handle-close}
        "Cancel"]
       [ui/button
        {:type     "submit"
         :variant  "contained"
         :color    "primary"
         :on-click #(do (.preventDefault %)
                        (handle-save))}
        "Save"]]]]))
