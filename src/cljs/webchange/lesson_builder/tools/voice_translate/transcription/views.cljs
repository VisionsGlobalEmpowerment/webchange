(ns webchange.lesson-builder.tools.voice-translate.transcription.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.voice-translate.transcription.state :as state]
    [webchange.lesson-builder.widgets.action-audio-editor.views :refer [action-audio-editor auto-select-button]]
    [webchange.lesson-builder.widgets.audio-add.views :refer [audio-add]]
    [webchange.lesson-builder.widgets.audio-list.views :refer [audio-list]]
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
  []
  (let [handle-change #(re-frame/dispatch [::state/change-selected-word %])
        value (-> @(re-frame/subscribe [::state/selected-region]) :data (get "word"))]
    [:div {:class-name "transcription-word"}
     [ui/input {:on-change  handle-change
                :value value}]]))

(defn- transcription-editor
  []
  (let [audio-wave-control (atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (re-frame/dispatch [::state/init (r/props this)]))

      :component-will-unmount
      (fn []
        (re-frame/dispatch [::state/reset]))

      :reagent-render
      (fn [{:keys [action-path]}]
        (let [{:keys [in-progress]} @(re-frame/subscribe [::state/window-state])]
          [:div {:class-name (ui/get-class-name {"widget--transcription-editor" true})}
           [wave {:action-path action-path
                  :class-name  "audio-editor--wave"
                  :ref         audio-wave-control}]
           (if-not in-progress
             [:<>
              [zoom {:wave       audio-wave-control
                     :class-name "audio-editor--zoom"}]
              [play-button {:wave       audio-wave-control
                            :class-name "audio-editor--play"}]
              [word]]
             [ui/loading-overlay])]))})))

(defn edit-transcription-window
  []
  (let [{:keys [open?]} @(re-frame/subscribe [::state/window-state])
        close-window #(re-frame/dispatch [::state/close-window])
        save-script #(re-frame/dispatch [::state/save-script])]
    (when open?
      [ui/dialog {:title "Edit transcription"
                  :class-name "edit-transcription-dialog"
                  :actions [:<>
                            [ui/button {:color    "blue-1"
                                        :on-click close-window}
                             "Cancel"]
                            [ui/button {:on-click save-script}
                             "Save"]]}
       [transcription-editor]])))
