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
  [{:keys [name anim speed on-mount start mix skin anim-offset meshes]
    :or {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0} meshes false} :as a}]
  (js/console.log "outer:")
  (js/console.log a)
  (let [atlas (texture-atlas name)
        _ (js/console.log "texture:")
        atlas-loader (s/AtlasAttachmentLoader. atlas)
        skeleton-json (s/SkeletonJson. atlas-loader)
        skeleton-data (.readSkeletonData skeleton-json (.get spine-manager (str "/raw/anim/" name "/skeleton.json")))
        skeleton (s/Skeleton. skeleton-data)
        animation-state-data (s/AnimationStateData. (.-data skeleton))
        _ (set! (.-defaultMix animation-state-data) mix)
        animation-state (s/AnimationState. animation-state-data)]
    (set! (.-scaleY skeleton) -1)
    (set! (.-x skeleton) (+ (.-x skeleton) (:x anim-offset)))
    (set! (.-y skeleton) (+ (.-y skeleton) (:y anim-offset)))
    (fn [{:keys [name anim speed on-mount start mix skin anim-offset meshes] :or {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0} meshes false} :as a}]
      (js/console.log "inner:")
      (js/console.log a)
      (.setAnimation animation-state 0 anim true)
      (.setSkinByName skeleton skin)
      (.setToSetupPose skeleton)
      [:> Shape {:time-diff  0
                 :scene-func (fn [context shape]
                               (.update animation-state (* (.getAttr shape "timeDiff") speed))
                               (.apply animation-state skeleton)
                               (.updateWorldTransform skeleton)
                               (let [skeleton-renderer (s/canvas.SkeletonRenderer. context)]
                                 (set! (.-triangleRendering skeleton-renderer) meshes)
                                 (.draw skeleton-renderer skeleton)))
                 :ref        (fn [ref] (if ref
                                         (do
                                           (when start (start-animation ref))
                                           (on-mount {:animation-state animation-state :skeleton skeleton :shape ref}))
                                         ))}])))

(defn init-spine-player [element animation]
  (let [params {"jsonUrl" (str "/raw/anim/" animation "/skeleton.json")
                "atlasUrl" (str "/raw/anim/" animation "/skeleton.atlas")
                "showControls" false}]
    (s/SpinePlayer. element (clj->js params))))

(def animations {:vera {:name "vera"
                        :width 1800
                        :height 2558
                        :skins ["default"]
                        :animations ["idle" "clapping_1clap" "clapping_finish" "clapping_start" "jump" "jump+clapping" "talk" "talk_eeeee"]}
                 :vera-go {:name "vera-go"
                           :width 752
                           :height 1175
                           :skins ["default"]
                           :animations ["go_back" "go_front" "go_prew"]}
                 :vera-90 {:name "vera-90"
                           :width 727
                           :height 1091
                           :skins ["default"]
                           :animations ["2"]}
                 :senoravaca {:name "senoravaca"
                              :width 351
                              :height 77
                              :skins ["default"]
                              :animations ["idle" "clapping_1clap" "clapping_finish" "clapping_start" "hand" "talk"]}
                 :mari {:name "mari"
                        :width 737
                        :height 511
                        :skins ["default"]
                        :animations ["idle" "go" "prev" "talk"]}
                 :rock {:name "rock"
                        :width 591
                        :height 681
                        :skins ["default"]
                        :animations ["idle" "talk"]}
                 :boxes {:name "boxes"
                         :width 771
                         :height 1033
                         :skins ["default" "8" "airplane" "bear" "bee" "dino" "ear" "empty" "eyes" "fire" "iguana" "insect" "island" "magnet" "qwestion" "sheep" "spider" "squirrel" "tree" "wood"]
                         :animations ["idle1" "idle2" "idle3" "idle_fly1" "idle_fly2" "idle_fly3" "jump" "jump2" "come" "come2" "leave" "leave2" "wood" "wood2"]}})