(ns webchange.editor-v2.dialog.dialog-form.views-record-audio
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.audio-utils.recorder :as recorder]
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
  []
  (r/with-let [current-state (r/atom :start-record)
               interval 87
               interval-id (r/atom nil)
               timing (r/atom 0)]
    (let [handle-start-record (fn []
                                (reset! interval-id (js/setInterval #(swap! timing + interval) interval))
                                (recorder/start #(reset! current-state :stop-record)))
          handle-stop-record (fn []
                               (js/clearInterval @interval-id)
                               (reset! timing 0)
                               (recorder/stop
                                 #(do (reset! current-state :start-record)
                                      (re-frame/dispatch [::translator-form.audios/upload-audio %
                                                          {} [["type" "blob"] ["blob-type" "audio"]]]))))]
      (case @current-state
        :start-record [:div.record-audio
                       [record-button {:on-click handle-start-record}]]
        :stop-record [:div.record-audio
                      [stop-button {:on-click handle-stop-record}]
                      [info {:value @timing}]]
        [:div "..."]))))
