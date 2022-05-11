(ns webchange.admin.widgets.school-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.school-form.state :as state]
    [webchange.validation.specs.school-spec :as school-spec]))

(defn school-form
  [{:keys [school-id on-save] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable?]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          school-data @(re-frame/subscribe [::state/form-data])
          model {:name     {:label "School Name"
                            :type  :text}
                 :location {:label "Location"
                            :type  :text}
                 :about    {:label "Location"
                            :type  :text-multiline}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [:div.widget--school-form
       [form {:form-id   (-> (str "school-" school-id)
                             (keyword))
              :data      school-data
              :model     model
              :spec      ::school-spec/edit-school
              :on-save   handle-save
              :disabled? (not editable?)
              :loading?  loading?
              :saving?   saving?}]])))
