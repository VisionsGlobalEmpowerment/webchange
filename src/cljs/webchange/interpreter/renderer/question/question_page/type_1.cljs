(ns webchange.interpreter.renderer.question.question-page.type-1
  (:require
    [webchange.common.events :as ce]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.components.text.component :as text]
    [webchange.interpreter.renderer.scene.components.rectangle.component :as rectangle]))

(def flow-tag "question-audio")
(def image- (r/atom []))
(def audio-icons (r/atom []))
(def answer-rectangle (r/atom []))
(def audio-sleep-icons (r/atom []))
(def remove-flows {:type "remove-flows" :flow-tag flow-tag})

(defn sleep [f ms]
  (js/setTimeout f ms))

(defn image-x-to-center
  [object]
  (let [r1 (.getBounds object)
        r1width (.-width r1)]
    (- (/ 1980 2) (/ r1width 2))))

(defn correct-question-text-y
  [object center min]
  (let [r1 (.getBounds object)
        r1height (.-height r1)]
    (max min (- center (/ r1height 2)))))

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
  (let [icon-white-img "/raw/img/questions/sound-icon-white.png"
        icon-img "/raw/img/questions/sound-icon.png"
        audio-data (-> audio-data
                       (assoc :tags [flow-tag])
                       (assoc :type "text-animation")
                       (assoc :target target))
        image (image/create
                {:type        "image"
                 :src         icon-white-img
                 :x           x,
                 :y           y,
                 :height      96
                 :width       96
                 :object-name (keyword (str target "-sound-sign"))
                 :parent      parent})]
    (swap! audio-icons conj image)
    ((:set-on-click-handler image)
     #(do
        (doall (map (fn [img] ((:set-src img) icon-white-img)) @audio-icons))
        (doall (map (fn [interval] (js/clearTimeout interval)) @audio-sleep-icons))

        ((:set-src image) icon-img)

        (re-frame/dispatch [::ce/execute-action remove-flows])
        (re-frame/dispatch [::ce/execute-action audio-data])

        (swap! audio-sleep-icons conj
               (sleep (fn [] ((:set-src image) icon-white-img))
                      (* (:duration audio-data) 1000)))
        ))
    ))

(defn create-question
  [parent image text chunks audio-data skip-action screenshot?]
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
       }))
  (let [image-wrapper (image/create
                        (cond-> {:type        "image"
                                 :src         image
                                 :x           0
                                 :y           0
                                 :object-name :question-image
                                 :parent      parent}
                                screenshot? (-> (assoc :width 480)
                                                (assoc :height 270)
                                                (assoc :raw image)
                                                (dissoc :src)
                                                )))
        _ ((:set-position image-wrapper) {:x (image-x-to-center (:object image-wrapper)) :y 200})
        question-wrapper (text/create {:type           "text",
                                       :x              1000
                                       :y              100,
                                       :object-name    :question-text
                                       :fill           0x000000,
                                       :font-family    "Roboto",
                                       :font-size      60,
                                       :width          1300,
                                       :parent         parent
                                       :text           text,
                                       :align          "left",
                                       :vertical-align "top"
                                       :font-weight    "normal"
                                       :scale          {:x 1, :y 1}
                                       :chunks         chunks
                                       })
        x-text-position (image-x-to-center (:object question-wrapper))
        y-text-position (correct-question-text-y (:object question-wrapper) 90 0)
        _ ((:set-position question-wrapper) {:x x-text-position :y y-text-position})
        ]
    (create-sound-icon {:x          (- x-text-position 112)
                        :y          40
                        :audio-data audio-data
                        :parent     parent
                        :target     "question-text"})
    ))



(defn- create-type-1-answers
  [answers parent success-action fail-action]
  (let [answers (:data answers)
        total (count answers)
        fail-action (-> fail-action
                        (assoc :tags [flow-tag]))]
    (doall
      (map-indexed
        (fn [idx {:keys [audio-data text chunks correct]}]
          (let [{x :x y :y} (get-coord total idx)

                rectangle (rectangle/create {:type          "rectangle"
                                             :object-name   (keyword (str "answer-rectangle-" idx))
                                             :border-radius 56
                                             :x             (- x 31),
                                             :y             (- y 48),
                                             :width         560
                                             :height        96
                                             :parent        parent
                                             :border-width  4
                                             :fill          0xFFFFFF
                                             :border-color  0x000000
                                             })

                _ ((:set-on-click-handler rectangle) #(do
                                                        (doall (map (fn [rectangle] ((:set-border-color rectangle) 0x000000)) @answer-rectangle))
                                                        ((:set-border-color rectangle) (if correct 0x0FB600 0xED1C24))
                                                        (re-frame/dispatch [::ce/execute-action remove-flows])
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
                                       :chunks         chunks
                                       :scale          {:x 1, :y 1}})]
            (swap! answer-rectangle conj rectangle)
            (create-sound-icon {:x          (- x 152)
                                :y          (- y 48)
                                :audio-data audio-data
                                :parent     parent
                                :target     (str "answer-text-" idx)})
            [text])
          )
        answers
        ))))
;
;(assoc stage :img (.createObjectURL js/URL (get stages-screenshots idx))))

(defn create-page
  [{:keys [image parent text chunks answers success fail skip audio-data screenshot?]} db]

  (reset! answer-rectangle [])
  (reset! audio-icons [])
  (reset! audio-sleep-icons [])
  (create-page-background parent)
  (let [success-action (ce/get-action success db)
        fail-action (ce/get-action fail db)
        skip-action (if skip (ce/get-action skip db))]
    (if screenshot?
      (app/take-screenshot #(let [img-url (.createObjectURL js/URL %)]
                              (resources/load-resources [img-url]
                                                        {:on-complete
                                                         (fn []
                                                           (create-question parent
                                                                            img-url
                                                                            text
                                                                            chunks
                                                                            audio-data
                                                                            skip-action
                                                                            screenshot?))})))
      (create-question parent image text chunks audio-data skip-action screenshot?))
    (create-type-1-answers answers parent success-action fail-action)))
