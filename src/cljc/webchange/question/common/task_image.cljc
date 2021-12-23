(ns webchange.question.common.task-image
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.get-question-data :refer [param-name->object-name]]))

(defn create
  [form-data
   {:keys [x y width height question-id param-name]}]
  (let [params (->> (keyword param-name)
                    (get form-data))

        image-object-name (param-name->object-name param-name question-id)

        image-x (+ x (/ width 2))
        image-y (+ y (/ height 2))
        image-width (- width (* common-params/block-padding 2))
        image-height (- height (* common-params/block-padding 2))]
    {:objects {(keyword image-object-name) (merge {:type      "image"
                                                   :x         image-x
                                                   :y         image-y
                                                   :width     image-width
                                                   :height    image-height
                                                   :origin    {:type "center-center"}
                                                   :editable? {:select true}}
                                                  params)}
     :assets  [{:url  (:src params)
                :size 1
                :type "image"}]}))
