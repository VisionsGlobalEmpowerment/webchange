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
        form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable true])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-play-click #(re-frame/dispatch [::state/play])]
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

     [:h2 "Activity Details"]
     [activity-info-form {:activity-id activity-id
                          :editable?   form-editable?
                          :on-save     handle-save}]]))

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
