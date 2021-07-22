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

(defn- add-task-image
  [result parent-name {:keys [task]}]
  (let [{task-image :img} task
        task-image-name (str parent-name "-task-image")

        {:keys [sides-ratio-h width height]} common-params
        task-image-container-width (* sides-ratio-h width)
        task-image-container-height height]
    (-> result
        (update-in [(keyword parent-name) :children] conj task-image-name)
        (merge (create-task-image {:object-name task-image-name
                                   :src         task-image
                                   :width       task-image-container-width
                                   :height      task-image-container-height})))))

(defn- add-main-content
  [result parent-name
   {:keys [on-option-click on-option-voice-over-click on-task-voice-over-click]}
   {:keys [options task]}]
  (let [{:keys [sides-ratio-h width]} common-params

        background-name (str parent-name "-background")
        task-text-name (str parent-name "-task-text")
        options-name (str parent-name "-options")

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
        task-text-container-height (- (* main-content-height sides-ratio-h) (* 2 p/block-padding))

        options-text-container-x (+ main-content-x p/block-padding)
        options-text-container-y (+ task-text-container-y task-text-container-height p/block-padding task-text-container-y)
        options-text-container-width (- main-content-width (* 2 p/block-padding))
        options-text-container-height (- main-content-height options-text-container-y p/block-padding)]
    (-> result
        (update-in [(keyword parent-name) :children] conj background-name)
        (merge (create-background {:object-name background-name
                                   :x           main-content-x
                                   :y           main-content-y
                                   :width       main-content-width
                                   :height      (:height common-params)
                                   :color       (:primary-color common-params)}))

        (update-in [(keyword parent-name) :children] conj task-text-name)
        (merge (create-task-text {:object-name              task-text-name
                                  :x                        task-text-container-x
                                  :y                        task-text-container-y
                                  :width                    task-text-container-width
                                  :height                   task-text-container-height
                                  :text                     task-text
                                  :on-task-voice-over-click on-task-voice-over-click}))

        (update-in [(keyword parent-name) :children] conj options-name)
        (merge (create-options {:object-name                options-name
                                :x                          options-text-container-x
                                :y                          options-text-container-y
                                :width                      options-text-container-width
                                :height                     options-text-container-height
                                :options                    options
                                :on-option-click            on-option-click
                                :on-option-voice-over-click on-option-voice-over-click})))))

(defn create
  [{:keys [object-name visible?] :as props}
   {:keys [alias task] :as args}]
  (let [substrate-name (str object-name "-substrate")
        {task-type :type} task]
    (cond-> (merge {(keyword object-name) {:type      "group"
                                           :alias     alias
                                           :x         (:x common-params)
                                           :y         (:y common-params)
                                           :children  [substrate-name]
                                           :visible   visible?
                                           :states    {:invisible {:visible false}
                                                       :visible   {:visible true}}
                                           :editable? {:show-in-tree? true}}}
                   (create-substrate {:object-name substrate-name
                                      :width       (:width common-params)
                                      :height      (:height common-params)}))
            :always (add-main-content object-name props args)
            (= task-type "text-image") (add-task-image object-name args))))
