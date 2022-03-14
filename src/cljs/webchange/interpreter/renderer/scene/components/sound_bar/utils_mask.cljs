(ns webchange.interpreter.renderer.scene.components.sound-bar.utils-mask
  (:require
    [webchange.interpreter.pixi :refer [Graphics]]
    [webchange.interpreter.renderer.scene.components.sound-bar.utils-value :refer [max-value]]))

(defn- get-item-size
  [total-size items-number gap]
  (Math/ceil (/ (- total-size
                   (* (dec items-number)
                      gap))
                items-number)))

(defn- get-bar-coordinates
  [value size]
  (let [gap 4
        item-width (get-item-size (:width size) (count value) gap)
        item-height (get-item-size (:height size) max-value gap)]
    (->> value
         (map-indexed (fn [bar-idx bar-value]
                        (->> (range bar-value)
                             (map (fn [idx]
                                    {:x      (* bar-idx (+ item-width gap))
                                     :y      (* idx (+ item-height gap))
                                     :width  item-width
                                     :height item-height})))))
         (flatten)
         (map (fn [{:keys [y] :as item}]
                (-> (assoc item :y (- (:height size) y item-height))))))))

(defn- draw-rect
  [graphics {:keys [x y width height color]
             :or   {x     0
                    y     0
                    color 0x000000}}]
  (doto graphics
    (.beginFill color)
    (.drawRect x y width height)
    (.endFill color)))

(defn update-mask!
  [state]
  (let [{:keys [mask value size]} @state
        bar-coordinates (get-bar-coordinates value size)]
    (.clear mask)
    (doseq [item bar-coordinates]
      (draw-rect mask item))))

(defn create-mask!
  [state]
  (let [graphics (Graphics.)]
    (swap! state assoc :mask graphics)
    (update-mask! state)))
