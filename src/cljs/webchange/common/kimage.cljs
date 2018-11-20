(ns webchange.common.kimage
  (:require
    [reagent.core :as r]
    [react-konva :refer [Image]]
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

(defn kimage
  [src params]
  (let [path (r/atom src)
        i (r/atom nil)
        _ (create-image src i)]
    (fn [src]
      (when-not (= @path src)
        (reset! path src)
        (create-image src i))
      [:> Image (merge {:image @i} params)]
      )))