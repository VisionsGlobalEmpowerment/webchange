(ns webchange.admin.widgets.activities-list.views
  (:require
    [webchange.ui.index :as ui]))

(defn- activities-list-item
  [{:keys [id name preview assessment on-click on-edit-click library-type]}]
  (let [handle-card-click #(on-click id)
        handle-edit-click #(do (.stopPropagation %)
                               (on-edit-click id))]
    [:div {:class-name "activities-list-item"
           :on-click   handle-card-click}
     [ui/image {:src        preview
                :class-name (ui/get-class-name {"preview" true
                                                "preview-assessment" assessment})
                :lazy?      true}]
     (when library-type
       [ui/icon {:class-name "global-icon"
                 :icon "global"
                 :color (if (= "global" library-type)
                          "blue-1"
                          "yellow-1")}])
     [:div.data
      name
      (when (fn? on-edit-click)
        [ui/button {:icon     "edit"
                    :on-click handle-edit-click}])]]))

(defn- back-item
  [{:keys [text on-click]}]
  [:div {:class-name "back-item"
         :on-click   on-click}
   [:div.data text]])

(defn activities-list
  [{:keys [data loading? on-card-click on-edit-click on-back-click]}]
  [:div.widget--activities-list
   (if loading?
     [ui/loading-overlay]
     [:div.activities-list
      (when (fn? on-back-click)
        [back-item {:text "Back"
                    :on-click on-back-click}])
      (for [{:keys [id] :as activity} data]
        ^{:key id}
        [activities-list-item (merge activity
                                     {:on-click      on-card-click
                                      :on-edit-click on-edit-click})])])])
