(ns webchange.common.anim
  (:require
    [reagent.core :as r]
    [react-konva :refer [Shape]]
    [konva :refer [Animation]]
    [spine :as s]
    [spine-player :as sp]
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

(defn set-slot
  [skeleton slot-name slot-attachment-name image region-options attachment-options]
  (let [texture (.get spine-manager image)
        region (doto (s/TextureAtlasRegion.)
                 (set! -x (get region-options :x 0))
                 (set! -y (get region-options :y 0))
                 (set! -width (get region-options :width 100))
                 (set! -height (get region-options :height 100))
                 (set! -originalWidth (get region-options :original-width 100))
                 (set! -originalHeight (get region-options :original-height 100))
                 (set! -offsetX (get region-options :offset-x 0))
                 (set! -offsetY (get region-options :offset-y 0))
                 (set! -index (get region-options :index -1))
                 (set! -texture texture))
        attachment (doto (s/RegionAttachment. (get attachment-options :name "element"))
                     (.setRegion region)
                     (set! -x (get attachment-options :x 0))
                     (set! -y (get attachment-options :y 0))
                     (set! -scaleX (get attachment-options :scale-x 1))
                     (set! -scaleY (get attachment-options :scale-y 1))
                     (set! -width (get attachment-options :width 100))
                     (set! -height (get attachment-options :height 100))
                     (.updateOffset))
        slot-index (.findSlotIndex skeleton slot-name)
        new-skin (s/Skin. image)]
    (.setAttachment new-skin slot-index (or slot-attachment-name "boxes") attachment)
    (.setSkin skeleton new-skin)
    (.setToSetupPose skeleton)))

(defn anim
  [{:keys [name anim speed on-mount start mix skin anim-offset meshes]
    :or   {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0} meshes false} :as a}]
  (r/with-let  [atlas (texture-atlas name)
                atlas-loader (s/AtlasAttachmentLoader. atlas)
                skeleton-json (s/SkeletonJson. atlas-loader)
                skeleton-data (.readSkeletonData skeleton-json (.get spine-manager (str "/raw/anim/" name "/skeleton.json")))
                skeleton (doto (s/Skeleton. skeleton-data)
                           (set! -scaleY -1)
                           (set! -x (:x anim-offset))
                           (set! -y (:y anim-offset))
                           (.setSkinByName skin)
                           (.setToSetupPose))
                animation-state-data (doto (s/AnimationStateData. (.-data skeleton))
                                       (set! -defaultMix mix))
                animation-state (doto (s/AnimationState. animation-state-data)
                                  (.setAnimation 0 anim true))
                scene-fn (fn [context shape]
                             (.update animation-state (* (.getAttr shape "timeDiff") speed))
                             (.apply animation-state skeleton)
                             (.updateWorldTransform skeleton)
                             (let [skeleton-renderer (get-renderer context)]
                               (set! (.-triangleRendering skeleton-renderer) meshes)
                               (.draw skeleton-renderer skeleton)))
                ref-fn (fn [ref] (when ref
                                   (.setStrokeHitEnabled ref false)
                                   (.shadowForStrokeEnabled ref false)
                                   (.perfectDrawEnabled ref false)
                                   (when start (start-animation ref))
                                   (on-mount {:animation-state animation-state :skeleton skeleton :shape ref})
                                   ))]
      [:> Shape {:time-diff  0
                 :scene-func scene-fn
                 :ref        ref-fn}]))

(defn init-spine-player [element animation]
  (let [params {"jsonUrl"      (str "/raw/anim/" animation "/skeleton.json")
                "atlasUrl"     (str "/raw/anim/" animation "/skeleton.atlas")
                "showControls" false}]
    (sp/player. element (clj->js params))))

(def animations {:vera       {:name       "vera"
                              :width      1800
                              :height     2558
                              :speed      0.3
                              :scale-x    0.2
                              :scale-y    0.2
                              :skins      ["default"]
                              :animations ["idle" "clapping_1clap" "clapping_finish" "clapping_start" "jump" "jump+clapping" "talk" "talk_eeeee"]}
                 :vera-45    {:name       "vera-45"
                              :width      758
                              :height     1130
                              :speed      0.3
                              :scale-x    0.2
                              :scale-y    0.2
                              :skins      ["vera" "default" "1_boy" "2_girl" "3_girl"]
                              :animations ["idle" "go_back" "go_front" "volley_call" "volley_idle" "volley_throw"]}
                 :vera-90    {:name       "vera-90"
                              :width      727
                              :height     1091
                              :speed      0.3
                              :scale-x    0.4
                              :scale-y    0.4
                              :skins      ["default"]
                              :animations ["idle" "V_idle" "V_down" "V_up" "bike_ride" "bike_jump" "bike_turndown" "bike_turnup" "run" "run_jump" "swing"]}
                 :senoravaca {:name       "senoravaca"
                              :width      351
                              :height     77
                              :speed      0.3
                              :scale-x    1
                              :scale-y    1
                              :skins      ["vaca" "lion"]
                              :animations ["idle" "idle2" "clapping_1clap" "clapping_finish" "clapping_start" "hand" "talk"]}
                 :mari       {:name       "mari"
                              :width      737
                              :height     511
                              :speed      0.35
                              :scale-x    0.5
                              :scale-y    0.5
                              :skins      ["default"]
                              :animations ["idle" "go" "prev" "talk"]}
                 :rock       {:name       "rock"
                              :width      591
                              :height     681
                              :speed      0.35
                              :scale-x    0.6
                              :scale-y    0.6
                              :skins      ["female" "male" "default"]
                              :animations ["idle" "talk"]}
                 :book       {:name       "book"
                              :width      1439
                              :height     960
                              :speed      0.35
                              :scale-x    1
                              :scale-y    1
                              :skins      ["default"]
                              :animations ["close_idle" "idle" "open_book" "page"]}
                 :hat        {:name       "hat"
                              :width      1800
                              :height     2558
                              :speed      0.3
                              :scale-x    0.3
                              :scale-y    0.3
                              :skins      ["default"]
                              :animations ["catch" "idle" "talk"]}
                 :pinata     {:name       "hat"
                              :width      678
                              :height     899
                              :speed      0.3
                              :scale-x    0.3
                              :scale-y    0.3
                              :skins      ["default"]
                              :animations ["empty" "empty_idle" "hit" "idle"]}
                 :boxes      {:name       "boxes"
                              :width      771
                              :height     1033
                              :speed      0.3
                              :scale-x    0.3
                              :scale-y    0.3
                              :skins      ["default"
                                           "8" "airplane" "bear" "bee" "dino" "ear" "empty"
                                           "eyes" "fire" "iguana" "insect" "island" "magnet"
                                           "qwestion" "sheep" "spider" "squirrel" "tree" "wood"
                                           "grapes" "one" "fingernail" "unicorn" "uniform"
                                           "elephant" "salad" "building" "star" "nurse"
                                           "hand" "mama" "monkey" "apple" "mountain"
                                           "snake" "chair" "sun" "watermelon" "soup"
                                           "ball" "factory" "duck" "potatoes" "fish"
                                           "lion" "lemon" "moon" "pencil" "milk"
                                           "tomato" "phone" "turtle" "train" "shark"
                                           "diamond" "finger" "dragon" "teeth"
                                           "frog" "rock" "rose" "mouse" "queen"
                                           "home" "bed" "car" "candle" "crocodile"
                                           "toothbrush" "pig" "eyebrow" "onion" "five"
                                           "baby" "cloud" "nest" "black" "orange"
                                           "flower" "flute" "strawberry" "fruits" "arrow"
                                           "bicycle" "boat" "whale" "mouth"
                                           "garden" "juice" "toys" "jamon" "giraffe"
                                           "cat" "guitar" "earth" "chicken" "glove"
                                           "twins" "giant" "sunflower" "gemstone" "chalk"
                                           "violin" "cow" "baby_cow" "window" "green"
                                           "wrench" "call" "rain" "lama" "cry"]
                              :animations ["idle1" "idle2" "idle3"
                                           "idle_fly1" "idle_fly2" "idle_fly3"
                                           "jump" "jump2"
                                           "come" "come2"
                                           "leave" "leave2"
                                           "wood" "wood2"]}})
