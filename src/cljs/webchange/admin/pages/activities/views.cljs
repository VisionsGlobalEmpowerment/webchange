(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- activities-list-item
  [{:keys [id name preview]}]
  (let [handle-card-click #(re-frame/dispatch [::state/open-activity id])
        handle-edit-click #(do (.stopPropagation %)
                               (re-frame/dispatch [::state/edit-activity id]))]
    [:div {:class-name "activities-list-item"
           :on-click   handle-card-click}
     [ui/image {:src        preview
                :class-name "preview"
                :lazy?      true}]
     [:div.data
      name
      [ui/icon-button {:icon     "edit"
                       :variant  "light"
                       :on-click handle-edit-click}]]]))

(defn- activities-list
  []
  (let [loading? @(re-frame/subscribe [::state/activities-loading?])
        activities @(re-frame/subscribe [::state/activities])]
    (if loading?
      [ui/loading-overlay]
      [:div {:class-name "activities-list"}
       (for [{:keys [id] :as activity} activities]
         ^{:key id}
         [activities-list-item activity])])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--activities"}
     [page/header {:title "Activities"
                   :icon  "activity"}]
     [page/main-content
      [activities-list]]]))
