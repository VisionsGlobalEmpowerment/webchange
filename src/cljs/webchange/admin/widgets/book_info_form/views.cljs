(ns webchange.admin.widgets.book-info-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.book-info-form.state :as state]
    [webchange.validation.specs.activity :as spec]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- remove-window
  [{:keys [book-id]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-book book-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?      open?
                 :loading?   in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm (if done? confirm-removed remove)
                 :on-cancel  (when-not done? close-window)}
     (if done?
       "Book successfully deleted"
       "Are you sure you want to delete book?")]))

(defn book-info-form
  [{:keys [book-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? class-name on-save on-cancel]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          data @(re-frame/subscribe [::state/form-data])
          visible? (= "visible" (:status data))
          model {:group-left {:type :group
                              :class-name "group-left"
                              :fields {:about             {:label "About"
                                                           :type  :text-multiline}
                                       :short-description {:label "Short Description"
                                                           :type  :text}
                                       :remove    {:label    "Delete Book"
                                                   :type     :action
                                                   :icon     "trash"
                                                   :on-click #(re-frame/dispatch [::state/open-remove-window])}}}
                 :group-filler {:type :group
                                :class-name "group-filler"}
                 :group-right {:type :group
                               :class-name "group-right"
                               :fields {:name              {:label "Name"
                                                            :type  :text}
                                        :lang {:label "Language"
                                               :type :select
                                               :options language-options}
                                        :visible {:label (if visible? "Visible" "Not Visible")
                                                  :icon (if visible? "visibility-on" "visibility-off")
                                                  :type :action
                                                  :on-click #(re-frame/dispatch [::state/toggle-visibility])}}}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [:<>
       [ui/form {:form-id    (-> (str "book-" book-id)
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
       [remove-window {:book-id book-id}]])))
