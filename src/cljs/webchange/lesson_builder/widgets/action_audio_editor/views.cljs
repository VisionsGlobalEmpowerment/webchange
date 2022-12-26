(ns webchange.lesson-builder.widgets.action-audio-editor.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.action-audio-editor.state :as state]
    [webchange.ui.index :as ui]))

(defn play-button
  [{:keys [id class-name wave]}]
  (let [state @(re-frame/subscribe [::state/play-button-state id])
        handle-start-playing #(re-frame/dispatch [::state/start-playing id wave])
        handle-stop-playing #(re-frame/dispatch [::state/stop-playing id wave])
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

(defn rewind-buttons
  [{:keys [class-name wave]}]
  (let [handle-rewind-to-start #(re-frame/dispatch [::state/rewind-to-start wave])
        handle-rewind-to-end #(re-frame/dispatch [::state/rewind-to-end wave])]
    [:div {:class-name class-name}
     [ui/button {:title    "Rewind to start of selection"
                 :icon     "rewind-backward"
                 :color    "blue-1"
                 :on-click handle-rewind-to-start}]
     [ui/button {:title    "Rewind to end of selection"
                 :icon     "rewind-forward"
                 :color    "blue-1"
                 :on-click handle-rewind-to-end}]]))

(defn selection-options
  [{:keys [action-path class-name id]}]
  (let [control-id (str id "-selection-options")
        show? @(re-frame/subscribe [::state/show-selection-options? id action-path])
        options @(re-frame/subscribe [::state/selection-options id action-path])
        handle-change #(re-frame/dispatch [::state/handle-selection-option id action-path %])]
    (when show?
      [:div {:class-name class-name}
       [:label {:for control-id}
        "Text Selection"]
       [ui/select {:id         control-id
                   :options    options
                   :on-change  handle-change
                   :class-name "audio-editor--options--select"}]])))

(defn volume
  [{:keys [action-path class-name id]}]
  (let [value @(re-frame/subscribe [::state/current-volume id])
        handle-change #(re-frame/dispatch [::state/set-current-volume id %])
        handle-input #(re-frame/dispatch [::state/set-action-volume action-path %])]
    [:div {:class-name (ui/get-class-name {"audio-editor--volume" true
                                           class-name             (some? class-name)})}
     [ui/range {:label     "Volume"
                :value     value
                :on-change handle-change
                :on-input  handle-input}]]))

(defn wave
  [{:keys [id action-path class-name ref]}]
  (let [audio-url @(re-frame/subscribe [::state/audio-url action-path])
        audio-region @(re-frame/subscribe [::state/initial-region id])
        script-data @(re-frame/subscribe [::state/script-data id])
        handle-change #(re-frame/dispatch [::state/set-action-region action-path %])
        handle-ready #(do (re-frame/dispatch [::state/set-loading id false])
                          (re-frame/dispatch [::state/init-region id action-path])
                          (re-frame/dispatch [::state/load-audio-script id {:action-path action-path}]))
        handle-ref #(reset! ref %)
        handle-stop-playing #(re-frame/dispatch [::state/stop-playing id ref])]
    (when (some? audio-url)
      ^{:key (str action-path "--" audio-url)}
      [ui/audio-wave {:url         audio-url
                      :initial-regions [audio-region]
                      :single-region? true
                      :script-data script-data
                      :on-change   handle-change
                      :on-pause    handle-stop-playing
                      :on-ready    handle-ready
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

(defn action-audio-editor
  [{:keys [id]}]
  (let [audio-wave-control (atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (re-frame/dispatch [::state/init id (r/props this)]))

      :component-will-unmount
      (fn []
        (re-frame/dispatch [::state/reset id]))

      :reagent-render
      (fn [{:keys [action-path]}]
        (let [loading? @(re-frame/subscribe [::state/loading? id])]
          [:div {:class-name (ui/get-class-name {"widget--audio-editor" true})}
           [wave {:id          id
                  :action-path action-path
                  :class-name  "audio-editor--wave"
                  :ref         audio-wave-control}]
           (if-not loading?
             [:<>
              [zoom {:wave       audio-wave-control
                     :class-name "audio-editor--zoom"}]
              [volume {:id          id
                       :action-path action-path
                       :class-name  "audio-editor--volume"}]

              [rewind-buttons {:wave       audio-wave-control
                               :class-name "audio-editor--rewind"}]
              [play-button {:id         id
                            :wave       audio-wave-control
                            :class-name "audio-editor--play"}]
              [selection-options {:id          id
                                  :action-path action-path
                                  :class-name  "audio-editor--options"}]]
             [ui/loading-overlay])]))})))

(defn auto-select-button
  [{:keys [id action-path]}]
  (let [loading? @(re-frame/subscribe [::state/auto-select-loading? id])
        handle-click #(re-frame/dispatch [::state/auto-select-region id action-path])]
    [ui/button {:icon       "select"
                :shape      "rounded"
                :class-name (ui/get-class-name {"widget--audio-editor--auto-select"          true
                                                "widget--audio-editor--auto-select--loading" loading?})
                :disabled?  loading?
                :on-click   handle-click}
     "Auto Select"]))
