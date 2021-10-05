(ns webchange.interpreter.renderer.scene.components.animation.utils.state)

(defn init-state
  []
  (let [initial-state {:has-item-in-hands? false
                       :item-img           nil}]
    (atom initial-state)))

(defn has-item-in-hands?
  [state]
  (get @state :has-item-in-hands? false))

(defn take-image
  [state image-src]
  (swap! state assoc :has-item-in-hands? true)
  (swap! state assoc :item-img image-src))

(defn give-image
  [state]
  (let [image-src (get @state :item-img)]
    (swap! state assoc :has-item-in-hands? false)
    (swap! state assoc :item-img nil)
    image-src))

(defn pass-image
  [source-state target-state]
  (->> (give-image source-state)
       (take-image target-state)))
