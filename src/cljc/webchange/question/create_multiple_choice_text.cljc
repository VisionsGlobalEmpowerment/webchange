(ns webchange.question.create-multiple-choice-text
  (:require
    [webchange.question.common.check-button :as check-button]
    [webchange.question.common.layout-markup :refer [get-layout-coordinates]]
    [webchange.question.common.options-list :as options-list]
    [webchange.question.common.substrate :as substrate]
    [webchange.question.common.task-image :as task-image]
    [webchange.question.common.task-text :as task-text]
    [webchange.question.common.params :as params]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [merge-data task-has-image? task-has-text?]]))

(defn- create-options
  [{:keys [options-number] :as form-data}
   layout
   {:keys [text-objects-names] :as props}
   data-names]
  (let [options (->> (range options-number)
                     (map inc)
                     (map (fn [option-idx]
                            (let [option-name (str "option-" option-idx)
                                  value-prop-name (str option-name "-value")
                                  text-prop-name (str option-name "-text")]
                              {:value           (->> value-prop-name keyword (get form-data))
                               :text-name       (get text-objects-names option-idx)
                               :text-props      (->> text-prop-name keyword (get form-data))
                               :text-param-name text-prop-name}))))]
    (options-list/create options form-data props layout data-names)))

(defn create
  [{:keys [alias options] :as form-data}
   data-names
   {:keys [object-name question-id visible? task-image-param-name text-objects-names] :as props}]
  (let [{options :data options-label :label} options
        substrate-name (str object-name "-substrate")
        options-name (str object-name "-options")
        task-text-group-name (str object-name "-task-text-group")

        task-image-object-name (param-name->object-name task-image-param-name question-id)
        task-image-container-name (str task-image-object-name "-container")

        has-image? (task-has-image? form-data)
        has-text? (task-has-text? form-data)

        layout (get-layout-coordinates form-data)]
    (cond-> {:objects {(keyword object-name) {:type      "group"
                                              :alias     alias
                                              :x         (:x params/template-size)
                                              :y         (:y params/template-size)
                                              :children  (cond-> [substrate-name
                                                                  options-name
                                                                  (get-in data-names [:check-button :objects :main])]
                                                                 has-text? (conj task-text-group-name)
                                                                 has-image? (conj task-image-container-name))
                                              :visible   visible?
                                              :editable? {:show-in-tree? true}}}}
            :always (merge-data (substrate/create {:object-name substrate-name}))
            :always (merge-data (create-options form-data
                                                layout
                                                (merge props
                                                       {:object-name        options-name
                                                        :text-objects-names text-objects-names
                                                        :label-type         options-label
                                                        :options            options})
                                                data-names))
            :always (merge-data (check-button/create data-names layout))
            has-text? (merge-data (task-text/create form-data
                                                    layout
                                                    (merge props
                                                           {:object-name task-text-group-name
                                                            :param-name  "task-text"
                                                            :question-id question-id})))
            has-image? (merge-data (task-image/create form-data
                                                      layout
                                                      (merge props
                                                             {:param-name     task-image-param-name
                                                              :container-name task-image-container-name
                                                              :image-name     task-image-object-name}))))))
