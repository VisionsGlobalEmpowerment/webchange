(ns webchange.interpreter.renderer.navigation-menu
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.group :refer [create-group]]))

(defn- get-coordinates
  [{:keys [viewport vertical horizontal padding object]}]
  (cond-> {:x 0 :y 0}
          (= horizontal "left") (assoc :x (/ (- (:x viewport)) (:scale-x viewport)))
          (= horizontal "right") (assoc :x (- (/ (- (:width viewport) (:x viewport)) (:scale-x viewport)) (:width object)))
          (= vertical "top") (assoc :y (/ (- (:y viewport)) (:scale-y viewport)))
          (= vertical "bottom") (assoc :y (- (/ (- (:height viewport) (:y viewport)) (:scale-y viewport)) (:height object)))
          (and (-> padding :x nil? not) (= horizontal "left")) (update :x + (:x padding))
          (and (-> padding :x nil? not) (= horizontal "right")) (update :x - (:x padding))
          (and (-> padding :y nil? not) (= vertical "top")) (update :y + (:y padding))
          (and (-> padding :y nil? not) (= vertical "bottom")) (update :y - (:y padding))))

(defn create-navigation-menu
  [{:keys [parent viewport]}]
  (let [menu-padding {:x 20 :y 20}
        back-button-size {:width 97 :height 99}
        settings-button-size {:width 100 :height 103}
        close-button-size {:width 97 :height 97}

        back-button (merge {:type        "image"
                            :src         "/raw/img/ui/back_button_01.png"
                            :object-name :back
                            :on-click    #(re-frame/dispatch [::ie/close-scene])}
                           (get-coordinates {:viewport   viewport
                                             :vertical   "top"
                                             :horizontal "left"
                                             :object     back-button-size
                                             :padding    {:x (:x menu-padding)
                                                          :y (:y menu-padding)}}))
        settings-button (merge {:type        "image"
                                :src         "/raw/img/ui/settings_button_01.png"
                                :object-name :settings
                                :on-click    #(re-frame/dispatch [::ie/open-settings])}
                               (get-coordinates {:viewport   viewport
                                                 :vertical   "top"
                                                 :horizontal "right"
                                                 :object     settings-button-size
                                                 :padding    {:x (+ (:width close-button-size) (* 2 (:x menu-padding)))
                                                              :y (:y menu-padding)}}))
        close-button (merge {:type        "image"
                             :src         "/raw/img/ui/close_button_01.png"
                             :object-name :close
                             :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
                            (get-coordinates {:viewport   viewport
                                              :vertical   "top"
                                              :horizontal "right"
                                              :object     close-button-size
                                              :padding    menu-padding}))]
    (create-group parent {:name        "navigation-menu"
                          :parent      parent
                          :object-name :navigation-menu
                          :children    [back-button settings-button close-button]})))
