(ns webchange.question.create-thumbs-up-n-down
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

(defn- get-options-frame
  [{:keys [options-number]}]
  (let [{:keys [gap]} params/options
        option-width (:mark-size params/option)
        option-height (:mark-size params/option)]
    {:list-width    (-> (+ option-width gap) (* options-number) (- gap))
     :list-height   option-height
     :positions     (->> (range options-number)
                         (map (fn [idx]
                                [idx {:x (-> (+ option-width gap) (* idx))
                                      :y 0}]))
                         (into {}))
     :option-width  option-width
     :option-height option-height}))

(defn- create-options
  [{:keys [layout mark-options] :as form-data}
   {:keys [question-id width height] :as props}]
  (let [options (->> mark-options
                     (map (fn [mark-option]
                            (let [value-prop-name (str mark-option "-value")
                                  text-prop-name (str mark-option "-text")
                                  image-prop-name (str mark-option "-image")]
                              {:value       (->> value-prop-name keyword (get form-data))
                               :text-name   (param-name->object-name text-prop-name question-id)
                               :text-props  (->> text-prop-name keyword (get form-data))
                               :image-name  (param-name->object-name image-prop-name question-id)
                               :image-props (->> image-prop-name keyword (get form-data))}))))
        frame (get-options-frame {:width          (- width (* 2 params/block-padding))
                                  :height         (- height (* 2 params/block-padding))
                                  :options-number (count options)
                                  :layout         layout})]
    (options-list/create options form-data (merge props {:frame frame}))))

(defn create
  [{:keys [alias layout options task-type] :as form-data}
   {:keys [object-name question-id visible? task-image-param-name] :as props}]
  (let [{options-label :label} options

        show-task-image? (= task-type "text-image")

        substrate-name (str object-name "-substrate")
        background-name (str object-name "-background")
        options-name (str object-name "-options")
        task-text-group-name (str object-name "-task-text-group")
        task-image-object-name (param-name->object-name task-image-param-name question-id)

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
                                                                 show-task-image? (conj task-image-object-name))
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
                                                        :question-id question-id})))
            show-task-image? (merge-data (task-image/create form-data
                                                            (merge props
                                                                   (:image layout-coordinates)
                                                                   {:param-name  task-image-param-name
                                                                    :object-name task-image-object-name}))))))
