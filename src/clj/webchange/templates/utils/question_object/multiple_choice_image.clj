(ns webchange.templates.utils.question-object.multiple-choice-image
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.utils.question-object.common-option-image :as option-image]
    [webchange.templates.utils.question-object.common-voice-over :as voice-over]))

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
  [{:keys [object-name x y width height text]}]
  (let [button-name (str object-name "-button")
        text-name (str object-name "-text")]
    (merge {(keyword object-name) {:type     "group"
                                   :x        x
                                   :y        y
                                   :children [button-name text-name]}
            (keyword text-name)   {:type           "text"
                                   :text           text
                                   :x              voice-over/default-size
                                   :y              0
                                   :width          (- width voice-over/default-size)
                                   :word-wrap      true
                                   :font-size      60
                                   :vertical-align "top"
                                   :editable?      {:select true}}}
           (voice-over/create {:object-name button-name
                               :x           0
                               :y           0}))))

(defn- create-options
  [{:keys [object-name x y width height options gap on-option-click on-option-voice-over-click]
    :or   {gap 15}}]
  (let [options-count (count options)
        option-width (/ (- width gap) options-count)
        option-height height]
    (->> (map-indexed vector options)
         (reduce (fn [result [idx {:keys [img text value]}]]
                   (let [option-name (str object-name "-option-" idx)
                         option-data (option-image/create {:object-name                option-name
                                                           :x                          (+ (/ gap 2) (* idx option-width) gap)
                                                           :y                          gap
                                                           :width                      (- option-width (* 2 gap))
                                                           :height                     (- option-height (* 2 gap))
                                                           :img                        img
                                                           :text                       text
                                                           :idx                        idx
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
  [{:keys [object-name on-option-click on-option-voice-over-click]} args]
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

        task-text-container-x main-content-x
        task-text-container-y 0
        task-text-container-width main-content-width
        task-text-container-height (* main-content-height sides-ratio-h)

        options-text-container-x main-content-x
        options-text-container-y task-text-container-height
        options-text-container-width main-content-width
        options-text-container-height (- main-content-height task-text-container-height)]
    (merge {(keyword object-name) {:type      "group"
                                   :alias     alias
                                   :x         (:x common-params)
                                   :y         (:y common-params)
                                   :children  [substrate-name background-name task-image-name task-text-name options-name]
                                   :visible   false
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
           (create-task-text {:object-name task-text-name
                              :x           task-text-container-x
                              :y           task-text-container-y
                              :width       task-text-container-width
                              :height      task-text-container-height
                              :text        task-text})
           (create-options {:object-name                options-name
                            :x                          options-text-container-x
                            :y                          options-text-container-y
                            :width                      options-text-container-width
                            :height                     options-text-container-height
                            :options                    options
                            :on-option-click            on-option-click
                            :on-option-voice-over-click on-option-voice-over-click}))))
