(ns webchange.question.create-multiple-choice-image
  (:require
    [webchange.question.create-common-check-button :as check-button]
    [webchange.question.create-common-option-image :as option-image]
    [webchange.question.create-common-voice-over :as voice-over]
    [webchange.question.params :as p]
    [webchange.question.utils :refer [merge-data]]))

(def common-params {:x             0
                    :y             0
                    :width         1920
                    :height        1080
                    :primary-color 0xFFA301
                    :sides-ratio-h 0.4
                    :padding       10})

(defn- create-substrate
  [{:keys [object-name width height]}]
  {:objects {(keyword object-name) {:type   "rectangle"
                                    :x      0
                                    :y      0
                                    :width  width
                                    :height height
                                    :fill   0xFFFFFF}}})

(defn- create-background
  [{:keys [object-name x y width height color]}]
  {:objects {(keyword object-name) {:type        "rectangle"
                                    :object-name object-name
                                    :x           x
                                    :y           y
                                    :width       width
                                    :height      height
                                    :fill        color}}})

(defn- create-task-text
  [{:keys [object-name x y width height text on-task-voice-over-click]}]
  (let [button-name (str object-name "-button")
        text-name (str object-name "-text")]
    (merge-data {:objects {(keyword object-name) {:type     "group"
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
                                                  :editable?      {:select true}}}}
                (voice-over/create {:object-name button-name
                                    :x           0
                                    :y           0
                                    :on-click    on-task-voice-over-click}))))

(defn- create-options
  [{:keys [object-name x y width height options on-option-click on-option-voice-over-click]}]
  (let [options-count (count options)
        max-option-width 440
        option-width (Math/min (-> (- width p/options-gap)
                                   (/ options-count))
                               max-option-width)
        offset-left (->> (* option-width options-count)
                         (- width)
                         (* 0.5))
        option-height height]
    (->> (map-indexed vector options)
         (reduce (fn [result [idx {:keys [img text value]}]]
                   (let [option-name (str object-name "-option-" idx)]
                     (-> result
                         (update-in [:objects (keyword object-name) :children] conj option-name)
                         (merge-data (option-image/create {:object-name                option-name
                                                           :x                          (->> (+ option-width p/options-gap)
                                                                                            (* idx)
                                                                                            (+ offset-left))
                                                           :y                          0
                                                           :width                      (- option-width p/options-gap)
                                                           :height                     option-height
                                                           :img                        img
                                                           :text                       text
                                                           :on-option-click            on-option-click
                                                           :on-option-voice-over-click on-option-voice-over-click
                                                           :value                      value})))))
                 {:objects {(keyword object-name) {:type     "group"
                                                   :x        x
                                                   :y        y
                                                   :children []}}}))))

(defn- create-task-image
  [{:keys [x y object-name] :or {x 0 y 0}}
   {:keys [task]}]
  (let [{task-image :img} task

        {:keys [sides-ratio-h width height]} common-params
        task-image-container-width (* sides-ratio-h width)
        task-image-container-height height

        image-x (+ x (/ task-image-container-width 2))
        image-y (+ y (/ task-image-container-height 2))
        image-width (- task-image-container-width (* (:padding common-params) 2))
        image-height (- task-image-container-height (* (:padding common-params) 2))]
    {:objects {(keyword object-name) {:type       "image"
                                      :src        task-image
                                      :x          image-x
                                      :y          image-y
                                      :max-width  image-width
                                      :max-height image-height
                                      :origin     {:type "center-center"}
                                      :editable?  {:select true}}}}))

(defn- create-main-content
  [{:keys [object-name]}
   {:keys [on-check-click on-option-click on-option-voice-over-click on-task-voice-over-click]}
   {:keys [answers-number options task] :as args}]
  (let [{:keys [sides-ratio-h width]} common-params

        background-name (str object-name "-background")
        task-text-name (str object-name "-task-text")
        options-name (str object-name "-options")

        {task-text :text task-type :type} task
        options (:data options)

        {main-content-x      :x
         main-content-y      :y
         main-content-width  :width
         main-content-height :height} (if (= task-type "text-image")
                                        (let [x (* sides-ratio-h width)]
                                          {:x      x
                                           :y      0
                                           :width  (- width x)
                                           :height (:height common-params)})
                                        {:x      0
                                         :y      0
                                         :width  width
                                         :height (:height common-params)})

        task-text-container-x (+ main-content-x p/block-padding)
        task-text-container-y p/block-padding
        task-text-container-width (- main-content-width (* 2 p/block-padding))
        task-text-container-height (- (* main-content-height p/main-content-blocks-ratio) (* 2 p/block-padding))

        show-check-button? (= answers-number "many")
        check-button-block-height (+ p/check-button-size p/block-padding)
        check-button-name (str object-name "-check-button")
        check-button-x (- (+ main-content-x (/ main-content-width 2))
                          (/ p/check-button-size 2))
        check-button-y (- (+ main-content-y main-content-height)
                          p/block-padding p/check-button-size)

        options-text-container-x (+ main-content-x p/block-padding)
        options-text-container-y (+ task-text-container-y task-text-container-height p/block-padding task-text-container-y)
        options-text-container-width (- main-content-width (* 2 p/block-padding))
        options-text-container-height (- main-content-height options-text-container-y check-button-block-height p/block-padding)]
    (cond-> {:objects {(keyword object-name) {:type     "group"
                                              :x        0 :y 0
                                              :children (cond-> [background-name task-text-name options-name]
                                                                show-check-button? (conj check-button-name))}}}
            :always (merge-data (create-background {:object-name background-name
                                                    :x           main-content-x
                                                    :y           main-content-y
                                                    :width       main-content-width
                                                    :height      (:height common-params)
                                                    :color       (:primary-color common-params)}))
            :always (merge-data (create-task-text {:object-name              task-text-name
                                                   :x                        task-text-container-x
                                                   :y                        task-text-container-y
                                                   :width                    task-text-container-width
                                                   :height                   task-text-container-height
                                                   :text                     task-text
                                                   :on-task-voice-over-click on-task-voice-over-click}))
            :always (merge-data (create-options {:object-name                options-name
                                                 :x                          options-text-container-x
                                                 :y                          options-text-container-y
                                                 :width                      options-text-container-width
                                                 :height                     options-text-container-height
                                                 :options                    options
                                                 :on-option-click            on-option-click
                                                 :on-option-voice-over-click on-option-voice-over-click}))
            show-check-button? (merge-data (check-button/create {:object-name check-button-name
                                                                 :x           check-button-x
                                                                 :y           check-button-y
                                                                 :on-click    on-check-click})))))

(defn create
  [{:keys [object-name visible?] :as props}
   {:keys [alias task] :as args}]
  (let [substrate-name (str object-name "-substrate")
        main-content-name (str object-name "-main-content")

        {task-type :type} task
        show-task-image? (= task-type "text-image")
        task-image-name (str object-name "-task-image")]
    (cond-> {:objects {(keyword object-name) {:type      "group"
                                              :alias     alias
                                              :x         (:x common-params)
                                              :y         (:y common-params)
                                              :children  (cond-> [substrate-name main-content-name]
                                                                 show-task-image? (conj task-image-name))
                                              :visible   visible?
                                              :states    {:invisible {:visible false} ;; change to 'set-attribute'
                                                          :visible   {:visible true}}
                                              :editable? {:show-in-tree? true}}}}
            :always (merge-data (create-substrate {:object-name substrate-name
                                                   :width       (:width common-params)
                                                   :height      (:height common-params)}))
            :always (merge-data (create-main-content {:object-name main-content-name} props args))
            show-task-image? (merge-data (create-task-image {:object-name task-image-name} args)))))
