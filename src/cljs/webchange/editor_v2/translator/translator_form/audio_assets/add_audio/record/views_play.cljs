(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-play
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.utils-audio :as audio]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.record.views-layout :refer [panel-layout]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.record-panel :as add-audio.record-panel]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.select-method  :as add-audio.select-method]))

(defn- get-styles
  []
  {:play-button   {:padding       "25px"
                   :border-radius "50%"
                   :position      "relative"}
   :play-progress {:position "absolute"}
   :play-icon     {:font-size "70px"}
   :action-button {:margin "5px"}})

(defn- button
  [{:keys [icon show-progress? on-click]}]
  (let [styles (get-styles)]
    [ui/button {:on-click on-click
                :style    (:play-button styles)}
     (when show-progress?
       [ui/circular-progress {:color     "secondary"
                              :size      120
                              :thickness 1
                              :style     (:play-progress styles)}])
     [icon {:style (:play-icon styles)}]]))

(defn- play-record-button
  []
  (let [audio-object (atom nil)
        button-state (r/atom :play)
        handle-play-click #(do (audio/play @audio-object)
                               (reset! button-state :stop))
        handle-stop-click #(do (audio/stop @audio-object)
                               (reset! button-state :play))]
    (r/create-class
      {:display-name "audio-play-record-panel-form"

       :component-did-mount
                     (fn [this]
                       (let [{:keys [audio-blob]} (r/props this)]
                         (reset! audio-object (audio/create audio-blob))
                         (audio/on-ended @audio-object handle-stop-click)))

       :reagent-render
                     (fn [{:keys []}]
                       (case @button-state
                         :play [button {:icon     ic/play-arrow
                                        :on-click handle-play-click}]
                         :stop [button {:icon           ic/stop
                                        :on-click       handle-stop-click
                                        :show-progress? true}]))})))

(defn- panel-actions
  []
  (let [actions [{:text    "cancel recording"
                  :icon    ic/backspace
                  :handler (fn [] (re-frame/dispatch [::add-audio.select-method/show-select-method-panel]))}
                 {:text    "re-record"
                  :icon    ic/cached
                  :handler (fn [] (re-frame/dispatch [::add-audio.record-panel/show-start-record-panel]))}
                 {:text    "done"
                  :icon    ic/done
                  :handler (fn [] (re-frame/dispatch [::add-audio.record-panel/show-set-record-params-panel]))}]
        styles (get-styles)]
    [:div
     (for [{:keys [text icon handler]} actions]
       ^{:key text}
       [ui/tooltip {:title text}
        [ui/icon-button {:on-click handler
                         :style    (:action-button styles)}
         [icon]]])]))

(defn play-record-panel
  [{:keys [audio-blob]}]
  [panel-layout {:actions (r/as-element [panel-actions])}
   [play-record-button {:audio-blob audio-blob}]])
