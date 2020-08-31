(ns webchange.interpreter.renderer.scene.components.animation.utils
  (:require
    [webchange.interpreter.renderer.pixi :refer [RegionAttachment Skin TextureAtlasRegion]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.resources :as resources]))

(def default-region-params
  {:x               0
   :y               0
   :width           100
   :height          100
   :original-width  100
   :original-height 100
   :offset-x        0
   :offset-y        0
   :index           -1})

(def default-region-attachment-params
  {:name    "element"
   :x       0
   :y       0
   :width   100
   :height  100
   :scale-x 1
   :scale-y 1})

(def get-position utils/get-position)
(def set-position utils/set-position)
(def get-scale utils/get-scale)
(def set-scale utils/set-scale)
(def set-visibility utils/set-visibility)

(defn create-region
  [texture custom-params]
  (let [params (merge default-region-params custom-params)]
    (doto (TextureAtlasRegion.)
      (set! -x (:x params))
      (set! -y (:y params))
      (set! -width (:width params))
      (set! -height (:height params))
      (set! -originalWidth (:original-width params))
      (set! -originalHeight (:original-height params))
      (set! -offsetX (:offset-x params))
      (set! -offsetY (:offset-y params))
      (set! -index (:index params))
      (set! -texture texture))))

(defn create-region-attachment
  [region custom-params]
  (let [params (merge default-region-attachment-params custom-params)]
    (doto (RegionAttachment. (:name params))
      (.setRegion region)
      (set! -x (:x params))
      (set! -y (:y params))
      (set! -scaleX (:scale-x params))
      (set! -scaleY (:scale-y params))
      (set! -width (:width params))
      (set! -height (:height params))
      (.updateOffset))))

(defn get-skeleton
  [spine-object]
  (.-skeleton spine-object))

(defn has-animation?
  [spine-object animation-name]
  (-> (.. spine-object -state)
      (.hasAnimationByName animation-name)))

(defn has-skin?
  [spine-object skin-name]
  (-> (get-skeleton spine-object)
      (.-data)
      (.findSkin skin-name)
      (some?)))

(defn set-animation-slot
  ([image-src spine-object slot-name]
   (set-animation-slot image-src spine-object slot-name {}))
  ([image-src spine-object slot-name {:keys [region-params attachment-params slot-attachment-name]}]
   (let [skeleton (get-skeleton spine-object)
         image-resource (resources/get-resource image-src)]
     (when (nil? image-resource)
       (-> (str "Can not set slot <" image-src ">: Resource was not found") js/Error. throw))
     (let [texture (.-texture image-resource)
           attachment (-> texture
                          (create-region region-params)
                          (create-region-attachment attachment-params))
           slot-index (.findSlotIndex skeleton slot-name)
           attachment-name (or slot-attachment-name "boxes")
           new-skin (doto (Skin. (.-url image-resource))
                      (.setAttachment slot-index attachment-name attachment))]
       (.setSkin skeleton new-skin)
       (.setSlotsToSetupPose skeleton)
       (.hackTextureBySlotIndex spine-object slot-index texture)))))

(defn add-animation
  ([spine-object animation-name]
   (add-animation spine-object animation-name {}))
  ([spine-object animation-name params]
   (when-not (has-animation? spine-object animation-name)
     (-> (str "Can not add animation <" animation-name ">: Animation does not exist") js/Error. throw))
   (let [{:keys [track-index loop? delay]} (merge {:track-index 0
                                                   :delay       0
                                                   :loop?       true}
                                                  params)]
     (-> (.-state spine-object)
         (.addAnimation track-index animation-name loop? delay)))))

(defn set-animation
  ([spine-object animation-name]
   (set-animation spine-object animation-name {}))
  ([spine-object animation-name params]
   (when-not (has-animation? spine-object animation-name)
     (-> (str "Can not set animation <" animation-name ">: Animation does not exist") js/Error. throw))
   (let [{:keys [track-index loop?]} (merge {:track-index 0
                                             :loop?       true}
                                            params)]
     (-> (.-state spine-object)
         (.setAnimation track-index animation-name loop?)))))

(defn set-empty-animation
  ([spine-object]
   (set-empty-animation spine-object {}))
  ([spine-object params]
   (let [{:keys [track-index mix-duration]} (merge {:track-index  0
                                                    :mix-duration 0}
                                                   params)]
     (-> (.-state spine-object)
         (.setEmptyAnimation track-index mix-duration)))))

(defn set-skin
  [spine-object skin-name]
  (when-not (has-skin? spine-object skin-name)
    (-> (str "Can not set skin <" skin-name ">: Skin does not exist") js/Error. throw))
  (-> (.-skeleton spine-object)
      (.setSkinByName skin-name)))

(defn set-animation-speed
  [spine-object animation-speed]
  (when (nil? animation-speed)
    (-> (str "Speed is not defined") js/Error. throw))
  (-> (.-state spine-object)
      (aset "timeScale" animation-speed)))
