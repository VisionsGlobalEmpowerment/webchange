(ns webchange.admin.widgets.activity-info-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.activity-info-form.state :as state]
    [webchange.validation.specs.activity :as spec]))

(defn activity-info-form
  [{:keys [activity-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          data @(re-frame/subscribe [::state/form-data])
          model {:name              {:label "Name"
                                     :type  :text}
                 :about             {:label "About"
                                     :type  :text-multiline}
                 :short-description {:label "Short Description"
                                     :type  :text}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [form {:form-id   (-> (str "activity-" activity-id)
                            (keyword))
             :data      data
             :model     model
             :spec      ::spec/activity-info
             :on-save   handle-save
             :disabled? (not editable?)
             :loading?  loading?
             :saving?   saving?}])))
