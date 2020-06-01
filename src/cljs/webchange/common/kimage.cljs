(ns webchange.common.kimage
  (:require
    [reagent.core :as r]
    [react-konva :refer [Image Group]]
    [konva :refer [Animation]]
    ))

(defn with-onload [image cb]
  (set! (.-onload image) #(cb image))
  image)

(defn with-src [image src]
  (set! (.-src image) src)
  image)

(defn create-image
  [src a]
  (-> (js/Image.)
      (with-onload #(reset! a %1))
      (with-src src)))

(defn- has-animation?
  [props]
  (contains? props :animation))

(defn- start-animation
  [animation image props]
  (let [animation-state (atom nil)
        anim-function (:animation props)]
    (when (and (nil? @animation)
               (not (nil? anim-function)))
      (reset! animation (Animation. #(anim-function @image (js->clj % :keywordize-keys true) animation-state) (.getLayer @image)))
      (.start @animation))))

(defn- stop-animation
  [animation]
  (when-not (nil? @animation)
    (.stop @animation)
    (reset! animation nil)))

(defn- rounded-corners-box
  [border-radius size ctx]
  (let [{:keys [width height]} size
        [top-left top-right bottom-right bottom-left] border-radius]
    (.beginPath ctx)
    (.moveTo ctx (- width top-right) 0)
    (.arc ctx (- width top-right) top-right top-right (/ (* 3 Math/PI) 2) 0) ;; top-right
    (.lineTo ctx width (- height bottom-right))
    (.arc ctx (- width bottom-right) (- height bottom-right) bottom-right 0 (/ Math/PI 2)) ;; bottom-right
    (.lineTo ctx bottom-left height)
    (.arc ctx bottom-left (- height bottom-left) bottom-left (/ Math/PI 2) Math/PI) ;; bottom-left
    (.lineTo ctx 0 top-left)
    (.arc ctx top-left top-left top-left Math/PI (/ (* 3 Math/PI) 2)) ;; top-left
    ))

(defn- with-clip
  [props border-radius image-dimension]
  (if-not (nil? border-radius)
    (assoc props :clip-func (partial rounded-corners-box border-radius image-dimension))
    props))

(def defined (complement nil?))

(defn- get-image-dimension
  [image-element component-props]
  (let [custom-width (:width component-props)
        custom-height (:height component-props)
        origin-width (if (defined image-element) (.-width image-element) nil)
        origin-height (if (defined image-element) (.-height image-element) nil)]
    (cond
      (and (defined custom-width)
           (defined custom-height)) {:width custom-width :height custom-height}
      (defined custom-width) {:width custom-width :height (* origin-height (/ custom-width origin-width))}
      (defined custom-height) {:width (* origin-width (/ custom-height origin-height)) :height custom-height}
      :else {:width origin-width :height origin-height})))

(defn kimage
  [{:keys [src]}]
  (let [path (r/atom src)
        i (r/atom nil)
        _ (create-image src i)
        konva-image (r/atom nil)
        animation (r/atom nil)]
    (r/create-class
      {:display-name "kimage"

       :component-did-mount
                     (fn [this]
                       (let [props (r/props this)]
                         (when (has-animation? props)
                           (start-animation animation konva-image props))))

       :component-did-update
                     (fn [this]
                       (let [props (r/props this)]
                         (if (has-animation? props)
                           (start-animation animation konva-image props)
                           (stop-animation animation))))

       :component-will-unmount
                     (fn []
                       (stop-animation animation))

       :reagent-render
                     (fn [{:keys [src] :as params}]
                       (when-not (= @path src)
                         (reset! path src)
                         (create-image src i))
                       (let [filtered-params (-> params
                                                 (dissoc :animations))
                             image-params (merge {:image @i}
                                                 filtered-params
                                                 {:ref (fn [ref]
                                                         (when ref
                                                           (reset! konva-image ref)
                                                           (when @i
                                                             (.cache ref))))})]
                         [:> Group
                          (-> {}
                              (with-clip (:border-radius params) (get-image-dimension @i params)))
                          [:> Image image-params]]))})))
