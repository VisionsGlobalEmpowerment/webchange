(ns webchange.question.common.task-image
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.get-question-data :refer [param-name->object-name]]))

(defn create
  [form-data
   {:keys [x y width height param-name object-name]}]
  (let [params (->> (keyword param-name)
                    (get form-data))

        image-x (+ x (/ width 2))
        image-y (+ y (/ height 2))
        image-width (- width (* common-params/block-padding 2))
        image-height (- height (* common-params/block-padding 2))]
    {:objects {(keyword object-name) (merge {:type      "image"
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
