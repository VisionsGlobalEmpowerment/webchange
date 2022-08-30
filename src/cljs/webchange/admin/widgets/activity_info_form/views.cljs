(ns webchange.admin.widgets.activity-info-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.activity-info-form.state :as state]
    [webchange.validation.specs.activity :as spec]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- remove-window
  [{:keys [activity-id]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-activity activity-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm   (if done? confirm-removed remove)
                 :on-cancel    (when-not done? close-window)}
     (if done?
       "Activity successfully deleted"
       "Are you sure you want to delete activity?")]))

(defn activity-info-form
  [{:keys [activity-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? class-name controls on-save on-cancel]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          data @(re-frame/subscribe [::state/form-data])
          locked? (:locked data)
          model {:group-left   {:type       :group
                                :class-name "group-left"
                                :fields     {:about             {:label "About"
                                                                 :type  :text-multiline}
                                             :short-description {:label "Short Description"
                                                                 :type  :text}
                                             :remove            {:label    "Delete Activity"
                                                                 :type     (if locked? :empty :action)
                                                                 :icon     "trash"
                                                                 :on-click #(re-frame/dispatch [::state/open-remove-window])}}}
                 :group-filler {:type       :group
                                :class-name "group-filler"}
                 :group-right  {:type       :group
                                :class-name "group-right"
                                :fields     (cond-> {:name {:label "Name"
                                                            :type  :text}
                                                     :lang {:label   "Language"
                                                            :type    :select
                                                            :options language-options}}
                                                    (some? controls) (assoc :controls {:type    :custom
                                                                                       :label   (:label controls)
                                                                                       :control (:component controls)}))}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [:<>
       [ui/form {:form-id    (-> (str "activity-" activity-id)
                                 (keyword))
                 :data       data
                 :model      model
                 :spec       ::spec/activity-info
                 :on-save    handle-save
                 :on-cancel  on-cancel
                 :disabled?  (not editable?)
                 :loading?   loading?
                 :saving?    saving?
                 :class-name class-name}]
       [remove-window {:activity-id activity-id}]])))
