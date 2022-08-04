(ns webchange.editor-v2.dialog.components.record-audio.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.utils.audio-recorder :as recorder]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- record-button
  [{:keys [on-click]}]
  [icon-button {:icon     "mic"
                :color    "primary"
                :size     "big"
                :on-click on-click}])

(defn- stop-button
  [{:keys [on-click]}]
  [icon-button {:icon     "stop"
                :color    "secondary"
                :size     "big"
                :on-click on-click}])

(defn- ms->time
  [ms]
  {:min (-> ms (/ 60000) (Math/floor))
   :sec (-> ms (rem 60000) (/ 1000) (Math/floor))
   :ms  (-> ms (rem 1000))})

(defn- to-str
  [value]
  (if (< value 10)
    (str "0" value)
    (str value)))

(defn- info
  [{:keys [file-name value]
    :or   {file-name "New Record"}}]
  (let [{:keys [min sec ms]} (ms->time value)]
    [:div.info
     [:span.name "New Record"]
     [:span.time (str (to-str min) ":" (to-str sec) "," ms)]]))

(defn record-audio
  [{:keys [on-start-record on-stop-record]}]
  (r/with-let [current-state (r/atom :start-record)
               interval 87
               interval-id (r/atom nil)
               timing (r/atom 0)]
    (let [handle-start-record (fn []
                                (reset! interval-id (js/setInterval #(swap! timing + interval) interval))
                                (recorder/start #(reset! current-state :stop-record))
                                (when (fn? on-start-record)
                                  (on-start-record)))
          handle-stop-record (fn []
                               (js/clearInterval @interval-id)
                               (reset! timing 0)
                               (recorder/stop
                                 #(do (reset! current-state :start-record)
                                      (re-frame/dispatch [::translator-form.audios/upload-audio %
                                                          {} [["type" "blob"] ["blob-type" "audio"]]])
                                      (when (fn? on-start-record)
                                        (on-stop-record)))))]
      (into [:div.record-audio-component]
            (case @current-state
              :start-record [[record-button {:on-click handle-start-record}]]
              :stop-record [[stop-button {:on-click handle-stop-record}]
                            [info {:value @timing}]]
              [[:div "..."]])))))
