(ns webchange.question.common.task-image
  (:require
    [webchange.question.common.params :as common-params]))

(defn create
  [{:keys [x y width height object-name]}
   {:keys [task]}]
  (let [{task-image :img} task
        image-x (+ x (/ width 2))
        image-y (+ y (/ height 2))
        image-width (- width (* common-params/block-padding 2))
        image-height (- height (* common-params/block-padding 2))]
    {:objects {(keyword object-name) {:type       "image"
                                      :src        task-image
                                      :x          image-x
                                      :y          image-y
                                      :max-width  image-width
                                      :max-height image-height
                                      :origin     {:type "center-center"}
                                      :editable?  {:select true}}}}))
