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
  [skeleton slot-name image region attachment]
  (let [region-defaults {:x 0 :y 0 :width 100 :height 100 :original-width 100 :original-height 100
                         :offset-x 0 :offset-y 0 :index -1}
        attachment-defaults {:width 100 :height 100 :scale-x 4 :scale-y 4}
        texture (.get spine-manager image)
        r (doto (s/TextureAtlasRegion.)
                 #_(set! -name "test-region")
                 (set! -x (get region :x 0))
                 (set! -y (get region :y 0))
                 (set! -width (get region :width 100))
                 (set! -height (get region :height 100))
                 (set! -originalWidth (get region :original-width 100))
                 (set! -originalHeight (get region :original-height 100))
                 (set! -offsetX (get region :offset-x 0))
                 (set! -offsetY (get region :offset-y 0))
                 (set! -index (get region :index -1))
                 (set! -texture texture))
        a (doto (s/RegionAttachment. (get attachment :name "element"))
                     (.setRegion r)
                     (set! -scaleX (get attachment :scale-x 4))
                     (set! -scaleY (get attachment :scale-y 4))
                     (set! -width (get attachment :width 100))
                     (set! -height (get attachment :height 100))
                     (.updateOffset))
        slot (.findSlot skeleton slot-name)]
    (.setAttachment slot a)))

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
                                   (js/console.log "inside ref" name)
                                   (.setStrokeHitEnabled ref false)
                                   (.shadowForStrokeEnabled ref false)
                                   (when start (start-animation ref))
                                   (on-mount {:animation-state animation-state :skeleton skeleton :shape ref})
                                   ))]
    (js/console.log "before shape" name skin)
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
