(ns webchange.question.create-thumbs-up-n-down
  (:require
    [webchange.question.common.background :as background]
    [webchange.question.common.layout-markup :refer [get-layout-coordinates]]
    [webchange.question.common.options-list :as options-list]
    [webchange.question.common.substrate :as substrate]
    [webchange.question.common.task-image :as task-image]
    [webchange.question.common.task-text :as task-text]
    [webchange.question.common.params :as params]
    [webchange.question.utils :refer [merge-data]]))

(defn- get-options-frame
  [{:keys [width options-number]}]
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
                                              :x         (:x params/template-size)
                                              :y         (:y params/template-size)
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