(ns webchange.lesson-builder.tools.voice-translate.transcription.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.voice-translate.transcription.state :as state]
    [webchange.ui.index :as ui]))

(defn play-button
  [{:keys [class-name wave]}]
  (let [state @(re-frame/subscribe [::state/play-button-state])
        handle-start-playing #(re-frame/dispatch [::state/start-playing wave])
        handle-stop-playing #(re-frame/dispatch [::state/stop-playing wave])
        handle-click #(case state
                        "play" (handle-stop-playing)
                        "stop" (handle-start-playing))]
    [ui/button {:icon       (case state
                              "play" "stop"
                              "stop" "play")
                :shape      "rounded"
                :on-click   handle-click
                :class-name class-name
                :variant    "filled"}]))

(defn play-selected-button
  [{:keys [class-name wave]}]
  (let [state @(re-frame/subscribe [::state/play-button-state])
        handle-start-playing #(re-frame/dispatch [::state/start-selected-playing wave])
        handle-stop-playing #(re-frame/dispatch [::state/stop-playing wave])
        handle-click #(case state
                        "play" (handle-stop-playing)
                        "stop" (handle-start-playing))]
    [ui/button {:icon       (case state
                              "play" "stop"
                              "stop" "play")
                :shape      "rounded"
                :on-click   handle-click
                :class-name class-name
                :variant    "filled"}]))

(defn wave
  [{:keys [action-path class-name ref]}]
  (let [audio-url @(re-frame/subscribe [::state/audio-url])
        script-data @(re-frame/subscribe [::state/script-data])
        initial-regions @(re-frame/subscribe [::state/initial-regions])
        handle-change #(re-frame/dispatch [::state/handle-change %])
        handle-create #(re-frame/dispatch [::state/handle-create %])
        handle-select #(re-frame/dispatch [::state/handle-select %])
        handle-ref #(reset! ref %)
        handle-stop-playing #(re-frame/dispatch [::state/stop-playing ref])]
    (when (some? audio-url)
      ^{:key (str action-path "--" audio-url)}
      [ui/audio-wave {:url         audio-url
                      :initial-regions initial-regions
                      :script-data script-data
                      :on-change   handle-change
                      :on-create   handle-create
                      :on-pause    handle-stop-playing
                      :on-select   handle-select
                      :ref         handle-ref
                      :class-name  class-name}])))

(defn zoom
  [{:keys [class-name wave]}]
  (let [handle-zoom-in #(re-frame/dispatch [::state/zoom-in wave])
        handle-zoom-out #(re-frame/dispatch [::state/zoom-out wave])]
    [:div {:class-name class-name}
     [ui/button {:on-click   handle-zoom-out
                 :title      "Zoom out"
                 :icon       "zoom-out"
                 :shape      "rounded"
                 :color      "grey-3"
                 :class-name "zoom-button"}]
     [ui/button {:on-click   handle-zoom-in
                 :title      "Zoom in"
                 :icon       "zoom-in"
                 :shape      "rounded"
                 :color      "grey-3"
                 :class-name "zoom-button"}]]))

(defn word
  [{:keys [wave]}]
  (let [handle-change #(re-frame/dispatch [::state/change-selected-word %])
        value (-> @(re-frame/subscribe [::state/selected-region]) :data (get "word") (or ""))
        error @(re-frame/subscribe [::state/selected-word-error])
        remove-word #(re-frame/dispatch [::state/remove-selected-word wave])]
    [:div {:class-name "transcription-word"}
     [ui/input {:on-change  handle-change
                :placeholder "Enter word"
                :value value
                :error error}]
     [ui/button {:on-click remove-word} "Remove"]]))

(defn- init-mouse-event
  [{:keys [ref on-right-click]}]
  (.addEventListener ref "contextmenu" (fn [event]
                                         (.preventDefault event)
                                         (on-right-click event))))

(defn- transcription-editor
  []
  (let [audio-wave-control (atom nil)
        ref (atom nil)
        handle-key-down (fn [e]
                          (when (and (.-ctrlKey e) (= (.-code e) "Space"))
                            (.preventDefault e)
                            (re-frame/dispatch [::state/start-selected-playing audio-wave-control]))
                          (when (and (.-ctrlKey e) (= (.-code e) "Backspace"))
                            (.preventDefault e)
                            (re-frame/dispatch [::state/remove-selected-word audio-wave-control])))]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (re-frame/dispatch [::state/init (r/props this)])
        (.addEventListener @ref "keydown" handle-key-down false))

      :component-will-unmount
      (fn []
        (re-frame/dispatch [::state/reset])
        (.removeEventListener @ref "keydown" handle-key-down))

      :reagent-render
      (fn [{:keys [action-path]}]
        (let [{:keys [in-progress]} @(re-frame/subscribe [::state/window-state])]
          [:div {:class-name (ui/get-class-name {"widget--transcription-editor" true})
                 :ref #(when (some? %)
                         (reset! ref %)
                         (init-mouse-event {:ref % :on-right-click (fn [e]
                                                                     (re-frame/dispatch [::state/open-context-menu audio-wave-control e]))}))
                 :tab-index 0}
           [wave {:action-path action-path
                  :class-name  "audio-editor--wave"
                  :ref         audio-wave-control}]
           (if-not in-progress
             [:<>
              [zoom {:wave       audio-wave-control
                     :class-name "audio-editor--zoom"}]
              [:div.audio-editor--play
               [:div "Region"]
               [play-selected-button {:wave audio-wave-control}]
               [:div "Audio"]
               [play-button {:wave audio-wave-control}]]
              [word {:wave audio-wave-control}]]
             [ui/loading-overlay])]))})))

(defn- reset-transcription-window
  []
  (let [open? @(re-frame/subscribe [::state/reset-transcription-window-open?])
        close-window #(re-frame/dispatch [::state/close-reset-transcription-window])
        handle-change #(re-frame/dispatch [::state/change-reset-transcription-value %])
        value @(re-frame/subscribe [::state/reset-transcription-value])
        reset-transcription #(re-frame/dispatch [::state/reset-transcription])]
    (when open?
      [ui/dialog {:title "Reset transcription text"
                  :class-name "reset-transcription-dialog"
                  :actions [:<>
                            [ui/button {:color    "blue-1"
                                        :on-click close-window}
                             "Cancel"]
                            [ui/button {:on-click reset-transcription}
                             "Save"]]}
       [:div {:class-name "transcription-text"}
        [ui/text-area {:on-change  handle-change
                       :value value
                       :placeholder "Enter transcription text"
                       :class-name  "text-component"}]]])))

(defn- insert-transcription-window
  []
  (let [open? @(re-frame/subscribe [::state/insert-transcription-window-open?])
        close-window #(re-frame/dispatch [::state/close-insert-transcription-window])
        handle-change #(re-frame/dispatch [::state/change-insert-transcription-value %])
        value @(re-frame/subscribe [::state/insert-transcription-value])
        insert-transcription #(re-frame/dispatch [::state/insert-transcription])]
    (when open?
      [ui/dialog {:title "Insert transcription text"
                  :class-name "insert-transcription-dialog"
                  :actions [:<>
                            [ui/button {:color    "blue-1"
                                        :on-click close-window}
                             "Cancel"]
                            [ui/button {:on-click insert-transcription}
                             "Save"]]}
       [:div {:class-name "transcription-text"}
        [ui/text-area {:on-change  handle-change
                       :value value
                       :placeholder "Enter transcription text"
                       :class-name  "text-component"}]]])))

(defn- context-menu
  []
  (let [{:keys [open? pos-x pos-y]} @(re-frame/subscribe [::state/context-menu-state])
        close-window #(re-frame/dispatch [::state/close-context-menu])
        open-insert-transcription-window #(re-frame/dispatch [::state/open-insert-transcription-window])]
    (when open?
      [:div {:class-name "transcription-context-menu"
             :style {:left (str pos-x "px") :top (str pos-y "px")}}
       [ui/list {:class-name "transcription-context-menu-list"}
        [ui/list-item {:class-name "transcription-context-menu-list-item"
                       :name    "Insert transcription"
                       :dense?  true
                       :on-click open-insert-transcription-window}]
        [ui/list-item {:class-name "transcription-context-menu-list-item"
                       :name    "Cancel"
                       :dense?  true
                       :on-click close-window}]]])))

(defn edit-transcription-window
  [{:keys [on-save]}]
  (let [{:keys [open?]} @(re-frame/subscribe [::state/window-state])
        close-window #(re-frame/dispatch [::state/close-window])
        save-script #(re-frame/dispatch [::state/save-script on-save])
        reset-transcription #(re-frame/dispatch [::state/open-reset-transcription-window])]
    (when open?
      [ui/dialog {:title "Edit transcription"
                  :class-name "edit-transcription-dialog"
                  :actions [:<>
                            [ui/button {:color    "blue-1"
                                        :on-click reset-transcription}
                             "Reset Transcription"]
                            [ui/button {:color    "blue-1"
                                        :on-click close-window}
                             "Cancel"]
                            [ui/button {:on-click save-script}
                             "Save"]]}
       [transcription-editor]
       [reset-transcription-window]
       [insert-transcription-window]
       [context-menu]])))
