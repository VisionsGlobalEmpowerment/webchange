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

(defonce renderers (atom {}))

(defn get-renderer [context]
  (when-not (get @renderers context)
    (swap! renderers assoc context (s/canvas.SkeletonRenderer. context)))
  (get @renderers context))

(defn anim
  [{:keys [name anim speed on-mount start mix skin anim-offset meshes]
    :or {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0} meshes false} :as a}]
  (let [atlas (texture-atlas name)
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
      (.setAnimation animation-state 0 anim true)
      (.setSkinByName skeleton skin)
      (.setToSetupPose skeleton)
      [:> Shape {:time-diff  0
                 :scene-func (fn [context shape]
                               (.update animation-state (* (.getAttr shape "timeDiff") speed))
                               (.apply animation-state skeleton)
                               (.updateWorldTransform skeleton)
                               (let [skeleton-renderer (get-renderer context)]
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
                        :speed 0.3
                        :scale-x 0.2
                        :scale-y 0.2
                        :skins ["default"]
                        :animations ["idle" "clapping_1clap" "clapping_finish" "clapping_start" "jump" "jump+clapping" "talk" "talk_eeeee"]}
                 :vera-go {:name "vera-go"
                           :width 752
                           :height 1175
                           :speed 0.5
                           :scale-x 0.2
                           :scale-y 0.2
                           :skins ["default"]
                           :animations ["go_back" "go_front" "go_prew"]}
                 :vera-45 {:name "vera-45"
                           :width 758
                           :height 1130
                           :speed 0.3
                           :scale-x 0.2
                           :scale-y 0.2
                           :skins ["vera" "default" "1_boy" "2_girl" "3_girl"]
                           :animations ["idle" "go_back" "go_front" "volley_call" "volley_idle" "volley_throw"]}
                 :vera-90 {:name "vera-90"
                           :width 727
                           :height 1091
                           :speed 0.3
                           :scale-x 0.4
                           :scale-y 0.4
                           :skins ["default"]
                           :animations ["2"]}
                 :senoravaca {:name "senoravaca"
                              :width 351
                              :height 77
                              :speed 0.3
                              :scale-x 1
                              :scale-y 1
                              :skins ["default"]
                              :animations ["idle" "clapping_1clap" "clapping_finish" "clapping_start" "hand" "talk"]}
                 :mari {:name "mari"
                        :width 737
                        :height 511
                        :speed 0.35
                        :scale-x 0.5
                        :scale-y 0.5
                        :skins ["default"]
                        :animations ["idle" "go" "prev" "talk"]}
                 :rock {:name "rock"
                        :width 591
                        :height 681
                        :speed 0.35
                        :scale-x 0.6
                        :scale-y 0.6
                        :skins ["default"]
                        :animations ["idle" "talk"]}
                 :book {:name "book"
                        :width 1439
                        :height 960
                        :speed 0.35
                        :scale-x 1
                        :scale-y 1
                        :skins ["default"]
                        :animations ["close_idle" "idle" "open_book" "page"]}
                 :boxes {:name "boxes"
                         :width 771
                         :height 1033
                         :speed 0.3
                         :scale-x 0.3
                         :scale-y 0.3
                         :skins ["default" "8" "airplane" "bear" "bee" "dino" "ear" "empty" "eyes" "fire" "iguana" "insect" "island" "magnet" "qwestion" "sheep" "spider" "squirrel" "tree" "wood"]
                         :animations ["idle1" "idle2" "idle3" "idle_fly1" "idle_fly2" "idle_fly3" "jump" "jump2" "come" "come2" "leave" "leave2" "wood" "wood2"]}})