(ns webchange.editor-v2.wizard.activity-template.utils-video)

(defn- pause
  [video]
  (.pause video))

(defn- play
  [video]
  (.play video))

(defn- get-current-time
  [video]
  (.-currentTime video))

(defn set-current-time
  [video frame]
  (set! (.-currentTime video) frame))

(defn- add-listener
  [video event handler]
  (.addEventListener video event handler))

(defn- remove-listener
  [video event handler]
  (.removeEventListener video event handler))

(defn- add-time-update-listener
  [video handler]
  (add-listener video "timeupdate" handler))

(defn- remove-time-update-listener
  [video handler]
  (remove-listener video "timeupdate" handler))

(defn play-range
  [video {:keys [from to]}]
  (let [handler (atom nil)
        time-update-handler (fn []
                              (when (> (get-current-time video) to)
                                (pause video)
                                (remove-time-update-listener video @handler)))]
    (reset! handler time-update-handler)
    (set-current-time video from)
    (add-time-update-listener video time-update-handler)
    (play video)))
