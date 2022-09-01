(ns webchange.lesson-builder.widgets.audio-list.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.audio-list.item-edit.views :refer [item-edit]]
    [webchange.lesson-builder.widgets.audio-list.state :as state]
    [webchange.ui.index :as ui]))

(defn- audio-item
  [{:keys [name on-click selected? url] :as props}]
  (let [mode @(re-frame/subscribe [::state/item-mode url])

        handle-click #(on-click url)
        handle-bring-to-top #(do (.stopPropagation %)
                                 (re-frame/dispatch [::state/bring-to-top url]))
        handle-edit-click #(do (.stopPropagation %)
                               (re-frame/dispatch [::state/set-item-mode url (if (= mode "view") "edit" "view")]))
        handle-edit-name-save #(do (re-frame/dispatch [::state/change-alias url %])
                                   (re-frame/dispatch [::state/set-item-mode url "view"]))
        handle-edit-name-cancel #(re-frame/dispatch [::state/set-item-mode url "view"])
        handle-remove-click #(do (.stopPropagation %)
                                 (re-frame/dispatch [::state/remove url]))]
    [:div {:class-name (ui/get-class-name {"audio-item"           true
                                           "audio-item--selected" selected?})
           :on-click   handle-click}
     [:div.audio-item--actions
      [ui/button {:icon       "arrow-up"
                  :color      "grey-3"
                  :class-name "audio-item--button audio-item--bring-top-button"
                  :title      "Bring to top"
                  :on-click   handle-bring-to-top}]
      [ui/icon {:icon       "check"
                :color      "grey-3"
                :class-name "audio-item--icon audio-item--check-icon"}]]
     [:div {:class-name "audio-item--name"
            :title      name}
      (case mode
        "edit" [item-edit (merge props
                                 {:on-change handle-edit-name-save
                                  :on-cancel handle-edit-name-cancel})]
        name)]
     [:div.audio-item--actions
      [ui/button {:icon       (if (= mode "edit") "check" "edit")
                  :color      "grey-3"
                  :class-name (ui/get-class-name {"audio-item--button"         true
                                                  "audio-item--button--active" (= mode "edit")})
                  :title      "Edit file name"
                  :on-click   handle-edit-click}]
      [ui/button {:icon       "trash"
                  :color      "grey-3"
                  :class-name "audio-item--button"
                  :title      "Delete file"
                  :on-click   handle-remove-click}]]]))

(defn audio-list
  [{:keys [value on-change] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [value on-change]}]
    (let [available-audios @(re-frame/subscribe [::state/available-audios])
          handle-item-click (fn [url]
                              (when (fn? on-change)
                                (on-change url)))]
      [:div.widget--audio-list
       (if-not (empty? available-audios)
         (for [{:keys [url] :as audio-data} available-audios]
           ^{:key url}
           [audio-item (merge {:selected? (= url value)
                               :on-click  handle-item-click}
                              audio-data)])
         [ui/info "Record audio or upload audio and the file will automatically show in this box to select and edit when done."])])))
