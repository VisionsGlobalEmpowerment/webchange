(ns webchange.interpreter.components.video
  (:require
    [react-konva :refer [Group Image]]))

(defonce state (atom {:animation nil
                      :image     nil
                      :video     nil
                      :act       "pause"}))

(defn play
  []
  (let [{:keys [animation video]} @state]
    (.play video)
    (.start animation)))

(defn pause
  []
  (let [{:keys [animation video]} @state]
    (.pause video)
    (.stop animation)))

(defn update-image-size
  []
  (let [{:keys [image video]} @state
        image-position (->> image (.position) js->clj)
        image-width (.width image)
        image-height (.height image)
        video-width (.-videoWidth video)
        video-height (.-videoHeight video)
        new-image-height (/ (* image-width video-height) video-width)]
    (.height image new-image-height)
    (.position image (clj->js {:x (:x image-position)
                               :y (+ (:y image-position) (/ (- image-height new-image-height) 2))}))))

(defn constructor
  [src act on-end]
  (let [video (.createElement js/document "video")]
    (.addEventListener video "loadedmetadata" update-image-size)
    (set! (.-src video) src)
    (when (fn? on-end) (set! (.-onended video) on-end))
    (swap! state assoc :video video)
    (swap! state assoc :act act)))

(defn component-did-mount
  []
  (let [layer (.getLayer (:image @state))
        animation (js/Konva.Animation. #() layer)]
    (swap! state assoc :animation animation)
    (when (= "play" (:act @state)) (play))))

(defn component-did-update
  []
  (update-image-size)
  (case (:act @state)
    "play" (play)
    "pause" (pause)
    (throw (str "Unknown state: " (:act @state)))))

(defn component-will-unmount
  []
  (pause))

(defn render
  [{:keys [x y width height src act on-end]}]
  (constructor src act on-end)
  (let [video (:video @state)]
    [:> Group {:x      x
               :y      y
               :width  width
               :height height}
     [:> Image {:x      0
                :y      0
                :width  width
                :height height
                :image  video
                :ref    #(swap! state assoc :image %)}]]))

(def video
  (with-meta render
             {:component-did-mount    component-did-mount
              :component-did-update   component-did-update
              :component-will-unmount component-will-unmount}))
