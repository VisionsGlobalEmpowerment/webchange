(ns webchange.admin.pages.student-view.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.student-view.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.student-form.views :refer [edit-student-form]]
    [webchange.ui.index :as ui]))

(defn page
  []
  (r/create-class
    {:display-name "Student Profile"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)])
       )

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [school-id student-id] :as props}]
       (print ">>" props)
       (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
             handle-edit-finished #(re-frame/dispatch [::state/handle-edit-finished])
             handle-cancel-click #(re-frame/dispatch [::state/handle-edit-finished])
             handle-close-click #(re-frame/dispatch [::state/handle-close-click])]
         [page/single-page {:class-name         "page--edit-teacher"
                            :class-name-content "page--edit-teacher--content"
                            :header             {:title    "Student Profile"
                                                 :icon     "students"
                                                 :on-close handle-close-click}
                            :background-image?  true
                            :form-container?    true}
          [page/form-wrapper {:actions (if form-editable?
                                         [ui/button {:icon     "close"
                                                     :color    "grey-3"
                                                     :on-click handle-cancel-click}]
                                         [ui/button {:icon     "edit"
                                                     :color    "grey-3"
                                                     :on-click handle-edit-finished}])}
           [edit-student-form {:student-id student-id
                               :school-id  school-id
                               :editable?  form-editable?
                               :on-save    handle-edit-finished
                               :actions    {:remove-account    true
                                            :remove-from-class false}}]]
          ;[:div.form-wrapper
          ; (if form-editable?
          ;   [ui/button {:icon       "close"
          ;               :color      "grey-3"
          ;               :class-name "form-wrapper-action"
          ;               :on-click   handle-cancel-click}]
          ;   [ui/button {:icon       "edit"
          ;               :color      "grey-3"
          ;               :class-name "form-wrapper-action"
          ;               :on-click   handle-edit-click}])
          ; [edit-teacher-form {:teacher-id teacher-id
          ;                     :disabled?  (not form-editable?)
          ;                     :on-save    open-teachers-list
          ;                     :on-remove  open-teachers-list}]]
          ]))}))