(ns webchange.editor-v2.translator.translator-form.audio-assets.add-audio.recorder)

(def stream (atom nil))
(def recorder (atom nil))
(def recorded-audio-chunks (atom nil))

(defn- get-audio-stream
  [callback]
  (-> (.-mediaDevices js/navigator)
      (.getUserMedia (clj->js {:audio true}))
      (.then callback)))

(defn- stop-stream
  [stream]
  (-> (.getTracks stream)
      (.forEach #(.stop %))))

(defn- handle-data-available
  [event]
  (->> (.-data event)
       (conj @recorded-audio-chunks )
       (reset! recorded-audio-chunks )))

(defn start
  [on-success]
  (get-audio-stream (fn [audio-stream]
                      (reset! stream audio-stream)
                      (reset! recorder (js/MediaRecorder. @stream))
                      (reset! recorded-audio-chunks [])
                      (.addEventListener @recorder "dataavailable" handle-data-available)
                      (.start @recorder)
                      (on-success))))

(defn stop
  [on-success]
  (.addEventListener @recorder
                     "stop"
                     (fn []
                       (let [audio-blob (js/Blob. @recorded-audio-chunks)]
                         (on-success audio-blob))))
  (.stop @recorder)
  (stop-stream @stream))
