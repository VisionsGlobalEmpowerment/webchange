(ns webchange.admin.pages.activity-edit.activity.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activity-edit.activity.state :as state]
    [webchange.admin.pages.activity-edit.common.state :as common-state]
    [webchange.admin.pages.activity-edit.common.publish.views :as publish]
    [webchange.admin.widgets.activity-info-form.views :refer [activity-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.name :refer [fullname]]))

(defn- activity-form
  [{:keys [activity-id]}]
  (let [{:keys [name preview created-at updated-at
                created-by-user updated-by-user]} @(re-frame/subscribe [::state/activity])
        locked? @(re-frame/subscribe [::state/activity-ui-locked?])

        handle-edit-click #(re-frame/dispatch [::common-state/edit-activity])
        handle-play-click #(re-frame/dispatch [::common-state/play-activity])
        handle-duplicate-click #(re-frame/dispatch [::state/duplicate-activity])

        form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove #(re-frame/dispatch [::state/open-activities-page])]
    [:div.activity-form
     [:div.header
      [:div.header-top
       [:div.info name]
       [:div.actions
        [ui/button {:icon       "system/play"
                    :class-name "play-button"
                    :on-click   handle-play-click}
         "Play"]
        (if locked?
          [ui/button {:icon       "duplicate"
                      :class-name "edit-button"
                      :on-click   handle-duplicate-click}
           "Duplicate Activity"]
          [ui/button {:icon       "edit"
                      :class-name "edit-button"
                      :on-click   handle-edit-click}
           "Edit Activity"])]]
      [:div.header-bottom
       [:div.date-user-info
        "Created by:" [:strong (fullname created-by-user)] " " (date-str->locale-date created-at)]
       [:div.date-user-info
        "Last Edited " [:strong (fullname updated-by-user)] " " (date-str->locale-date updated-at)]]]

     [ui/image {:src        preview
                :class-name "preview"}]

     [:div.activity-details
      [:h2
       "Activity Details"
       (when-not locked?
         [:div.actions
          [ui/button {:icon     "duplicate"
                      :on-click handle-duplicate-click}]
          [ui/button {:icon     (if form-editable? "close" "edit")
                      :on-click handle-edit-info-click}]])]
      [activity-info-form {:activity-id activity-id
                           :editable?   form-editable?
                           :on-save     handle-save
                           :on-cancel   handle-save
                           :on-remove   handle-remove
                           :class-name  "info-form"
                           :controls    {:label     "Publishing Details"
                                         :component publish/publish-controls}}]]]))

(defn page
  [{:keys [activity-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/activity-loading?])]
      [page/page {:class-name    "page--activity-edit"
                  :align-content "center"}
       [page/content {:class-name-content "page--activity-edit--content"
                      :transparent?       true}
        (if loading?
          [ui/loading-overlay]
          [activity-form {:activity-id activity-id}])]])))
