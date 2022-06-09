(ns webchange.admin.pages.activity-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activity-edit.state :as state]
    [webchange.admin.widgets.activity-info-form.views :refer [activity-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]))

(defn- activity-form
  [{:keys [activity-id]}]
  (let [{:keys [name preview created-at last-edit]} @(re-frame/subscribe [::state/activity])
        handle-edit-click #(re-frame/dispatch [::state/edit])
        handle-play-click #(re-frame/dispatch [::state/play])

        form-editable? @(re-frame/subscribe [::state/form-editable?])
        removing? @(re-frame/subscribe [::state/removing?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove-activity #(re-frame/dispatch [::state/remove])
        handle-remove-click #(ui/with-confirmation {:message    "Remove Activity?"
                                                    :on-confirm handle-remove-activity})]
    [:div.activity-form
     [:h1
      name
      [:dl
       [:dt "Created"]
       [:dd (date-str->locale-date created-at)]
       [:dt "Last Edited"]
       [:dd (date-str->locale-date last-edit)]]]
     [ui/image {:src        preview
                :class-name "preview"}
      [:div.actions
       [ui/icon-button {:icon     "play"
                        :on-click handle-play-click}
        "Play"]
       [ui/icon-button {:icon     "edit"
                        :on-click handle-edit-click}
        "Edit Activity"]]]

     [:h2
      "Activity Details"
      [ui/icon-button {:icon     (if form-editable? "close" "edit")
                       :on-click handle-edit-info-click
                       :variant  "light"}]]
     [activity-info-form {:activity-id activity-id
                          :editable?   form-editable?
                          :on-save     handle-save
                          :class-name  "info-form"}]
     (when-not form-editable?
       [ui/icon-button {:icon       "remove"
                        :variant    "light"
                        :class-name "remove-button"
                        :loading?   removing?
                        :on-click   handle-remove-click}
        "Delete Activity"])]))

(defn page
  [{:keys [activity-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/activity-loading?])]
      [page/page {:class-name "page--activity-edit"}
       [page/header {:title "Activity"
                     :icon  "activity"}]
       [page/main-content {:class-name "page--activity-edit--content"}
        (if loading?
          [ui/loading-overlay]
          [activity-form {:activity-id activity-id}])]])))
