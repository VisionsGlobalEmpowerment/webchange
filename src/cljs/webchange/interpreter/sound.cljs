(ns webchange.interpreter.sound
  (:require
    [webchange.interpreter.pixi :refer [sound SoundFilter]]
    [webchange.resources.manager :as resources]))

(defonce app-volume (atom {:fx    {:default-value 0.25
                                   :filter        nil
                                   :gain          nil}
                           :music {:default-value 0.08
                                   :filter        nil
                                   :gain          nil}}))

(defonce registered-audio (atom []))

(defn- register-audio!
  [audio]
  (swap! registered-audio conj audio)
  audio)

(defn stop-all-audio!
  []
  (doseq [audio @registered-audio]
    (.stop audio))
  (reset! registered-audio []))

(defn- set-volume
  [gain value]
  (set! (.. gain -gain -value) value))

(defn music-volume
  [value]
  (-> @app-volume
      (get-in [:music :gain])
      (set-volume value)))

(defn effects-volume
  [value]
  (-> @app-volume
      (get-in [:fx :gain])
      (set-volume (* value 5))))

(defn- create-volume-filter
  []
  (let [ctx (.. sound -context -audioContext)
        gain (.createGain ctx)
        filter (SoundFilter. gain)]
    {:gain   gain
     :filter filter}))

(defn init
  []
  (when (or (nil? (get-in @app-volume [:fx :filter]))
            (nil? (get-in @app-volume [:music :filter])))
    (swap! app-volume update :fx merge (create-volume-filter))
    (swap! app-volume update :music merge (create-volume-filter))
    (music-volume (get-in @app-volume [:fx :default-value]))
    (effects-volume (get-in @app-volume [:music :default-value]))))

(defn- play
  [resource {:keys [loop on-ended start offset duration volume]
             :or   {loop     false
                    on-ended #()
                    start    0
                    volume   1}}]
  (let [params (cond-> {:loop     loop
                        :complete on-ended
                        :start    start}
                       (some? duration) (assoc :end (+ start duration)))
        music-audio? (:loop params)]
    (.setTimeout js/window
                 (fn []
                   (let [filter (if music-audio?
                                  (get-in @app-volume [:music :filter])
                                  (get-in @app-volume [:fx :filter]))]
                     (doto (.-sound resource)
                       (set! -filters (clj->js [filter]))
                       (set! -volume volume)
                       (.play (clj->js params)))))
                 offset)))

(defn- stop
  [resource]
  (.stop (.-sound resource)))

(defn- wrap-audio
  [resource]
  (clj->js {:stop (fn [] (stop resource))}))

(defn play-audio
  [{:keys [key] :as params}]
  (let [resource (resources/get-resource key)]
    (when (nil? resource) (-> (str "Resource for audio <" key "> is not defined") js/Error. throw))
    (play resource params)
    (->> (wrap-audio resource)
         (register-audio!)
         (js/Promise.resolve))))
