(ns webchange.game-changer.steps.select-background-music.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.steps.select-background-music.state :as state]
    [webchange.ui-framework.components.index :refer [audio card file icon-button label]]))

(defn- music-option
  [{:keys [name url on-click]}]
  (let [handle-click #(on-click {:name name :url url})]
    [card {:class-name "music-option"
           :on-click   handle-click}
     [audio {:url url}]
     [:div.name (or name url)]]))

(defn- upload-music
  [{:keys [on-change]}]
  (let [handle-change (fn [url] (on-change {:url  url
                                            :name "Uploaded audio"}))]
    [card {:class-name "upload-form-wrapper"}
     [file {:type            "audio"
            :on-change       handle-change
            :show-file-name? false
            :class-name      "upload-form"
            :button-text     "Choose File"}]]))

(defn- music-options-list
  [{:keys [on-change]}]
  (let [music-options @(re-frame/subscribe [::state/music-options])]
    [:div.music-options-list
     [label {:class-name "block-label"} "Options:"]
     [upload-music {:on-change on-change}]
     (for [{:keys [url] :as option} music-options]
       ^{:key url}
       [music-option (merge option
                            {:on-click on-change})])]))

(defn- current-value
  [{:keys [on-reset]}]
  (let [{:keys [name url]} @(re-frame/subscribe [::state/current-value])]
    [:div.current-value
     [label {:class-name "block-label"} "Current Background Music:"]
     (if (some? url)
       [card {:class-name "current-value-wrapper"}
        [audio {:url url}]
        [:div.name (or name url)]
        [icon-button {:icon     "remove"
                      :on-click on-reset}]]
       [card {:class-name "current-value-wrapper"}
        [:div.name "No music selected"]])]))

(defn select-background-music
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init (:scene-data @data)])
               handle-change #(re-frame/dispatch [::state/set-current-value %])
               handle-reset #(re-frame/dispatch [::state/reset-current-value])]
    [:div.select-background-music
     [current-value {:on-reset handle-reset}]
     [music-options-list {:on-change handle-change}]]))
