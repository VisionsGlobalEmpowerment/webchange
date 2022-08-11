(ns webchange.question.common.option-item
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.utils :refer [merge-data round]]
    [webchange.question.utils :as utils]))

(defn- create-substrate
  [{:keys [object-name x y width height actions border-radius use-state]
    :or   {x             0
           y             0
           border-radius 24}}]
  (let [states {:default   {:border-color 0xFFFFFF
                            :border-width 2
                            :fill         0xC4C4C4}
                :selected  {:border-color 0xFFFFFF
                            :border-width 2
                            :fill         0xFFFFFF}
                :correct   {:border-color 0x56B624
                            :border-width 6
                            :fill         0xFFFFFF}
                :incorrect {:border-color 0xFFFFFF
                            :border-width 2
                            :fill         0xFFFFFF}}]
    {:objects {(keyword object-name) (merge {:type          "rectangle"
                                             :x             x
                                             :y             y
                                             :width         width
                                             :height        height
                                             :border-radius border-radius
                                             :states        states
                                             :actions       actions}
                                            (if (some? use-state)
                                              (get states use-state)
                                              (:default states)))}}))

(defn- create-image
  [{:keys [image-name image-props image-param-name]}
   {:keys [object-name x y width height border-radius]
    :or   {x             0
           y             0
           border-radius 0}}]
  {:objects {(keyword object-name) {:type        "group"
                                    :object-name object-name
                                    :x           x
                                    :y           y
                                    :children    [image-name]}
             (keyword image-name)  (merge {:type          "image"
                                           :x             (/ width 2)
                                           :y             (/ height 2)
                                           :width         width
                                           :height        height
                                           :border-radius border-radius
                                           :origin        {:type "center-center"}
                                           :metadata      {:question-form-param image-param-name}}
                                          image-props)}
   :assets  [{:url  (:src image-props)
              :size 1
              :type "image"}]})

(defn- create-text
  [{:keys [text-name text-props text-param-name]}
   {:keys [x y width height text actions]
    :or   {x 0
           y 0}}]
  {:objects {(keyword text-name) (cond-> (merge {:type           "text"
                                                 :text           text
                                                 :x              x
                                                 :y              y
                                                 :width          width
                                                 :height         height
                                                 :word-wrap      true
                                                 :font-family    common-params/font-family
                                                 :font-size      48
                                                 :vertical-align "middle"
                                                 :editable?      {:select true}
                                                 :metadata       {:question-form-param text-param-name}}
                                                text-props)
                                         (some? actions) (assoc :actions actions))}})

(defn- create-wrong-mark
  [{:keys [object-name x y width height]}]
  (let [stroke-width 6]
    {:objects {(keyword object-name) {:type         "svg-path"
                                      :data         (str "M " stroke-width " " stroke-width " "
                                                         "L " (- width stroke-width) " " (- height stroke-width) " "
                                                         "M " stroke-width " " (- height stroke-width) " "
                                                         "L " (- width stroke-width) " " stroke-width)
                                      :x            x
                                      :y            y
                                      :stroke       "#FF0000"
                                      :stroke-width (* stroke-width 2)
                                      :visible      false
                                      :states       {:visible   {:visible true}
                                                     :invisible {:visible false}}}}}))

(defn- apply-padding
  [dimensions {:keys [h v]}]
  (-> dimensions
      (update :x + h)
      (update :y + v)
      (update :width - (* h 2))
      (update :height - (* v 2))))

(defn- get-content-dimensions
  [props padding]
  (-> (merge {:x 0 :y 0}
             (select-keys props [:width :height]))
      (apply-padding padding)))

(defn create
  [{:keys [image-name text-name value] :as option}
   {:keys [object-name x y width height question-id question-type correct-option?] :as props}
   data-names
   {:keys [highlight-correct-options?]
    :or   {highlight-correct-options? false}}]
  (let [image-option? (or (= question-type "multiple-choice-image")
                          (= question-type "thumbs-up-n-down"))
        text-option? (= question-type "multiple-choice-text")
        mark-option? (= question-type "thumbs-up-n-down")

        substrate-name (str object-name "-substrate")
        image-name (str object-name "-" image-name "-group")
        wrong-mark-name (str object-name "-wrong-mark")

        get-action-name (fn [action-name]
                          (-> object-name (str "-" action-name) (keyword)))

        item-actions {:click (cond-> {:type       "action"
                                      :on         "click"
                                      :id         (get-in data-names [:options :actions :click-handler])
                                      :params     {:value value}
                                      :unique-tag common-params/question-action-tag})}]
    (cond-> (merge-data {:objects {(keyword object-name) {:type       "group"
                                                          :x          x
                                                          :y          y
                                                          :pivot      {:x 0.5 :y 0.5}
                                                          :transition object-name
                                                          :editable?  {:select true}
                                                          :children   (cond-> [substrate-name]
                                                                              image-option? (conj image-name)
                                                                              text-option? (conj text-name)
                                                                              :always (conj wrong-mark-name))}}
                         :actions {(get-action-name "set-selected")   {:type "sequence-data"
                                                                       :tags [(utils/get-option-tag :set-selected {:option-value value :question-id question-id})]
                                                                       :data [{:type "state" :id "selected" :target substrate-name}]}
                                   (get-action-name "set-unselected") {:type "sequence-data"
                                                                       :tags [(utils/get-option-tag :set-unselected {:option-value value :question-id question-id})
                                                                              (utils/get-option-tag :set-unselected-all {:question-id question-id})]
                                                                       :data [{:type "state" :id "default" :target substrate-name}
                                                                              {:type "state" :id "invisible" :target wrong-mark-name}]}
                                   (get-action-name "set-correct")    {:type "sequence-data"
                                                                       :tags [(utils/get-option-tag :set-correct {:option-value value :question-id question-id})]
                                                                       :data [{:type "state" :id "correct" :target substrate-name}
                                                                              {:type "state" :id "invisible" :target wrong-mark-name}]}
                                   (get-action-name "set-incorrect")  {:type "sequence-data"
                                                                       :tags [(utils/get-option-tag :set-incorrect {:option-value value :question-id question-id})]
                                                                       :data [{:type "state" :id "incorrect" :target substrate-name}
                                                                              {:type "state" :id "visible" :target wrong-mark-name}
                                                                              {:type "action" :id (get-action-name "shake")}]}
                                   (get-action-name "shake")          (let [duration 0.15
                                                                            angle 10
                                                                            repeat 1]
                                                                        {:type "sequence-data"
                                                                         :data (->> [{:to {:rotation angle
                                                                                           :duration duration}}
                                                                                     {:to {:rotation (- angle)
                                                                                           :duration (* duration 2)
                                                                                           :yoyo     true
                                                                                           :repeat   repeat}}
                                                                                     {:to {:rotation 0
                                                                                           :duration duration}}]
                                                                                    (map #(merge % {:type          "transition"
                                                                                                    :transition-id object-name})))})}}
                        (create-substrate (cond-> {:object-name substrate-name
                                                   :width       width
                                                   :height      height
                                                   :actions     item-actions}
                                                  mark-option? (assoc :border-radius (-> width (/ 2) utils/round))
                                                  (and correct-option?
                                                       highlight-correct-options?) (assoc :use-state :correct)))
                        (create-wrong-mark (let [size 50]
                                             (cond-> {:object-name wrong-mark-name
                                                      :width       size
                                                      :height      size}
                                                     image-option? (merge {:x (-> (/ width 2)
                                                                                  (- (/ size 2))
                                                                                  (round))
                                                                           :y 20})
                                                     text-option? (merge {:x (- width size 20)
                                                                          :y 20})))))
            image-option? (merge-data (create-image option
                                                    (merge {:object-name image-name
                                                            :actions     item-actions}
                                                           (get-content-dimensions props {:h 10 :v 10}))))

            text-option? (merge-data (create-text option
                                                  (merge {:object-name text-name
                                                          :actions     item-actions}
                                                         (get-content-dimensions props {:h 40 :v 16})))))))
