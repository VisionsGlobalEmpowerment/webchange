(ns webchange.admin.pages.activity-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activity-edit.state :as state]
    [webchange.admin.widgets.activity-info-form.views :refer [activity-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]))

(defn- activity-form
  [{:keys [activity-id]}]
  (let [{:keys [name preview created-at updated-at]} @(re-frame/subscribe [::state/activity])
        handle-edit-click #(re-frame/dispatch [::state/edit])
        handle-play-click #(re-frame/dispatch [::state/play])

        form-editable? @(re-frame/subscribe [::state/form-editable?])
        removing? @(re-frame/subscribe [::state/removing?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove #(re-frame/dispatch [::state/open-activities-page])]
    [:div.activity-form
     [:div.header
      [:div.info
       name
       [:dl
        [:dt "Created"]
        [:dd (date-str->locale-date created-at)]
        [:dt "Last Edited"]
        [:dd (date-str->locale-date updated-at)]]]
      [:div.actions
       [ui/button {:icon       "system-play"
                   :class-name "play-button"
                   :on-click   handle-play-click}
        "Play"]
       [ui/button {:icon       "edit"
                   :class-name "edit-button"
                   :on-click   handle-edit-click}
        "Edit Activity"]]]

     [ui/image {:src        preview
                :class-name "preview"}]

     [:div.activity-details
      [:h2
       "Activity Details"
       [:div.actions
        [ui/button {:icon     "duplicate"
                    :on-click handle-edit-info-click}]
        [ui/button {:icon     (if form-editable? "close" "edit")
                    :on-click handle-edit-info-click}]]]
      [activity-info-form {:activity-id activity-id
                           :editable?   form-editable?
                           :on-save     handle-save
                           :on-cancel   handle-save
                           :on-remove   handle-remove
                           :class-name  "info-form"}]]]))

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
