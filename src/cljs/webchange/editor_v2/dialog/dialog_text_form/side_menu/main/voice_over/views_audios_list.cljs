(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-audios-list
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state :as state]
    [webchange.ui-framework.components.index :refer [circular-progress icon icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- audios-list-item
  [{:keys [alias selected? url]}]
  (let [handle-click #(re-frame/dispatch [::state/set-current-audio url])]
    [:div {:on-click   handle-click
           :class-name (get-class-name {"available-audios-list-item" true
                                        "selected"                   selected?})}
     [:div.selected-icon-wrapper
      (when selected?
        [icon {:icon       "check"
               :class-name "selected-icon"}])]
     [:div.alias alias]]))

(defn audios-list
  []
  (let [audios @(re-frame/subscribe [::state/audios-list])]
    [:div.available-audios-list
     (for [{:keys [url] :as audio} audios]
       ^{:key url}
       [audios-list-item audio])]))
