(ns webchange.admin.pages.teacher-profile.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.teacher-profile.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.teacher-form.views :refer [edit-teacher-form]]
    [webchange.ui.index :as ui]))

(defn page
  []
  (r/create-class
    {:display-name "Teacher Profile"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [teacher-id]}]
       (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
             handle-edit-click #(re-frame/dispatch [::state/set-form-editable true])
             handle-cancel-click #(re-frame/dispatch [::state/set-form-editable false])
             open-teachers-list #(re-frame/dispatch [::state/open-teachers-list])]
         [page/single-page {:class-name         "page--edit-teacher"
                            :class-name-content "page--edit-teacher--content"
                            :header             {:title    "Edit Teacher"
                                                 :icon     "teachers"
                                                 :on-close open-teachers-list}
                            :background-image?  true
                            :form-container?    true}
          [:div.form-wrapper
           (if form-editable?
             [ui/button {:icon       "close"
                         :color      "grey-3"
                         :class-name "form-wrapper-action"
                         :on-click   handle-cancel-click}]
             [ui/button {:icon       "edit"
                         :color      "grey-3"
                         :class-name "form-wrapper-action"
                         :on-click   handle-edit-click}])
           [edit-teacher-form {:teacher-id teacher-id
                               :disabled?  (not form-editable?)
                               :on-save    open-teachers-list
                               :on-remove  open-teachers-list}]]]))}))
