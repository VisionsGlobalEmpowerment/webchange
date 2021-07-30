(ns webchange.question.common.background
  (:require
    [webchange.question.common.params :as common-params]))

(defn create
  [{:keys [object-name x y width height color]
    :or   {x      0
           y      0
           width  (:width common-params/template-size)
           height (:height common-params/template-size)
           color  common-params/background-color}}]
  {:pre [(string? object-name)]}
  {:objects {(keyword object-name) {:type        "rectangle"
                                    :object-name object-name
                                    :x           x
                                    :y           y
                                    :width       width
                                    :height      height
                                    :fill        color}}})
