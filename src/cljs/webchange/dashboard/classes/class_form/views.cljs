(ns webchange.dashboard.classes.class-form.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.class-form.state :as state]
    [webchange.ui-framework.components.index :refer [input label select]]))

(defn- control-group
  [{:keys [title required?]
    :or   {required? false}}]
  (into [:div.control-group
         [label {:class-name "control-label"}
          (if required?
            (str title "*")
            title)]]
        (-> (r/current-component)
            (r/children))))

(defn- class-name
  []
  (let [value @(re-frame/subscribe [::state/name])
        handle-change #(re-frame/dispatch [::state/set-name %])
        error @(re-frame/subscribe [::state/name-error])]
    [control-group {:title     "Name"
                    :required? true}
     [input {:value     value
             :error     error
             :on-change handle-change}]]))

(defn- course-selector
  []
  (let [value @(re-frame/subscribe [::state/course-id])
        options @(re-frame/subscribe [::state/course-options])
        handle-change #(re-frame/dispatch [::state/set-course-id %])
        error @(re-frame/subscribe [::state/course-id-error])]
    [control-group {:title     "Course"
                    :required? true}
     [select {:value       value
              :options     options
              :on-change   handle-change
              :error       error
              :type        "int"
              :placeholder "Select course"
              :variant     "outlined"}]]))

(defn- class-form
  []
  [:div.class-form
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
        "Cancel.."]
       [ui/button
        {:type     "submit"
         :variant  "contained"
         :color    "primary"
         :on-click #(do (.preventDefault %)
                        (handle-save))}
        "Save"]]]]))
