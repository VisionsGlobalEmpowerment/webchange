(ns webchange.common.anim
  (:require
    [reagent.core :as r]
    [react-konva :refer [Shape]]
    [konva :refer [Animation]]
    [spine :as s]
    ))

(def spine-manager (s/canvas.AssetManager.))

(defn load-anim-text
  [asset progress]
  (.loadText spine-manager (:url asset) #(swap! progress + (:size asset))))

(defn load-anim-texture
  [asset progress]
  (.loadTexture spine-manager (:url asset) #(swap! progress + (:size asset))))

(defn texture-atlas
  [name]
  (let [text (.get spine-manager (str "/raw/anim/" name "/skeleton.atlas"))]
    (s/TextureAtlas. text (fn [path] (.get spine-manager (str "/raw/anim/" name "/" path))))))

(defn start-animation
  [shape]
  (let [animation (Animation. (fn [frame] (.setAttr shape "timeDiff" (/ (.-timeDiff frame) 1000))) (.getLayer shape))]
    (.start animation)))

(defn anim
  [{:keys [name anim speed on-mount start mix skin anim-offset] :or {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0}}}]
  (let [atlas (texture-atlas name)
        atlas-loader (s/AtlasAttachmentLoader. atlas)
        skeleton-json (s/SkeletonJson. atlas-loader)
        skeleton-data (.readSkeletonData skeleton-json (.get spine-manager (str "/raw/anim/" name "/skeleton.json")))
        skeleton (s/Skeleton. skeleton-data)
        animation-state-data (s/AnimationStateData. (.-data skeleton))
        _ (set! (.-defaultMix animation-state-data) mix)
        animation-state (s/AnimationState. animation-state-data)]
    (set! (.-flipY skeleton) true)
    (set! (.-x skeleton) (+ (.-x skeleton) (:x anim-offset)))
    (set! (.-y skeleton) (+ (.-y skeleton) (:y anim-offset)))
    (fn [{:keys [name anim speed on-mount start mix skin anim-offset] :or {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0}}}]
      (.setAnimation animation-state 0 anim true)
      (.setSkinByName skeleton skin)
      [:> Shape {:time-diff  0
                 :scene-func (fn [context shape]
                               (.update animation-state (* (.getAttr shape "timeDiff") speed))
                               (.apply animation-state skeleton)
                               (.updateWorldTransform skeleton)
                               (let [skeleton-renderer (s/canvas.SkeletonRenderer. context)]
                                 (.draw skeleton-renderer skeleton)))
                 :ref        (fn [ref] (if ref
                                         (do
                                           (when start (start-animation ref))
                                           (on-mount {:animation-state animation-state :skeleton skeleton :shape ref}))
                                         ))}])))