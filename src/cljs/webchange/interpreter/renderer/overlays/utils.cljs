(ns webchange.interpreter.renderer.overlays.utils)

(def common-elements
  {:close-button {:src  "/raw/img/ui/close_button_01.png"
                  :size {:width 97 :height 97}}
   :home-button  {:src  "/raw/img/ui/activity_finished/home.png"
                  :size {:width 96 :height 96}}})

(defn get-coordinates
  [{:keys [viewport vertical horizontal padding object] :as params
    :or   {object  {:width 0 :height 0}
           padding {:x 0 :y 0}}}]
  (cond-> {}
          (contains? params :horizontal) (assoc :x 0)
          (contains? params :vertical) (assoc :y 0)
          (= horizontal "center") (assoc :x (-> (- (:target-width viewport)
                                                   (:width object))
                                                (/ 2)))
          (= horizontal "right") (assoc :x (-> (:target-width viewport)
                                               (- (:width object))))
          (= vertical "center") (assoc :y (-> (- (:target-height viewport)
                                                 (:height object))
                                              (/ 2)))
          (= vertical "bottom") (assoc :y (-> (:target-height viewport)
                                              (- (:height object))))
          (and (-> padding :x nil? not) (= horizontal "left")) (update :x + (:x padding))
          (and (-> padding :x nil? not) (= horizontal "right")) (update :x - (:x padding))
          (and (-> padding :y nil? not) (= vertical "top")) (update :y + (:y padding))
          (and (-> padding :y nil? not) (= vertical "bottom")) (update :y - (:y padding))))
