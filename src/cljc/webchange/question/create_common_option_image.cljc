(ns webchange.question.create-common-option-image
  (:require
    [webchange.question.create-common-voice-over :as voice-over]
    [webchange.question.params :as p]
    [webchange.question.utils :refer [merge-data]]))

(defn- create-image
  [{:keys [object-name x y width height src on-click value]
    :or   {x 0
           y 0}}]
  (let [background-name (str object-name "-background")
        image-name (str object-name "-image")
        image-activate-name (str object-name "-image-activate")
        image-inactivate-name (str object-name "-image-inactivate")
        actions (cond-> {}
                        (some? on-click) (assoc :click (cond-> {:type "action"
                                                                :on   "click"
                                                                :id   on-click}
                                                               (some? value) (assoc :params {:value value}))))]
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
               (keyword image-name)      {:type       "image"
                                          :src        src
                                          :x          (/ width 2)
                                          :y          (/ height 2)
                                          :width      (- width (* p/option-padding 2))
                                          :height     (- height (* p/option-padding 2))
                                          :image-size "contain"
                                          :origin     {:type "center-center"}
                                          :editable?  {:select true}}}
     :actions {(keyword image-activate-name)   {:type   "state"
                                                :target background-name
                                                :id     "active"
                                                :tags   [(str "activate-option-" value)]}
               (keyword image-inactivate-name) {:type   "state"
                                                :target background-name
                                                :id     "default"
                                                :tags   ["inactivate-options" (str "inactivate-option-" value)]}}}))

(defn- create-text
  [{:keys [object-name x y width height text]}]
  {:objects {(keyword object-name) {:type           "text"
                                    :text           text
                                    :x              x
                                    :y              (+ y (/ height 2))
                                    :width          width
                                    :word-wrap      true
                                    :font-size      p/option-font-size
                                    :vertical-align "middle"
                                    :editable?      {:select true}}}})

(defn create
  [{:keys [object-name x y width height img text label-type on-option-click on-option-voice-over-click value]
    :or   {x 0
           y 0}}]
  (let [show-text? (= label-type "audio-text")
        show-voice-over? (not= label-type "none")

        label-height 80

        image-name (str object-name "-image")
        text-name (str object-name "-text")
        button-name (str object-name "-button")

        button-x (if show-text? 0 (- (/ width 2)
                                     (/ voice-over/default-size 2)))

        image-ratio 1.25
        image-width width
        image-height (Math/min (* image-width image-ratio)
                               (- height label-height p/options-gap))

        text-x (+ voice-over/default-size p/voice-over-margin)
        text-y (+ image-height p/options-gap)
        text-width (- width text-x)
        text-height label-height]
    (cond-> {:objects {(keyword object-name) {:type        "group"
                                              :object-name object-name
                                              :x           x
                                              :y           y
                                              :children    (cond-> [image-name]
                                                                   show-text? (conj text-name)
                                                                   show-voice-over? (conj button-name))}}}
            :always (merge-data (create-image {:object-name image-name
                                               :src         img
                                               :width       image-width
                                               :height      image-height
                                               :on-click    on-option-click
                                               :value       value}))
            show-text? (merge-data (create-text {:object-name text-name
                                                 :x           text-x
                                                 :y           text-y
                                                 :width       text-width
                                                 :height      text-height
                                                 :text        text}))
            show-voice-over? (merge-data (voice-over/create {:object-name     button-name
                                                             :x               button-x
                                                             :y               (+ image-height p/options-gap)
                                                             :on-click        on-option-voice-over-click
                                                             :on-click-params {:value value}})))))
