(ns webchange.interpreter.renderer.question.question-page.type-1
  (:require
    [webchange.common.events :as ce]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.components.text.component :as text]
    [webchange.interpreter.renderer.scene.components.rectangle.component :as rectangle]
    [webchange.interpreter.renderer.overlays.utils :as overlays-utils]
    [webchange.utils.text :as text-utils]))

(defn sleep [f ms]
  (js/setTimeout f ms))

(defn image-x-to-center
  [object]
  (let [
        r1 (.getBounds object)
        r1width (.-width r1)
        ]
    (- (/ 1980 2) (/ r1width 2))))

(defn object-position-dimension
  [object]
  (let [
        r1 (.getBounds object)
        r1x (.-x r1)
        r1y (.-y r1)
        r1width (.-width r1)
        r1height (.-height r1)
        ]
    {:x      r1x
     :y      r1y
     :width  r1width
     :height r1height})
  )


(defn get-coord
  [total idx]
  (case total
    1 (case idx
        0 {:x 410 :y 750}
        1 {:x 1144 :y 750}
        2 {:x 410 :y 896}
        3 {:x 1144 :y 896})
    2 (case idx
        0 {:x 410 :y 750}
        1 {:x 1144 :y 750}
        2 {:x 410 :y 896}
        3 {:x 1144 :y 896})
    3 (case idx
        0 {:x 410 :y 750}
        1 {:x 1144 :y 750}
        2 {:x 410 :y 896}
        3 {:x 1144 :y 896})
    4 (case idx
        0 {:x 410 :y 750}
        1 {:x 1144 :y 750}
        2 {:x 410 :y 896}
        3 {:x 1144 :y 896})
    ))

(defn create-page-background
  [parent]
  (rectangle/create {:type          "rectangle"
                     :object-name   :question-background-1
                     :border-radius 0
                     :x             0
                     :y             0
                     :width         1920
                     :height        648
                     :parent        parent
                     :border-width  1
                     :fill          0xFFFFFF
                     })
  (rectangle/create {:type          "rectangle"
                     :object-name   :question-background-2
                     :border-radius 0
                     :x             0
                     :y             648
                     :width         1920
                     :height        432
                     :parent        parent
                     :border-width  1
                     :fill          0xff7900}))

(defn create-sound-icon
  [{:keys [x y audio-data parent target]}]
  (let [audio-data (-> audio-data
                       (assoc :type "text-animation")
                       (assoc :target target))
        image (image/create
                {:type        "image"
                 :src         "/raw/img/questions/sound-icon-white.png"
                 :x           x,
                 :y           y,
                 :height      96
                 :width       96
                 :object-name (keyword (str target "-sound-sign"))
                 :parent      parent})]
    ((:set-on-click-handler image)
     #(do
        ((:set-src image) "/raw/img/questions/sound-icon.png")
        (re-frame/dispatch [::ce/execute-action audio-data])
        (sleep (fn [] ((:set-src image) "/raw/img/questions/sound-icon-white.png"))
               (* (:duration audio-data) 1000))))
    ))

(defn create-question
  [parent image text chunks audio-data skip-action]
  (if skip-action
    (image/create
      {:type        "image"
       :src         "/raw/img/questions/skip.png"
       :x           1760
       :y           40
       :width       96
       :height      96
       :object-name :question-skip-image
       :parent      parent
       :on-click    #(re-frame/dispatch [::ce/execute-action skip-action])
       })

    )
  (let [image-wrapper (image/create
                        {:type        "image"
                         :src         image
                         :x           0
                         :y           0
                         :object-name :question-image
                         :parent      parent})
        _ ((:set-position image-wrapper) {:x (image-x-to-center (:object image-wrapper)) :y 200})
        question-wrapper (text/create {:type           "text",
                                       :x              1000
                                       :y              80,
                                       :object-name    :question-text
                                       :fill           0x000000,
                                       :font-family    "Roboto",
                                       :font-size      60,
                                       :width          1500,
                                       :parent         parent
                                       :text           text,
                                       :align          "left",
                                       :vertical-align "middle"
                                       :font-weight    "normal"
                                       :scale          {:x 1, :y 1}
                                       :chunks         chunks     ;(text-utils/text->chunks text)
                                       })
        x-text-position (image-x-to-center (:object question-wrapper))
        _ ((:set-position question-wrapper) {:x x-text-position :y 80})
        ]
    (create-sound-icon {:x          (- x-text-position 112)
                        :y          40
                        :audio-data audio-data
                        :parent     parent
                        :target     "question-text"})
    ))



(defn create-type-1-answers
  [answers parent success-action fail-action]
  (let [answers (:data answers)
        total (count answers)]
    (doall
      (map-indexed
        (fn [idx {:keys [audio-data text chunks correct]}]
          (let [{x :x y :y} (get-coord total idx)

                rectangle (rectangle/create {:type          "rectangle"
                                             :object-name   (keyword (str "answer-rectangle-" idx))
                                             :border-radius 56
                                             :x             (- x 11),
                                             :y             (- y 48),
                                             :width         520
                                             :height        96
                                             :parent        parent
                                             :border-width  5
                                             :fill          0xFFFFFF
                                             :border-color  0x000000
                                             })

                _ ((:set-on-click-handler rectangle) #(do
                                                        ((:set-border-color rectangle) (if correct 0x0FB600 0xED1C24))
                                                        (re-frame/dispatch (if correct
                                                                             [::ce/execute-action success-action]
                                                                             [::ce/execute-action fail-action]))))
                text-obj (text/create {:type           "text",
                                       :x              x,
                                       :y              (- y 48),
                                       :object-name    (keyword (str "answer-text-" idx))
                                       :fill           0x000000,
                                       :font-family    "Roboto",
                                       :font-size      26,
                                       :width          520
                                       :height         96
                                       :text           text,
                                       :parent         parent
                                       :align          "left",
                                       :vertical-align "middle"
                                       :font-weight    "normal"
                                       :chunks         chunks ; (text-utils/text->chunks text)
                                       :scale          {:x 1, :y 1}})]
            (create-sound-icon {:x          (- x 132)
                                :y          (- y 48)
                                :audio-data audio-data
                                :parent     parent
                                :target     (str "answer-text-" idx)})
            [
             text
             ])
          )
        answers
        ))))

(defn create-page
  [{:keys [image parent text chunks answers success fail skip audio-data]} db]
  (create-page-background parent)
  (let [success-action (ce/get-action success db)
        fail-action (ce/get-action fail db)
        skip-action (if skip (ce/get-action skip db))]
    (create-question parent image text chunks audio-data skip-action)
    (create-type-1-answers answers parent success-action fail-action)
    )
  )