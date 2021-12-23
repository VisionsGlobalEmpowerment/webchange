(ns webchange.question.create-multiple-choice-text
  (:require
    [webchange.question.common.background :as background]
    [webchange.question.common.layout-markup :refer [get-layout-coordinates]]
    [webchange.question.common.options-list :as options-list]
    [webchange.question.common.substrate :as substrate]
    [webchange.question.common.task-image :as task-image]
    [webchange.question.common.task-text :as task-text]
    [webchange.question.common.params :as params]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [merge-data]]))

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
  [{:keys [layout options-number] :as form-data}
   {:keys [width height question-id] :as props}]
  (let [options (->> (range options-number)
                     (map inc)
                     (map (fn [option-idx]
                            (let [option-name (str "option-" option-idx)
                                  text-prop-name (str option-name "-text")]
                              {:value      option-name
                               :text-name  (param-name->object-name text-prop-name question-id)
                               :text-props (->> text-prop-name keyword (get form-data))}))))
        frame (get-options-frame {:width          (- width (* 2 params/block-padding))
                                  :height         (- height (* 2 params/block-padding))
                                  :options-number options-number
                                  :layout         layout})]
    (options-list/create options form-data (merge props {:frame frame}))))

(defn create
  [{:keys [alias layout options task-type] :as form-data}
   {:keys [object-name task-text-name question-id visible? task-image-param-name] :as props}]
  (let [{options :data options-label :label} options

        show-task-image? (= task-type "text-image")

        substrate-name (str object-name "-substrate")
        background-name (str object-name "-background")
        options-name (str object-name "-options")
        task-text-group-name (str object-name "-task-text-group")

        layout-coordinates (get-layout-coordinates {:layout      layout
                                                    :with-image? show-task-image?})]
    (cond-> {:objects {(keyword object-name) {:type      "group"
                                              :alias     alias
                                              :x         (:x params/template-size)
                                              :y         (:y params/template-size)
                                              :children  (cond-> [substrate-name
                                                                  background-name
                                                                  task-text-group-name
                                                                  options-name]
                                                                 show-task-image? (conj (param-name->object-name task-image-param-name question-id)))
                                              :visible   visible?
                                              :editable? {:show-in-tree? true}}}}
            :always (merge-data (substrate/create {:object-name substrate-name}))
            :always (merge-data (background/create (merge {:object-name background-name}
                                                          (:background layout-coordinates))))
            :always (merge-data (task-text/create form-data
                                                  (merge props
                                                         (:text layout-coordinates)
                                                         {:object-name task-text-group-name
                                                          :param-name  "task-text"
                                                          :question-id question-id})))
            :always (merge-data (create-options form-data
                                                (merge props
                                                       (:options layout-coordinates)
                                                       {:object-name options-name
                                                        :label-type  options-label
                                                        :options     options
                                                        :question-id question-id})))
            show-task-image? (merge-data (task-image/create form-data
                                                            (merge props
                                                                   (:image layout-coordinates)
                                                                   {:param-name  task-image-param-name
                                                                    :question-id question-id}))))))
