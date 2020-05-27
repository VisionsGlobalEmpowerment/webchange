(ns webchange.common.kimage
  (:require
    [reagent.core :as r]
    [react-konva :refer [Image]]
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

(defn- get-props
  [components]
  (let [arguments (.. components -props -argv)]
    (when (= 3 (count arguments))
      (js->clj (nth arguments 2)))))

(defn kimage
  [src params]
  (let [path (r/atom src)
        i (r/atom nil)
        _ (create-image src i)
        konva-image (r/atom nil)
        animation (r/atom nil)]
    (r/create-class
      {:display-name "kimage"

       :component-did-mount
                     (fn [this]
                       (let [props (get-props this)]
                         (when (has-animation? props)
                           (start-animation animation konva-image props))))

       :component-did-update
                     (fn [this]
                       (let [props (get-props this)]
                         (if (has-animation? props)
                           (start-animation animation konva-image props)
                           (stop-animation animation))))

       :component-will-unmount
                     (fn []
                       (stop-animation animation))

       :reagent-render
                     (fn [src params]
                       (when-not (= @path src)
                         (reset! path src)
                         (create-image src i))
                       (let [filtered-params (-> params
                                                 (dissoc :animations))]
                         [:> Image (merge {:image @i}
                                          filtered-params
                                          {:ref (fn [ref]
                                                  (when ref
                                                    (reset! konva-image ref)
                                                    (when @i
                                                      (.cache ref))))}
                                          )]))})))