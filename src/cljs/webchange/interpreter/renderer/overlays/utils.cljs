(ns webchange.interpreter.renderer.overlays.utils)

(def common-elements
  {:close-button {:src  "/raw/img/ui/close_button_01.png"
                  :size {:width 97 :height 97}}})

(defn get-coordinates
  [{:keys [viewport vertical horizontal padding object] :as params
    :or   {object  {:width 0 :height 0}
           padding {:x 0 :y 0}}}]
  (cond-> {}
          (contains? params :horizontal) (assoc :x 0)
          (contains? params :vertical) (assoc :y 0)
          (= horizontal "left") (assoc :x (/ (- (:x viewport)) (:scale-x viewport)))
          (= horizontal "center") (assoc :x (- (/ (- (/ (:width viewport) 2) (:x viewport))
                                                  (:scale-x viewport))
                                               (/ (:width object) 2)))
          (= horizontal "right") (assoc :x (- (/ (- (:width viewport) (:x viewport)) (:scale-x viewport)) (:width object)))
          (= vertical "top") (assoc :y (/ (- (:y viewport)) (:scale-y viewport)))
          (= vertical "center") (assoc :y (/ (- (/ (:height viewport) 2)
                                                (/ (:height object) 2))
                                             (:scale-y viewport)))
          (= vertical "bottom") (assoc :y (- (/ (- (:height viewport) (:y viewport)) (:scale-y viewport)) (:height object)))
          (and (-> padding :x nil? not) (= horizontal "left")) (update :x + (:x padding))
          (and (-> padding :x nil? not) (= horizontal "right")) (update :x - (:x padding))
          (and (-> padding :y nil? not) (= vertical "top")) (update :y + (:y padding))
          (and (-> padding :y nil? not) (= vertical "bottom")) (update :y - (:y padding))))
