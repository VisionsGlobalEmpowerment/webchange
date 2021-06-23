(ns webchange.interpreter.renderer.question.question-page.type-3
  (:require
    [webchange.common.events :as ce]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.app :as app]
    [reagent.core :as r]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.components.text.component :as text]
    [webchange.interpreter.renderer.scene.components.rectangle.component :as rectangle]))

(def flow-tag "question-audio")
(def image- (r/atom []))
(def audio-icons (r/atom []))
(def answer-rectangles (r/atom []))
(def audio-sleep-icons (r/atom []))
(def remove-flows {:type "remove-flows" :flow-tag flow-tag})

(defn sleep [f ms]
  (js/setTimeout f ms))

(defn get-coord
  [total idx]
  (case total
    1 (case idx
        0 {:x 792 :y 534})
    2 (case idx
        0 {:x 792 :y 534}
        1 {:x 1013 :y 534})
    3 (case idx
        0 {:x 792 :y 534}
        1 {:x 1013 :y 534}
        2 {:x 1235 :y 534})
    4 (case idx
        0 {:x 792 :y 534}
        1 {:x 1013 :y 534}
        2 {:x 1235 :y 534}
        3 {:x 1464 :y 534})))

(defn create-page-background
  [parent]
  (rectangle/create {:type          "rectangle"
                     :object-name   :question-background-1
                     :border-radius 0
                     :x             0
                     :y             0
                     :width         1920
                     :height        1920
                     :parent        parent
                     :border-width  1
                     :fill          0xFFFFFF}))

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
                      (* (:duration audio-data) 1000)))))))

(defn create-question
  [parent image text chunks audio-data skip-action screenshot?]
  (when skip-action
    (image/create
      {:type        "image"
       :src         "/raw/img/questions/skip.png"
       :x           1760
       :y           40
       :width       96
       :height      96
       :object-name :question-skip-image
       :parent      parent
       :on-click    #(re-frame/dispatch [::ce/execute-action skip-action])}))

  (image/create
    (cond-> {:type        "image"
             :x           150
             :y           298
             :object-name :question-image
             :parent      parent}
            screenshot? (->
                          (assoc :width 480)
                          (assoc :height 270)
                          (assoc :raw image))
            (not screenshot?) (->
                                (assoc :src image))))
  (text/create {:type           "text",
                :x              860,
                :y              245,
                :object-name    :question-text
                :fill           0x000000,
                :font-family    "Roboto",
                :font-size      60,
                :width          776,
                :parent         parent
                :text           text,
                :align          "left",
                :vertical-align "top"
                :font-weight    "normal"
                :scale          {:x 1, :y 1}
                :chunks         chunks})
  (create-sound-icon {:x          745
                      :y          240
                      :audio-data audio-data
                      :parent     parent
                      :target     "question-text"}))

(defn- create-answer
  [total parent success-action fail-action]
  (fn [idx {:keys [audio-data correct image]}]
    (let [{x :x y :y} (get-coord total idx)

          rectangle (rectangle/create {:type          "rectangle"
                                       :object-name   (keyword (str "answer-rectangle-" idx))
                                       :border-radius 24
                                       :x             (- x 31),
                                       :y             (- y 48),
                                       :width         191,
                                       :height        262,
                                       :parent        parent
                                       :border-width  4
                                       :fill          0xFFFFFF
                                       :border-color  0x000000
                                       })
          _ ((:set-on-click-handler rectangle) #(do
                                                  (doall (map (fn [r] ((:set-border-color r) 0x000000)) @answer-rectangles))
                                                  ((:set-border-color rectangle) (if correct 0x0FB600 0xED1C24))
                                                  (re-frame/dispatch [::ce/execute-action remove-flows])
                                                  (re-frame/dispatch (if correct
                                                                       [::ce/execute-action success-action]
                                                                       [::ce/execute-action fail-action]))))]
      (image/create
        (cond-> {:type        "image"
                 :src         image
                 :x           (+ x -5)
                 :y           (+ y 10)
                 :width       141,
                 :height      141,
                 :object-name :question-image
                 :parent      parent}))
      (swap! answer-rectangles conj rectangle)
      (create-sound-icon {:x          (- x 40)
                          :y          (+ y 220)
                          :audio-data audio-data
                          :parent     parent
                          :target     (str "answer-text-" idx)}))))

(defn- create-answers
  [answers parent success-action fail-action]
  (let [answers (:data answers)
        total (count answers)
        fail-action (-> fail-action
                        (assoc :tags [flow-tag]))]
    (doall
      (map-indexed (create-answer total parent success-action fail-action) answers))))

(defn create-page
  [{:keys [image parent text chunks answers success fail skip audio-data screenshot?]} db action]

  (reset! answer-rectangles [])
  (reset! audio-icons [])
  (reset! audio-sleep-icons [])
  (create-page-background parent)
  (let [success-action (ce/cond-action db (assoc action :success success) :success)
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
    (create-answers answers parent success-action fail-action)))
