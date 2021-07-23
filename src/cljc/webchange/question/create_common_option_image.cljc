(ns webchange.question.create-common-option-image
  (:require
    [webchange.question.create-common-voice-over :as voice-over]
    [webchange.question.params :as p]
    [webchange.question.utils :refer [merge-data]]))

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
    {:objects {(keyword object-name)     {:type        "group"
                                          :object-name object-name
                                          :x           x
                                          :y           y
                                          :children    [background-name image-name]
                                          :actions     actions}
               (keyword background-name) {:type          "rectangle"
                                          :x             p/option-border-width
                                          :y             p/option-border-width
                                          :width         (- width (* p/option-border-width 2))
                                          :height        (- height (* p/option-border-width 2))
                                          :border-radius p/option-border-radius
                                          :border-width  p/option-border-width
                                          :border-color  0x000000
                                          :states        {:default {:border-color 0x000000}
                                                          :active  {:border-color 0x00c3ff}}
                                          :fill          0xFFFFFF}
               (keyword image-name)      {:type      "image"
                                          :src       src
                                          :x         (/ width 2)
                                          :y         (/ height 2)
                                          :min-width (- width (* p/option-padding 2))
                                          :max-width (- width (* p/option-padding 2))
                                          :origin    {:type "center-center"}
                                          :editable? {:select true}}}}))

(defn- create-text
  [{:keys [object-name x y width height text]}]
  {:objects {(keyword object-name) {:type           "text"
                                    :text           text
                                    :x              x
                                    :y              (+ y (/ height 2))
                                    :width          width
                                    :font-size      p/option-font-size
                                    :vertical-align "middle"
                                    :editable?      {:select true}}}})

(defn create
  [{:keys [object-name x y width height img text on-option-click on-option-voice-over-click value]
    :or   {x 0
           y 0}}]
  (let [label-height 80

        image-name (str object-name "-image")
        text-name (str object-name "-text")
        button-name (str object-name "-button")

        image-ratio 1.25
        image-width width
        image-height (Math/min (* image-width image-ratio)
                               (- height label-height p/options-gap))

        text-x (+ voice-over/default-size p/voice-over-margin)
        text-y (+ image-height p/options-gap)
        text-width (- width text-x)
        text-height label-height]
    (merge-data {:objects {(keyword object-name) {:type        "group"
                                                  :object-name object-name
                                                  :x           x
                                                  :y           y
                                                  :children    [image-name text-name button-name]}}}
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
                              :height      text-height
                              :text        text})
                (voice-over/create {:object-name     button-name
                                    :x               0
                                    :y               (+ image-height p/options-gap)
                                    :on-click        on-option-voice-over-click
                                    :on-click-params {:value value}}))))
