(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.audios-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.state :as state]
    [webchange.ui-framework.components.index :refer [circular-progress icon icon-button input]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- audios-list-item-display
  [{:keys [name selected? url on-edit-click]}]
  (let [handle-click #(re-frame/dispatch [::state/set-current-audio url])
        handle-edit-click #(do (.stopPropagation %) (on-edit-click))
        handle-remove-click #(do (.stopPropagation %) (re-frame/dispatch [::state/remove-audio url]))
        handle-bring-to-top-click #(do (.stopPropagation %) (re-frame/dispatch [::state/bring-to-top url]))]
    [:div {:on-click   handle-click
           :class-name (get-class-name {"available-audios-list-item" true
                                        "selected"                   selected?})}
     [icon-button {:icon       "bring-to-top"
                   :title      "Bring item to top"
                   :class-name "bring-to-top-button"
                   :on-click   handle-bring-to-top-click}]
     [:div.selected-icon-wrapper
      (when selected?
        [icon {:icon       "check"
               :class-name "selected-icon"}])]
     [:div.name name]
     [:div.actions
      [icon-button {:icon       "edit"
                    :title      "Change audio name"
                    :class-name "action-button"
                    :on-click   handle-edit-click}]
      [icon-button {:icon       "remove"
                    :title      "Delete"
                    :class-name "action-button"
                    :on-click   handle-remove-click}]]]))

(defn- audios-list-item-edit-form
  [{:keys [name on-finish url]}]
  (r/with-let [value (r/atom name)
               handle-input-change #(reset! value %)
               handle-cancel #(on-finish)
               handle-apply #(do (re-frame/dispatch [::state/set-audio-alias url @value])
                                 (on-finish))]
    [:div.available-audios-list-item.active
     [input {:value            @value
             :on-change        handle-input-change
             :select-on-focus? true
             :on-enter-press   handle-apply
             :on-esc-press     handle-cancel
             :class-name       "alias-input"}]
     [:div.actions
      [icon-button {:icon       "check"
                    :title      "Apply changes"
                    :class-name "action-button"
                    :on-click   handle-apply}]
      [icon-button {:icon       "cancel"
                    :title      "Cancel"
                    :class-name "action-button"
                    :on-click   handle-cancel}]]]))

(defn- audios-list-item
  [props]
  (r/with-let [editing? (r/atom false)
               handle-start-edit #(reset! editing? true)
               handle-finish-edit #(reset! editing? false)]
    (if @editing?
      [audios-list-item-edit-form (merge props
                                         {:on-finish handle-finish-edit})]
      [audios-list-item-display (merge props
                                       {:on-edit-click handle-start-edit})])))

(defn audios-list
  []
  (let [audios @(re-frame/subscribe [::state/audios-list])]
    [:div.available-audios-list
     (for [{:keys [url] :as audio} audios]
       ^{:key url}
       [audios-list-item audio])]))
