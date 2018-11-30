(ns webchange.common.anim
  (:require
    [reagent.core :as r]
    [react-konva :refer [Shape]]
    [konva :refer [Animation]]
    [spine :as s]
    [webchange.interpreter.events :as events]
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
  [name animation speed on-mount]
  (let [atlas (texture-atlas name)
        atlas-loader (s/AtlasAttachmentLoader. atlas)
        skeleton-json (s/SkeletonJson. atlas-loader)
        skeleton-data (.readSkeletonData skeleton-json (.get spine-manager (str "/raw/anim/" name "/skeleton.json")))
        skeleton (s/Skeleton. skeleton-data)
        animation-state-data (s/AnimationStateData. (.-data skeleton))
        _ (set! (.-defaultMix animation-state-data) 0.2)
        animation-state (s/AnimationState. animation-state-data)]
    (set! (.-flipY skeleton) true)
    (fn [name animation speed on-mount]
      (.setAnimation animation-state 0 animation true)
      [:> Shape {:time-diff  0
                 :scene-func (fn [context shape]
                               (.update animation-state (* (.getAttr shape "timeDiff") speed))
                               (.apply animation-state skeleton)
                               (.updateWorldTransform skeleton)
                               (let [skeleton-renderer (s/canvas.SkeletonRenderer. context)]
                                 (.draw skeleton-renderer skeleton)))
                 :ref        (fn [ref] (if ref
                                         (do
                                           (start-animation ref)
                                           (on-mount animation-state))
                                         ))}])))