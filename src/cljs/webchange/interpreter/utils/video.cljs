(ns webchange.interpreter.utils.video)

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
