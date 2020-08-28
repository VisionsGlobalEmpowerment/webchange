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

(def started-animations (atom {}))

(defn- reset-animation!
  [uuid]
  (if-let [animation (get started-animations uuid)]
    (do
      (.stop animation)
      (swap! started-animations dissoc uuid))))

(defn start-animation
  [{:keys [animation-state skeleton shape]}]
  (let [uuid (random-uuid)
        animation (Animation. (fn [frame]
                                (if-let [layer (.getLayer shape)]
                                  (.batchDraw layer)
                                  (reset-animation! uuid))
                                (if (> 15 (.-frameRate frame))
                                  false
                                  (do
                                    (.update animation-state (/ (.-timeDiff frame) 1000))
                                    (.apply animation-state skeleton)
                                    (.updateWorldTransform skeleton)))))]
    (swap! started-animations assoc uuid animation)
    (.start animation)))

(defonce renderers (atom {}))

(defn get-renderer [context]
  (when-not (get @renderers context)
    (swap! renderers assoc context (s/canvas.SkeletonRenderer. context)))
  (get @renderers context))

(defn set-slot
  [skeleton animation-state slot-name slot-attachment-name image region-options attachment-options]
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
    (.setToSetupPose skeleton)
    (.apply animation-state skeleton)
    (.updateWorldTransform skeleton)))

(defn anim
  [{:keys [name anim speed on-mount start mix skin anim-offset meshes]
    :or   {mix 0.2 speed 1 skin "default" anim-offset {:x 0 :y 0} meshes false} :as a}]
  (r/with-let [atlas (texture-atlas name)
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
                                 (.setAnimation 0 anim true)
                                 (set! -timeScale speed))
               scene-fn (fn [context shape]
                          (let [skeleton-renderer (get-renderer context)]
                            (set! (.-triangleRendering skeleton-renderer) meshes)
                            (.draw skeleton-renderer skeleton)))
               ref-fn (fn [ref] (when ref
                                  (.setStrokeHitEnabled ref false)
                                  (.shadowForStrokeEnabled ref false)
                                  (.perfectDrawEnabled ref false)
                                  (.apply animation-state skeleton)
                                  (.updateWorldTransform skeleton)
                                  (let [animation {:animation-state animation-state :skeleton skeleton :shape ref}]
                                    (when start (start-animation animation))
                                    (on-mount animation))))]
    [:> Shape {:time-diff  0
               :scene-func scene-fn
               :ref        ref-fn}]))

(defn init-spine-player [element animation]
  (let [params {"jsonUrl"      (str "/raw/anim/" animation "/skeleton.json")
                "atlasUrl"     (str "/raw/anim/" animation "/skeleton.atlas")
                "showControls" false}]
    (sp/player. element (clj->js params))))

(def animations {:vera       {:width  380,
                              :height 537,
                              :scale  {:x 1, :y 1},
                              :speed  1
                              :meshes true
                              :skin "01 Vera_1"}
                 :senoravaca {:width  351,
                              :height 717,
                              :scale  {:x 1, :y 1}
                              :speed  1
                              :meshes true
                              :skin   "vaca"}
                 :mari       {:width  910,
                              :height 601,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :skin "01 mari"}
                 :boxes {:speed 1}})

(defn prepare-anim-object-params
  "Overwrite animation properties. Set default skin if no skin provided."
  [object]
  (if-let [anim-data (get animations (-> object :name keyword))]
    (merge object anim-data (select-keys object [:skin]))
    object))
