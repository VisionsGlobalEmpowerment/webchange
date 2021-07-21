(ns webchange.question.create-multiple-choice-image
  (:require
    [webchange.question.create-common-option-image :as option-image]
    [webchange.question.create-common-voice-over :as voice-over]
    [webchange.question.params :as p]))

(def common-params {:x             0
                    :y             0
                    :width         1920
                    :height        1080
                    :primary-color 0xFFA301
                    :sides-ratio-h 0.4
                    :padding       10})

(defn- create-substrate
  [{:keys [object-name width height]}]
  {(keyword object-name) {:type   "rectangle"
                          :x      0
                          :y      0
                          :width  width
                          :height height
                          :fill   0xFFFFFF}})

(defn- create-background
  [{:keys [object-name x y width height color]}]
  {(keyword object-name) {:type        "rectangle"
                          :object-name object-name
                          :x           x
                          :y           y
                          :width       width
                          :height      height
                          :fill        color}})

(defn- create-task-image
  [{:keys [object-name src x y width height padding]
    :or   {x       0
           y       0
           padding (:padding common-params)}}]
  (let [image-x (+ x (/ width 2))
        image-y (+ y (/ height 2))
        image-width (- width (* padding 2))
        image-height (- height (* padding 2))]
    {(keyword object-name) {:type       "image"
                            :src        src
                            :x          image-x
                            :y          image-y
                            :max-width  image-width
                            :max-height image-height
                            :origin     {:type "center-center"}
                            :editable?  {:select true}}}))

(defn- create-task-text
  [{:keys [object-name x y width height text on-task-voice-over-click]}]
  (let [button-name (str object-name "-button")
        text-name (str object-name "-text")]
    (merge {(keyword object-name) {:type     "group"
                                   :x        x
                                   :y        y
                                   :children [button-name text-name]}
            (keyword text-name)   {:type           "text"
                                   :text           text
                                   :x              (+ voice-over/default-size p/voice-over-margin)
                                   :y              0
                                   :width          (- width voice-over/default-size p/voice-over-margin)
                                   :word-wrap      true
                                   :font-size      p/task-font-size
                                   :vertical-align "top"
                                   :editable?      {:select true}}}
           (voice-over/create {:object-name button-name
                               :x           0
                               :y           0
                               :on-click    on-task-voice-over-click}))))

(defn- create-options
  [{:keys [object-name x y width height options on-option-click on-option-voice-over-click]}]
  (let [options-count (count options)
        option-width (/ (- width p/options-gap) options-count)
        option-height height]
    (->> (map-indexed vector options)
         (reduce (fn [result [idx {:keys [img text value]}]]
                   (let [option-name (str object-name "-option-" idx)
                         option-data (option-image/create {:object-name                option-name
                                                           :x                          (* idx (+ option-width p/options-gap))
                                                           :y                          0
                                                           :width                      (- option-width p/options-gap)
                                                           :height                     option-height
                                                           :img                        img
                                                           :text                       text
                                                           :on-option-click            on-option-click
                                                           :on-option-voice-over-click on-option-voice-over-click
                                                           :value                      value})]
                     (-> result
                         (update-in [(keyword object-name) :children] conj option-name)
                         (merge option-data))))
                 {(keyword object-name) {:type     "group"
                                         :x        x
                                         :y        y
                                         :children []}}))))

(defn create
  [{:keys [object-name on-option-click on-option-voice-over-click on-task-voice-over-click visible?]} args]
  (let [{:keys [sides-ratio-h width] :as common-params} common-params

        substrate-name (str object-name "-substrate")
        background-name (str object-name "-background")
        task-image-name (str object-name "-task-image")
        task-text-name (str object-name "-task-text")
        options-name (str object-name "-options")

        alias (get args :alias object-name)
        task-image (get-in args [:task :img])
        task-text (get-in args [:task :text])
        options (get-in args [:options :data])

        main-content-x (* sides-ratio-h width)
        main-content-width (- width main-content-x)
        main-content-height (:height common-params)

        task-image-container-width (* sides-ratio-h width)
        task-image-container-height (:height common-params)

        task-text-container-x (+ main-content-x p/block-padding)
        task-text-container-y p/block-padding
        task-text-container-width (- main-content-width (* 2 p/block-padding))
        task-text-container-height (- (* main-content-height sides-ratio-h) (* 2 p/block-padding))

        options-text-container-x (+ main-content-x p/block-padding)
        options-text-container-y (+ task-text-container-height p/block-padding)
        options-text-container-width (- main-content-width (* 2 p/block-padding))
        options-text-container-height (- main-content-height task-text-container-height (* 2 p/block-padding))]
    (merge {(keyword object-name) {:type      "group"
                                   :alias     alias
                                   :x         (:x common-params)
                                   :y         (:y common-params)
                                   :children  [substrate-name background-name task-image-name task-text-name options-name]
                                   :visible   visible?
                                   :states    {:invisible {:visible false}
                                               :visible   {:visible true}}
                                   :editable? {:show-in-tree? true}}}
           (create-substrate {:object-name substrate-name
                              :width       (:width common-params)
                              :height      (:height common-params)})
           (create-background {:object-name background-name
                               :x           main-content-x
                               :y           0
                               :width       main-content-width
                               :height      (:height common-params)
                               :color       (:primary-color common-params)})
           (create-task-image {:object-name task-image-name
                               :src         task-image
                               :width       task-image-container-width
                               :height      task-image-container-height})
           (create-task-text {:object-name              task-text-name
                              :x                        task-text-container-x
                              :y                        task-text-container-y
                              :width                    task-text-container-width
                              :height                   task-text-container-height
                              :text                     task-text
                              :on-task-voice-over-click on-task-voice-over-click})
           (create-options {:object-name                options-name
                            :x                          options-text-container-x
                            :y                          options-text-container-y
                            :width                      options-text-container-width
                            :height                     options-text-container-height
                            :options                    options
                            :on-option-click            on-option-click
                            :on-option-voice-over-click on-option-voice-over-click}))))
