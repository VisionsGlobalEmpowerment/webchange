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
                                                  :x        (+ x p/block-padding)
                                                  :y        (+ y p/block-padding)
                                                  :children [button-name text-name]}
                           (keyword text-name)   {:type           "text"
                                                  :text           text
                                                  :x              (+ voice-over/default-size p/voice-over-margin)
                                                  :y              0
                                                  :width          (- width voice-over/default-size p/voice-over-margin (* 2 p/block-padding))
                                                  :word-wrap      true
                                                  :font-size      p/task-font-size
                                                  :vertical-align "top"
                                                  :editable?      {:select true}}}}
                (voice-over/create {:object-name button-name
                                    :x           0
                                    :y           0
                                    :on-click    on-task-voice-over-click}))))

(defn- create-options
  [{:keys [object-name x y width height label-type options on-check-click on-option-click on-option-voice-over-click question-id]}
   {:keys [answers-number]}]
  (let [check-button-name (str object-name "-check-button")
        check-button-height (+ p/check-button-size (* p/block-padding 2))
        check-button-x (- (/ width 2) (/ p/check-button-size 2))
        check-button-y (+ (- height check-button-height) p/block-padding)

        list-x p/block-padding
        list-y p/block-padding
        list-width (- width (* 2 p/block-padding))
        list-height (- height p/block-padding check-button-height)

        show-check-button? (= answers-number "many")
        options-count (count options)
        max-option-width 440
        option-width (min (-> (- list-width p/options-gap)
                              (/ options-count))
                          max-option-width)
        offset-left (->> (* option-width options-count)
                         (- list-width)
                         (* 0.5))]
    (->> (map-indexed vector options)
         (reduce (fn [result [idx {:keys [img text value]}]]
                   (let [option-name (str object-name "-option-" idx)]
                     (-> result
                         (update-in [:objects (keyword object-name) :children] conj option-name)
                         (merge-data (option-image/create {:object-name                option-name
                                                           :x                          (->> (+ option-width p/options-gap)
                                                                                            (* idx)
                                                                                            (+ offset-left)
                                                                                            (+ list-x))
                                                           :y                          list-y
                                                           :width                      (- option-width p/options-gap)
                                                           :height                     list-height
                                                           :img                        img
                                                           :text                       text
                                                           :label-type                 label-type
                                                           :on-option-click            on-option-click
                                                           :on-option-voice-over-click on-option-voice-over-click
                                                           :value                      value
                                                           :question-id                question-id})))))
                 (cond-> {:objects {(keyword object-name) {:type     "group"
                                                           :x        x
                                                           :y        y
                                                           :children (cond-> []
                                                                             show-check-button? (conj check-button-name))}}}
                         show-check-button? (merge-data (check-button/create {:object-name check-button-name
                                                                              :x           check-button-x
                                                                              :y           check-button-y
                                                                              :on-click    on-check-click})))))))

(defn- create-task-image
  [{:keys [x y width height object-name]}
   {:keys [task]}]
  (let [{task-image :img} task
        image-x (+ x (/ width 2))
        image-y (+ y (/ height 2))
        image-width (- width (* p/block-padding 2))
        image-height (- height (* p/block-padding 2))]
    {:objects {(keyword object-name) {:type       "image"
                                      :src        task-image
                                      :x          image-x
                                      :y          image-y
                                      :max-width  image-width
                                      :max-height image-height
                                      :origin     {:type "center-center"}
                                      :editable?  {:select true}}}}))

(defn- get-layout-coordinates
  [{:keys [layout task]}]
  (let [{:keys [width height]} common-params
        small-side (->> 1.618 (inc) (/ 1))
        big-side (- 1 small-side)
        {task-type :type} task]
    (if (= task-type "text")
      {:text       {:x      0
                    :y      0
                    :width  width
                    :height (* small-side height)}
       :options    {:x      0
                    :y      (* small-side height)
                    :width  width
                    :height (* big-side height)}
       :background {:x      0
                    :y      0
                    :width  width
                    :height height}}
      (case layout
        "horizontal" (let [left-side-width (* small-side width)]
                       {:image      {:x      0
                                     :y      0
                                     :width  left-side-width
                                     :height height}
                        :text       {:x      left-side-width
                                     :y      0
                                     :width  (- width left-side-width)
                                     :height (* small-side height)}
                        :options    {:x      left-side-width
                                     :y      (* small-side height)
                                     :width  (- width left-side-width)
                                     :height (* big-side height)}
                        :background {:x      left-side-width
                                     :y      0
                                     :width  (- width left-side-width)
                                     :height height}})
        "vertical" (let [top-side-height (* small-side height)]
                     {:image      {:x      0
                                   :y      0
                                   :width  (* small-side width)
                                   :height top-side-height}
                      :text       {:x      (* small-side width)
                                   :y      0
                                   :width  (* big-side width)
                                   :height top-side-height}
                      :options    {:x      0
                                   :y      top-side-height
                                   :width  width
                                   :height (- height top-side-height)}
                      :background {:x      0
                                   :y      top-side-height
                                   :width  width
                                   :height (- height top-side-height)}})))))

(defn create
  [{:keys [object-name visible?] :as props}
   {:keys [alias options task] :as form-data}]
  (let [{task-text :text task-type :type} task
        {options :data options-label :label} options

        show-task-image? (= task-type "text-image")

        substrate-name (str object-name "-substrate")
        background-name (str object-name "-background")
        task-text-name (str object-name "-task-text")
        options-name (str object-name "-options")
        task-image-name (str object-name "-task-image")

        layout-coordinates (get-layout-coordinates form-data)]
    (cond-> {:objects {(keyword object-name) {:type      "group"
                                              :alias     alias
                                              :x         (:x common-params)
                                              :y         (:y common-params)
                                              :children  (cond-> [substrate-name background-name task-text-name options-name]
                                                                 show-task-image? (conj task-image-name))
                                              :visible   visible?
                                              :editable? {:show-in-tree? true}}}}
            :always (merge-data (create-substrate {:object-name substrate-name
                                                   :width       (:width common-params)
                                                   :height      (:height common-params)}))
            :always (merge-data (create-background (merge {:object-name background-name
                                                           :color       (:primary-color common-params)}
                                                          (:background layout-coordinates))))
            :always (merge-data (create-task-text (merge props
                                                         (:text layout-coordinates)
                                                         {:object-name task-text-name
                                                          :text        task-text})))
            :always (merge-data (create-options (merge props
                                                       (:options layout-coordinates)
                                                       {:object-name options-name
                                                        :label-type  options-label
                                                        :options     options})
                                                form-data))
            show-task-image? (merge-data (create-task-image (merge props
                                                                   (:image layout-coordinates)
                                                                   {:object-name task-image-name})
                                                            form-data)))))
