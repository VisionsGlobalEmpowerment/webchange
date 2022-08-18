(ns webchange.lesson-builder.widgets.activity-title.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.activity-history.views :refer [activity-history]]
    [webchange.lesson-builder.widgets.activity-title.state :as state]
    [webchange.ui.index :as ui]))

(defn- last-save
  []
  (r/with-let [ref (atom nil)
               handle-ref #(when (some? %) (reset! ref %))
               history-open? (r/atom false)]
    (let [{:keys [date time]} @(re-frame/subscribe [::state/last-save])
          loading? @(re-frame/subscribe [::state/versions-loading?])
          handle-click #(swap! history-open? not)]
      [:<>
       [:div {:class-name "activity-title--last-save"
              :on-click   handle-click
              :ref        handle-ref}
        [:span.last-save--title "Last Save"]
        [:div.last-save--date
         (if-not loading?
           [:<>
            [:span date]
            [:span time]]
           [:span "..."])]
        [ui/icon {:icon       "drop"
                  :class-name "last-save--drop-icon"}]]
       (when @history-open?
         [ui/dialog {:anchor @ref
                     :title  "History"}
          [activity-history]])])))

(defn activity-title
  []
  (let [activity-name @(re-frame/subscribe [::state/activity-name])
        saving? @(re-frame/subscribe [::state/saving?])
        handle-save-click #(re-frame/dispatch [::state/save])
        handle-preview-click #(re-frame/dispatch [::state/preview])]
    [:div.widget--activity-title
     [:h1.activity-title--name activity-name]
     [last-save]
     [ui/button {:shape      "rounded"
                 :class-name "activity-title--action-button"
                 :loading?   saving?
                 :on-click   handle-save-click}
      "Save"]
     [ui/button {:icon       "play"
                 :shape      "rounded"
                 :color      "blue-1"
                 :class-name "activity-title--action-button"
                 :on-click   handle-preview-click}
      "Preview"]]))
