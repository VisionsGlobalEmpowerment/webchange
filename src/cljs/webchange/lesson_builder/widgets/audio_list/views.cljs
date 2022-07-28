(ns webchange.lesson-builder.widgets.audio-list.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.info.views :refer [info]]
    [webchange.lesson-builder.widgets.audio-list.state :as state]
    [webchange.lesson-builder.widgets.not-implemented.views :refer [not-implemented]]
    [webchange.ui.index :as ui]))

(defn- audio-item
  [{:keys [name on-click selected? url]}]
  (let [handle-click #(on-click url)
        handle-bring-to-top #(re-frame/dispatch [::state/bring-to-top url])]
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
      name]
     [:div.audio-item--actions
      [ui/button {:icon       "edit"
                  :color      "grey-3"
                  :class-name "audio-item--button"}]
      [ui/button {:icon       "select"
                  :color      "grey-3"
                  :class-name "audio-item--button"}]
      [ui/button {:icon       "trash"
                  :color      "grey-3"
                  :class-name "audio-item--button"}]]]))

(defn audio-list
  [{:keys [value on-change]}]
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
       [info "Record audio or upload audio and the file will automatically show in this box to select and edit when done."])]))
