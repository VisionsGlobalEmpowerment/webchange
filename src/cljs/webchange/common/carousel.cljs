(ns webchange.common.carousel
  (:require
    [reagent.core :as r]
    [react-konva :refer [Group]]
    [konva :refer [Image Animation]]
    [goog.object :as g]
    ))

(defn carousel-frame [group last next width head tail end speed]
  (fn [frame]
    (let [head-width (.width @head)
          offset-x (* speed (.-timeDiff frame))
          current-x (.x group)
          new-x (- current-x offset-x)
          end-x (+ new-x (.x @end) (.width @end))]
      (.x group new-x)

      (when (<= end-x width)
        (let [x (+ (.x @end) (.width @end))
              cloned (.clone next #js {:x x})]
          (.add group cloned)
          (reset! end cloned)
          (swap! tail conj cloned)))

      (when (< new-x (- head-width))
        (.x group (+ new-x head-width))
        (.destroy @head)
        (doseq [shape @tail]
          (.x shape (- (.x shape) head-width)))
        (reset! head (first @tail))
        (reset! tail (rest @tail))))))

(defn start-carousel
  [group {:keys [first last next width]}]
  (.add group first)
  (let [head (atom first)
        tail (atom [])
        end (atom first)
        speed 0.5]
    (let [callback (carousel-frame group last next width head tail end speed)
          animation (Animation. callback (.getLayer group))]
      (.start animation))))

(defn js-image->konva-image [image]
  (doto (Image. #js {:image image})))

(defn create-image! [src key storage]
  (doto (js/Image.)
    (g/set "onload" #(swap! storage assoc key (js-image->konva-image (.-target %))))
    (g/set "src" src)))

(defn init-images [props]
  (let [storage (r/atom {})]
    (create-image! (:first props) :first storage)
    (create-image! (:last props) :last storage)
    (create-image! (:next props) :next storage)
    storage))

(defn carousel [props]
  (let [storage (init-images props)]
    (fn [props]
      (let [{:keys [first last next]} @storage]
        (if (and first last next)
          [:> Group {:ref #(when % (start-carousel % (assoc props :first first :last last :next next)))}]))))  )
