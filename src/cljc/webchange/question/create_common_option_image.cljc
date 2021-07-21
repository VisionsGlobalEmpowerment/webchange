(ns webchange.question.create-common-option-image
  (:require
    [webchange.question.create-common-voice-over :as voice-over]))

(defn- create-image
  [{:keys [object-name x y width height src on-click on-click-params]
    :or   {x 0
           y 0}}]
  (let [background-name (str object-name "-background")
        image-name (str object-name "-image")
        actions (cond-> {}
                        (some? on-click) (assoc :click (cond-> {:type "action"
                                                                :on   "click"
                                                                :id   on-click}
                                                               (some? on-click-params) (assoc :params on-click-params))))]
    {(keyword object-name)     {:type        "group"
                                :object-name object-name
                                :x           x
                                :y           y
                                :children    [background-name image-name]
                                :actions     actions}
     (keyword background-name) {:type          "rectangle"
                                :x             0
                                :y             0
                                :width         width
                                :height        height
                                :border-radius 20
                                :border-width  2
                                :border-color  0x000000
                                :fill          0xFFFFFF}
     (keyword image-name)      {:type       "image"
                                :src        src
                                :x          (/ width 2)
                                :y          (/ height 2)
                                :max-width  width
                                :max-height height
                                :origin     {:type "center-center"}
                                :editable?  {:select true}}}))

(defn- create-text
  [{:keys [object-name x y width text]}]
  {(keyword object-name) {:type           "text"
                          :text           text
                          :x              x
                          :y              y
                          :width          width
                          :font-size      60
                          :vertical-align "top"
                          :editable?      {:select true}}})

(defn create
  [{:keys [object-name x y width height img text gap idx on-option-click on-option-voice-over-click value]
    :or   {x   0
           y   0
           gap 20}}]
  (let [label-height 80

        image-name (str object-name "-image")
        text-name (str object-name "-text")
        button-name (str object-name "-button")

        image-width width
        image-height (- height label-height)

        text-x (+ voice-over/default-size gap)
        text-y (+ image-height gap)
        text-width (- width text-x)]
    (merge {(keyword object-name) {:type        "group"
                                   :object-name object-name
                                   :x           x
                                   :y           y
                                   :children    [image-name text-name button-name]}}
           (create-image {:object-name     image-name
                          :src             img
                          :width           image-width
                          :height          image-height
                          :on-click        on-option-click
                          :on-click-params {:value value}})
           (create-text {:object-name text-name
                         :x           text-x
                         :y           text-y
                         :width       text-width
                         :text        text})
           (voice-over/create {:object-name     button-name
                               :x               0
                               :y               (+ image-height gap)
                               :on-click        on-option-voice-over-click
                               :on-click-params {:value value}}))))
