(ns webchange.lesson-builder.tools.background-music.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.background-music.state :as state]
    [webchange.audio-utils.player :as utils]
    [webchange.ui.index :as ui]))

(defn audio-component
  []
  (let [audio (atom nil)
        state (r/atom "stop")
        handle-play (fn []
                      (reset! state "play")
                      (when (some? @audio)
                        (utils/play @audio)))
        handle-stop (fn []
                      (reset! state "stop")
                      (when (some? @audio)
                        (utils/stop @audio)))
        handle-click (fn []
                       (if (= @state "stop")
                         (handle-play)
                         (handle-stop)))]
    (r/create-class
     {:display-name "wc-audio"
      :component-did-mount
      (fn [this]
        (let [{:keys [url volume] :or {volume 1}} (r/props this)]
          (when (some? url)
            (reset! audio (js/Audio. url))
            (set! (.-volume @audio) volume))))
      :component-did-update
      (fn [this [_ old-props]]
        (let [new-props (r/props this)]
          (when-not (= (:url old-props) (:url new-props))
            (handle-stop)
            (reset! audio (js/Audio. (:url new-props))))
          (when (and (some? @audio)
                     (not= (:volume old-props) (:volume new-props)))
            (set! (.-volume @audio) (:volume new-props)))))
      :component-will-unmount
      (fn []
        (handle-stop))
      :reagent-render
      (fn []
        [:div {:class-name "audio-component"}
         [ui/button {:icon     (if (= @state "stop") "play" "stop")
                     :icon-color    "white"
                     :on-click handle-click}]])})))

(defn- volume
  []
  (let [volume @(re-frame/subscribe [::state/volume])]
    [:div.background-music-volume
     [:div.background-music-subheader "Volume"]
     [ui/input {:value volume
                :on-change #(re-frame/dispatch [::state/set-volume %])
                :type "range"
                :step 0.05
                :min 0
                :max 1}]]))

(defn- current-music
  []
  (let [src @(re-frame/subscribe [::state/src])
        volume @(re-frame/subscribe [::state/volume])
        audio-name @(re-frame/subscribe [::state/audio-name])]
    [:div.current-audio
     [:div.background-music-subheader "Current music"]
     [:div.audio-wrapper
      [:div.audio-name-wrapper
       [audio-component {:url src
                         :volume volume}]
       [:div.name audio-name]]
      [ui/icon {:icon "trash"
                :color "grey-4"
                :on-click #(re-frame/dispatch [::state/remove-src])}]]]))

(defn- available-options
  []
  (let [music-options @(re-frame/subscribe [::state/music-options])
        current-src @(re-frame/subscribe [::state/src])]
    [:div.music-options-list
     [:div.background-music-subheader "Available options"]
     (for [{:keys [url] :as option} music-options]
       ^{:key url}
       [:div.music-options-list-item {:on-click #(re-frame/dispatch [::state/set-src url])}
        [:div.audio-wrapper
         [:div.audio-name-wrapper
          [audio-component {:url url}]
          [:div.name (:name option)]]
         [ui/icon {:icon "check"
                   :color (if (= url current-src)
                            "blue-1"
                            "grey-4")}]]])]))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn- upload-music
  []
  (r/with-let [file-input (atom nil)]
    (let [uploading? @(re-frame/subscribe [::state/uploading?])
          handle-upload #(re-frame/dispatch [::state/upload-music %])]
      [:div.upload-music
       [:input {:type      "file"
                :accept    ["mp3" "wav" "m4a"]
                :on-change #(-> % change-event->file handle-upload)
                :ref       #(reset! file-input %)}]
       [ui/button {:class-name "background-music-upload"
                   :on-click  #(.click @file-input)}
        "Upload File"]])))

(defn background-music
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [:div.widget--background-music
     [:h1 "Background Music"]
     [current-music]
     [volume]
     [available-options]
     [upload-music]
     [ui/button {:class-name "background-music-apply"
                 :on-click #(re-frame/dispatch [::state/apply])} "Apply"]]))
