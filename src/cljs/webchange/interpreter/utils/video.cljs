(ns webchange.interpreter.utils.video)

(defonce registered-video (atom []))

(defn- register-video!
  [video]
  (swap! registered-video conj video)
  video)

(defn stop-all-video!
  []
  (doseq [video @registered-video]
    (.pause video))
  (reset! registered-video []))

(defn set-current-time
  [video frame]
  (set! (.-currentTime video) frame))

(defn pause
  [video]
  (.pause video))

(defn play
  [video]
  (-> video
      register-video!
      .play))

(defn stop
  [video]
  (pause video)
  (set-current-time video 0))

(defn play-range
  ([video from to]
   (play-range video from to #()))
  ([video from to on-end]
   (set-current-time video from)
   (let [duration (-> (- to from) (* 1000))
         schedule-pause (fn [] (js/setTimeout #(do (pause video)
                                                   (on-end))
                                              duration))]
     (-> (play video)
         (.then schedule-pause)))))
