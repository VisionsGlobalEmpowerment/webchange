(ns webchange.question.common.check-button
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.utils :refer [merge-data round]]))

(defn- create-background
  [{:keys [object-name width height]}]
  {:objects {(keyword object-name) {:type          "rectangle"
                                    :x             0
                                    :y             0
                                    :width         width
                                    :height        height
                                    :border-width  6
                                    :border-color  0x56B624
                                    :border-radius (-> width (/ 2) round)
                                    :fill          0xFFFFFF
                                    :states        {:active   {:fill 0x56B624}
                                                    :inactive {:fill 0xFFFFFF}}}}})

(defn- create-icon
  [{:keys [object-name width height]}]
  (let [stroke-width 12
        icon-width 60
        icon-height 41
        scale-x 1
        scale-y 1]
    {:objects {(keyword object-name) {:type         "svg-path"
                                      :data         "M 6 17 l 17 18 m 0 0 l 31 -29"
                                      :x            (-> (- (/ width 2)
                                                           (/ icon-width 2))
                                                        (round))
                                      :y            (-> (- (/ height 2)
                                                           (/ icon-height 2))
                                                        (round))
                                      :width        width
                                      :height       height
                                      :scale-x      scale-x
                                      :scale-y      scale-y
                                      :stroke       "#56B624"
                                      :stroke-width stroke-width
                                      :states       {:invisible {:visible false}
                                                     :inactive  {:stroke  "#56B624"
                                                                 :visible true}
                                                     :active    {:stroke  "#FFFFFF"
                                                                 :visible true}}}}}))

(defn create
  ([data-names layout]
   (create data-names layout {}))
  ([data-names layout
    {:keys [show-check-button?]
     :or   {show-check-button? false}}]
   (let [{:keys [x y width height]} (get layout :check-button)
         {:keys [objects actions]} (get data-names :check-button)

         background-name (-> objects :main (str "-background"))
         icon-name (-> objects :main (str "-icon"))]
     (merge-data {:objects {(-> objects :main keyword) {:type     "group"
                                                        :x        x
                                                        :y        y
                                                        :children [background-name icon-name]
                                                        :actions  (cond-> {:click {:type       "action"
                                                                                   :on         "click"
                                                                                   :id         (:on-click actions)
                                                                                   :unique-tag common-params/question-action-tag}})
                                                        :visible  show-check-button?
                                                        :states   {:visible   {:visible true}
                                                                   :invisible {:visible false}}}}
                  :actions {(-> actions :set-default keyword)   {:type "parallel"
                                                                 :data [{:type "state" :id "invisible" :target (-> objects :main)}]}
                            (-> actions :set-touched keyword)   {:type "parallel"
                                                                 :data [{:type "state" :id "visible" :target (-> objects :main)}
                                                                        {:type "state" :id "inactive" :target background-name}
                                                                        {:type "state" :id "invisible" :target icon-name}]}
                            (-> actions :set-ready keyword)     {:type "parallel"
                                                                 :data [{:type "state" :id "visible" :target (-> objects :main)}
                                                                        {:type "state" :id "inactive" :target background-name}
                                                                        {:type "state" :id "inactive" :target icon-name}]}

                            (-> actions :set-submitted keyword) {:type "parallel"
                                                                 :data [{:type "state" :id "visible" :target (-> objects :main)}
                                                                        {:type "state" :id "active" :target background-name}
                                                                        {:type "state" :id "active" :target icon-name}]}}}
                 (create-background {:object-name background-name
                                     :width       width
                                     :height      height})
                 (create-icon {:object-name icon-name
                               :width       width
                               :height      height})))))
