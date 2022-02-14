(ns webchange.dashboard.students.student-form.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.students.student-form.state :as state]
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

(defn- class
  []
  (let [value @(re-frame/subscribe [::state/class])
        options @(re-frame/subscribe [::state/class-options])
        handle-change #(re-frame/dispatch [::state/set-class %])
        error @(re-frame/subscribe [::state/class-error])]
    [control-group {:title     "Class"
                    :required? true}
     [select {:value       value
              :options     options
              :on-change   handle-change
              :error       error
              :type        "int"
              :placeholder "Select class"
              :variant     "outlined"}]]))

(defn- gender
  []
  (let [value @(re-frame/subscribe [::state/gender])
        options @(re-frame/subscribe [::state/gender-options])
        handle-change #(re-frame/dispatch [::state/set-gender %])]
    [control-group {:title "Gender"}
     [select {:value       value
              :options     options
              :on-change   handle-change
              :type        "int"
              :placeholder "Select gender"
              :variant     "outlined"}]]))

(defn- first-name
  []
  (let [value @(re-frame/subscribe [::state/first-name])
        handle-change #(re-frame/dispatch [::state/set-first-name %])
        error @(re-frame/subscribe [::state/first-name-error])]
    [control-group {:title     "First Name"
                    :required? true}
     [input {:value     value
             :error     error
             :on-change handle-change}]]))

(defn- last-name
  []
  (let [value @(re-frame/subscribe [::state/last-name])
        handle-change #(re-frame/dispatch [::state/set-last-name %])]
    [control-group {:title "Last Name"}
     [input {:value     value
             :on-change handle-change}]]))

(defn- birth-date
  []
  (let [value @(re-frame/subscribe [::state/birth-date])
        handle-change #(re-frame/dispatch [::state/set-birth-date %])]
    [control-group {:title "Date of Birth"}
     [ui/text-field
      {:value           value
       :type            "date"
       :on-change       #(handle-change (->> % .-target .-value))
       :variant         "outlined"
       :class-name      "mui-control"
       :InputLabelProps {:shrink true}}]]))

(defn- access-code
  []
  (r/with-let [show-code (r/atom false)
               handle-show-click #(swap! show-code not)]
    (let [value @(re-frame/subscribe [::state/access-code])
          handle-generate-click #(re-frame/dispatch [::state/generate-access-code])
          error @(re-frame/subscribe [::state/access-code-error])]
      [control-group {:title     "Access-code"
                      :required? true}
       [ui/text-field
        {:type       (if @show-code "text" "password")
         :value      value
         :variant    "outlined"
         :class-name "mui-control"
         :error      (boolean error)
         :InputProps {:end-adornment (r/as-element [ui/input-adornment {:position "end"}
                                                    [ui/icon-button {:on-click handle-show-click}
                                                     (if @show-code
                                                       [ic/visibility-off]
                                                       [ic/visibility])]
                                                    [ui/icon-button {:on-click handle-generate-click}
                                                     [ic/update]]])}}]])))

(defn- student-form
  []
  [:div.student-form
   [class]
   [first-name]
   [last-name]
   [gender]
   [birth-date]
   [access-code]])

(defn student-form-modal
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
       [student-form]]
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
