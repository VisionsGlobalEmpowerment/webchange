(ns webchange.admin.pages.activity-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activity-edit.state :as state]
    [webchange.admin.widgets.activity-info-form.views :refer [activity-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.auth.state :as auth]
    [webchange.ui.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.name :refer [fullname]]))

(defn- lock-control
  []
  (let [locked? @(re-frame/subscribe [::state/activity-locked?])
        can-lock? @(re-frame/subscribe [::auth/can-lock-activity?])
        loading? @(re-frame/subscribe [::state/lock-loading?])
        label (cond
                loading? "Saving.."
                locked? "Unlock Activity"
                :default "Lock Activity")
        handle-change #(re-frame/dispatch [::state/set-locked %])]
    [ui/switch {:checked?       locked?
                :disabled?      (not can-lock?)
                :indeterminate? loading?
                :label          label
                :on-change      handle-change
                :class-name     "switch-control"}]))

(defn- publish-control
  []
  (let [published? @(re-frame/subscribe [::state/activity-published?])
        can-publish? @(re-frame/subscribe [::state/can-publish?])
        loading? @(re-frame/subscribe [::state/publish-loading?])
        label (cond
                loading? "Saving.."
                published? "Global"
                :default "Not published")
        handle-change #(re-frame/dispatch [::state/set-published %])]
    [ui/switch {:checked?       published?
                :disabled?      (not can-publish?)
                :indeterminate? loading?
                :label          label
                :on-change      handle-change
                :class-name     "switch-control"}]))

(defn- publish-controls
  []
  [:<>
   [lock-control]
   [publish-control]])


(defn- activity-form
  [{:keys [activity-id]}]
  (let [{:keys [name preview created-at updated-at
                created-by-user updated-by-user metadata]} @(re-frame/subscribe [::state/activity])
        locked? (:locked metadata)

        handle-edit-click #(re-frame/dispatch [::state/edit])
        handle-play-click #(re-frame/dispatch [::state/play])
        handle-duplicate-click #(re-frame/dispatch [::state/duplicate])

        form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove #(re-frame/dispatch [::state/open-activities-page])
        handle-lock #(re-frame/dispatch [::state/set-locked %])]
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
                           :on-lock     handle-lock
                           :class-name  "info-form"
                           :controls    publish-controls}]]]))

(defn page
  [{:keys [activity-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/activity-loading?])]
      [page/page {:class-name    "page--activity-edit"
                  :align-content "center"}
       [page/content {:class-name   "page--activity-edit--content"
                      :transparent? true}
        (if loading?
          [ui/loading-overlay]
          [activity-form {:activity-id activity-id}])]])))
