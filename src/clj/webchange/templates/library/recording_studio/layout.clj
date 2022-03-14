(ns webchange.templates.library.recording-studio.layout)

(defn- get-layout-params
  []
  (let [take-half #(-> % (/ 2) (Math/ceil))

        canvas {:width  1920
                :height 1080}

        screen-margin-x 208
        screen-margin-y 48
        screen {:x         screen-margin-x
                :y         screen-margin-y
                :width     (- (:width canvas) (* 2 screen-margin-x))
                :height    768
                :padding-x 200
                :padding-y 100}

        timer-size 600
        timer {:x    (+ (:x screen)
                        (take-half (:width screen))
                        (- (take-half timer-size)))
               :y    (+ (:y screen)
                        (take-half (:height screen))
                        (- (take-half timer-size)))
               :size timer-size}

        right-margin {:x     (+ (:x screen) (:width screen))
                      :width screen-margin-x}
        bottom-margin {:y      (+ (:y screen) (:height screen))
                       :height (- (:height canvas)
                                  (:height screen)
                                  (:y screen))}

        concept-image {:x      (+ (:x screen) (:padding-x screen))
                       :y      (+ (:y screen) (:padding-y screen))
                       :width  (- (:width screen) (* 2 (:padding-x screen)))
                       :height (- (:height screen) (* 2 (:padding-y screen)))}



        approve-button-size 96
        approve-button {:x      (+ (:x right-margin)
                                   (/ (:width right-margin) 2)
                                   (- (/ approve-button-size 2)))
                        :y      (:y screen)
                        :width  approve-button-size
                        :height approve-button-size}

        button-size 168
        button-border 16
        button-half-size (take-half button-size)
        button-small-size (-> button-size (* 0.8) (Math/ceil))
        button-half-small-size (take-half button-small-size)

        record-buttons-center {:x (-> (+ (:x screen)
                                         (take-half (:width screen))))
                               :y (+ (:y bottom-margin)
                                     (-> (:height bottom-margin) (take-half)))}

        play-back-buttons-center {:x (- (:x record-buttons-center)
                                        button-size
                                        64)
                                  :y (:y record-buttons-center)}

        sound-bar-height 115
        sound-bar {:x      screen-margin-x
                   :y      (+ (:y bottom-margin)
                              (take-half (- (:height bottom-margin)
                                            sound-bar-height)))
                   :width  (- (:x play-back-buttons-center)
                              button-half-size
                              80
                              screen-margin-x)
                   :height sound-bar-height}]

    {:approve-button      approve-button
     :concept-image       concept-image
     :start-play-button   {:x            (-> (:x play-back-buttons-center) (- button-half-size))
                           :y            (-> (:y play-back-buttons-center) (- button-half-size))
                           :width        button-size
                           :height       button-size
                           :border-width button-border}
     :stop-play-button    {:x            (-> (:x play-back-buttons-center) (- button-half-small-size))
                           :y            (-> (:y play-back-buttons-center) (- button-half-small-size))
                           :width        button-small-size
                           :height       button-small-size
                           :border-width button-border}
     :start-record-button {:x            (-> (:x record-buttons-center) (- button-half-size))
                           :y            (-> (:y record-buttons-center) (- button-half-size))
                           :width        button-size
                           :height       button-size
                           :border-width button-border}
     :stop-record-button  {:x            (-> (:x record-buttons-center) (- button-half-small-size))
                           :y            (-> (:y record-buttons-center) (- button-half-small-size))
                           :width        button-small-size
                           :height       button-small-size
                           :border-width button-border}
     :screen              screen
     :sound-bar           sound-bar
     :timer               timer}))

(def layout-params (get-layout-params))
