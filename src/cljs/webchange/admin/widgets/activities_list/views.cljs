(ns webchange.admin.widgets.activities-list.views
  (:require
    [webchange.ui.index :as ui]))

(defn- activities-list-item
  [{:keys [id name preview on-click on-edit-click library-type]}]
  (let [handle-card-click #(on-click id)
        handle-edit-click #(do (.stopPropagation %)
                               (on-edit-click id))]
    [:div {:class-name "activities-list-item"
           :on-click   handle-card-click}
     [ui/image {:src        preview
                :class-name "preview"
                :lazy?      true}]
     (when library-type
       [ui/icon {:class-name "global-icon"
                 :icon "global"
                 :color (if (= "global" library-type)
                          "blue-1"
                          "yellow-1")}])
     [:div.data
      name
      [ui/button {:icon     "edit"
                  :on-click handle-edit-click}]]]))

(defn activities-list
  [{:keys [data loading? on-activity-click on-edit-activity-click]}]
  [:div.widget--activities-list
   (if loading?
     [ui/loading-overlay]
     [:div.activities-list
      (for [{:keys [id] :as activity} data]
        ^{:key id}
        [activities-list-item (merge activity
                                     {:on-click      on-activity-click
                                      :on-edit-click on-edit-activity-click})])])])
