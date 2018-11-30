(ns webchange.interpreter.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [react-spring :refer [Spring]]
    [reagent.core :as r]
    [react-konva :refer [Stage, Layer, Group, Rect]]
    [konva :refer [Tween]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :as anim]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]
    ))

(def assets (atom {}))

(defn put-data
  [data key]
  (swap! assets assoc (name key) data))

(defn get-data
  [key]
  (get @assets key))

(defn has-data
  [key]
  (contains? @assets key))

(defn create-tagged-key
  [tag key]
  (str tag "_" key))

(defn new-blob
  [arraybuffer]
  (js/Blob. [arraybuffer]))

(defn get-data-as-url
  [key]
  (let [object-url-key (create-tagged-key "object-url" key)]
    (when-not (has-data object-url-key)
      (-> key
          get-data
          new-blob
          js/URL.createObjectURL
          (put-data object-url-key)))
    (get-data object-url-key)))

(def host "/api")
(def resources "")

(defn get-course
  [course-id]
  (http/get (str host "/courses/" course-id) {:with-credentials? false}))

(defn get-scene
  [course-id scene-id]
  (http/get (str host "/courses/" course-id "/scenes/" scene-id) {:with-credentials? false}))


(defn get-total-size
  [assets]
  (->> assets
       (map :size)
       (reduce +)))

(defn load-asset
  [asset progress]
  (go (let [response (<! (http/get (str resources (:url asset)) {:response-type :array-buffer :with-credentials? false}))]
        (put-data (:body response) (:url asset))
        (swap! progress + (:size asset)))))

(defn load-assets
  [assets scene-id]
  (let [total (get-total-size assets)
        current-progress (atom 0)]
    (add-watch current-progress :inc
               (fn [_ _ _ n]
                 (re-frame/dispatch [::events/set-loading-progress [scene-id (Math/round (* n (/ 100 total)))]])
                 (if (>= n total)
                   (re-frame/dispatch [::events/set-scene-loaded [scene-id true]]))))
    (doseq [asset assets]
      (case (-> asset :type keyword)
        :anim-text (anim/load-anim-text asset current-progress)
        :anim-texture (anim/load-anim-texture asset current-progress)
        (load-asset asset current-progress)))))

(defn load-course
  [course-id cb]
  (go (let [course-response (<! (get-course course-id))
            course (:body course-response)]
        (cb course))))

(defn load-scene
  [course-id scene-id cb]
  (go (let [scene-response (<! (get-scene course-id scene-id))
            scene (:body scene-response)]
        (load-assets (:assets scene) scene-id)
        (cb scene))))

(defn length
  [cx cy x y]
  (Math/sqrt (+ (Math/pow (- cx x) 2) (Math/pow (- cy y) 2))))

(defn transition-duration
  [component to]
  (let [cx (-> component (.-attrs) (.-x))
        cy (-> component (.-attrs) (.-y))
        {:keys [x y duration speed]} to]
    (cond
      (> duration 0) duration
      (> speed 0) (/ (length cx cy x y) speed)
      :else (/ (length cx cy x y) 100))))

(defn interpolate
  [{:keys [component to on-ended]}]
  (let [duration (transition-duration @component to)
        params (-> to
                   (assoc :duration duration)
                   (assoc :onFinish on-ended))]
    (if (:loop to)
      (-> params
          (assoc :node @component)
          (assoc :onFinish (fn [] (this-as t (.reset t) (.play t))))
          clj->js
          Tween.
          .play)
      (.to @component (clj->js params)))
    ))