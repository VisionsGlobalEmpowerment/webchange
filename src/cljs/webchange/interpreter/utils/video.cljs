(ns webchange.interpreter.utils.video)

(defn- get-current-time
  [video]
  (.-currentTime video))

(defn set-current-time
  [video frame]
  (set! (.-currentTime video) frame))

(defn pause
  [video]
  (.pause video))

(defn play
  [video]
  (.play video))

(defn stop
  [video]
  (pause video)
  (set-current-time video 0))

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
  ([video from to]
   (play-range video from to #()))
  ([video from to on-end]
   (let [handler (atom nil)
         time-update-handler (fn []
                               (when (> (get-current-time video) to)
                                 (pause video)
                                 (remove-time-update-listener video @handler)
                                 (on-end)))]
     (reset! handler time-update-handler)
     (set-current-time video from)
     (add-time-update-listener video time-update-handler)
     (play video))))
