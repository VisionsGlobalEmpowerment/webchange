(ns webchange.question.create-multiple-choice-text
  (:require
    [webchange.question.common.background :as background]
    [webchange.question.common.layout-markup :refer [get-layout-coordinates]]
    [webchange.question.common.options-list :as options-list]
    [webchange.question.common.substrate :as substrate]
    [webchange.question.common.task-image :as task-image]
    [webchange.question.common.task-text :as task-text]
    [webchange.question.common.params :as params]
    [webchange.question.utils :refer [merge-data]]))

(def common-params {:x             0
                    :y             0
                    :width         1920
                    :height        1080
                    :primary-color 0xFFA301
                    :sides-ratio-h 0.4
                    :padding       10})

(defn- get-options-horizontal-frame
  [{:keys [width options-number]}]
  (let [{option-height    :height
         option-max-width :max-width
         option-min-width :min-width} (:text params/option)
        {:keys [gap]} params/options

        option-width (-> (-> (+ width gap) (/ 2) (- gap))
                         (max option-min-width)
                         (min option-max-width))]
    (-> (case options-number
          1 {:list-width  option-width
             :list-height option-height
             :positions   {0 {:x 0
                              :y 0}}}
          2 {:list-width  (-> (* 2 option-width) (+ gap))
             :list-height option-height
             :positions   {0 {:x 0
                              :y 0}
                           1 {:x (+ option-width gap)
                              :y 0}}}
          3 {:list-width  (-> (* 2 option-width) (+ gap))
             :list-height (-> (* 2 option-height) (+ gap))
             :positions   {0 {:x 0
                              :y 0}
                           1 {:x (+ option-width gap)
                              :y 0}
                           2 {:x (-> (+ option-width gap) (/ 2))
                              :y (+ option-height gap)}}}
          4 {:list-width  (-> (* 2 option-width) (+ gap))
             :list-height (-> (* 2 option-height) (+ gap))
             :positions   {0 {:x 0
                              :y 0}
                           1 {:x (+ option-width gap)
                              :y 0}
                           2 {:x 0
                              :y (+ option-height gap)}
                           3 {:x (+ option-width gap)
                              :y (+ option-height gap)}}})
        (merge {:option-width  option-width
                :option-height option-height}))))

(defn- get-options-vertical-frame
  [{:keys [width options-number]}]
  (let [{option-height    :height
         option-max-width :max-width
         option-min-width :min-width} (:text params/option)
        {:keys [gap]} params/options

        option-width (-> width
                         (max option-min-width)
                         (min option-max-width))]
    {:list-width    option-width
     :list-height   (-> (+ option-height gap) (* options-number) (- gap))
     :positions     (->> (range options-number)
                         (map (fn [idx]
                                [idx {:x 0
                                      :y (-> (+ option-height gap) (* idx))}]))
                         (into {}))
     :option-width  option-width
     :option-height option-height}))

(defn- get-options-frame
  [{:keys [layout] :as props}]
  (case layout
    "horizontal" (get-options-vertical-frame props)
    "vertical" (get-options-horizontal-frame props)))

(defn- create-options
  [{:keys [width height options] :as props}
   {:keys [layout] :as form-data}]
  (let [options-number (count options)
        frame (get-options-frame {:width          (- width (* 2 params/block-padding))
                                  :height         (- height (* 2 params/block-padding))
                                  :options-number options-number
                                  :layout         layout})]
    (options-list/create (merge props {:frame frame}) form-data)))

(defn create
  [{:keys [object-name visible?] :as props}
   {:keys [alias layout options task] :as form-data}]
  (let [{task-text :text task-type :type} task
        {options :data options-label :label} options

        show-task-image? (= task-type "text-image")

        substrate-name (str object-name "-substrate")
        background-name (str object-name "-background")
        task-text-name (str object-name "-task-text")
        options-name (str object-name "-options")
        task-image-name (str object-name "-task-image")

        layout-coordinates (get-layout-coordinates {:layout      layout
                                                    :with-image? (= task-type "text-image")})]
    (cond-> {:objects {(keyword object-name) {:type      "group"
                                              :alias     alias
                                              :x         (:x common-params)
                                              :y         (:y common-params)
                                              :children  (cond-> [substrate-name
                                                                  background-name
                                                                  task-text-name options-name]
                                                                 show-task-image? (conj task-image-name))
                                              :visible   visible?
                                              :editable? {:show-in-tree? true}}}}
            :always (merge-data (substrate/create {:object-name substrate-name}))
            :always (merge-data (background/create (merge {:object-name background-name}
                                                          (:background layout-coordinates))))
            :always (merge-data (task-text/create (merge props
                                                         (:text layout-coordinates)
                                                         {:object-name task-text-name
                                                          :text        task-text})))
            :always (merge-data (create-options (merge props
                                                       (:options layout-coordinates)
                                                       {:object-name options-name
                                                        :label-type  options-label
                                                        :options     options})
                                                form-data))
            show-task-image? (merge-data (task-image/create (merge props
                                                                   (:image layout-coordinates)
                                                                   {:object-name task-image-name})
                                                            form-data)))))
