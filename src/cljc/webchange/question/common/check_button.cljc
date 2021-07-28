(ns webchange.question.common.check-button
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.utils :refer [merge-data]]))

(defn- create-background
  [{:keys [object-name size]}]
  {:objects {(keyword object-name) {:type          "rectangle"
                                    :x             0
                                    :y             0
                                    :width         size
                                    :height        size
                                    :border-radius (/ size 2)
                                    :border-width  2
                                    :border-color  0x000000
                                    :fill          0x8ccfb9}}})

(defn- create-icon
  [{:keys [object-name size]}]
  (let [icon-width 48
        icon-height 48
        scale-x 2
        scale-y 2]
    {:objects {(keyword object-name) {:type         "svg-path"
                                      :data         "M9 16.2L4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z"
                                      :x            (- (/ size 2) (/ icon-width 2))
                                      :y            (- (/ size 2) (/ icon-height 2))
                                      :width        (/ icon-width scale-x)
                                      :height       (/ icon-height scale-y)
                                      :scale-x      scale-x
                                      :scale-y      scale-y
                                      :stroke       0x000000
                                      :stroke-width 2}}}))

(defn create
  [{:keys [object-name x y on-click on-click-params]
    :or   {x 0
           y 0}}]
  (let [size common-params/check-button-size
        background-name (str object-name "-background")
        icon-name (str object-name "-icon")
        actions (cond-> {}
                        (some? on-click) (assoc :click (cond-> {:type "action"
                                                                :on   "click"
                                                                :id   on-click
                                                                :unique-tag "question-action"}
                                                               (some? on-click-params) (assoc :params on-click-params))))]
    (merge-data {:objects {(keyword object-name) {:type     "group"
                                                  :x        x
                                                  :y        y
                                                  :children [background-name icon-name]
                                                  :actions  actions}}}
                (create-background {:object-name background-name
                                    :size        size})
                (create-icon {:object-name icon-name
                              :size        size}))))
