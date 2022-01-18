(ns webchange.question.common.option-item
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.utils :refer [merge-data]]
    [webchange.question.utils :as utils]))

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
                                           :editable?     {:select true}
                                           :metadata      {:question-form-param image-param-name}}
                                          image-props)}
   :assets  [{:url  (:src image-props)
              :size 1
              :type "image"}]})

(defn- create-substrate
  [{:keys [object-name x y width height question-id value actions border-radius]
    :or   {border-radius 24}}]
  {:pre [(string? object-name) (string? question-id) (string? value)]}
  (let [action-activate-name (str object-name "-activate")
        action-inactivate-name (str object-name "-inactivate")
        states {:default {:border-color 0xFFFFFF
                          :fill         0xC4C4C4}
                :active  {:border-color 0xFFFFFF
                          :fill         0xFFFFFF}
                :correct {:border-color 0x56B624
                          :fill         0xFFFFFF}}]
    {:objects {(keyword object-name) (cond-> {:type          "rectangle"
                                              :x             x
                                              :y             y
                                              :width         width
                                              :height        height
                                              :border-radius border-radius
                                              :border-width  2
                                              :states        states}
                                             :always (merge (:default states))
                                             (some? actions) (assoc :actions actions))}
     :actions {(keyword action-activate-name)   {:type   "state"
                                                 :target object-name
                                                 :id     "active"
                                                 :tags   [(str "activate-option-" value "-" question-id)]}
               (keyword action-inactivate-name) {:type   "state"
                                                 :target object-name
                                                 :id     "default"
                                                 :tags   [(str "inactivate-options-" question-id)
                                                          (str "inactivate-option-" value "-" question-id)]}}}))

(defn- create-text
  [{:keys [text-name text-props text-param-name]}
   {:keys [x y width height text actions]}]
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

(defn- get-option-actions
  [{:keys [value]} data-names]
  {:click (cond-> {:type       "action"
                   :on         "click"
                   :id         (get-in data-names [:options :actions :click-handler])
                   :params     {:value value}
                   :unique-tag common-params/question-action-tag})})

(defn- create-image-with-text-options
  [{:keys [image-name value] :as option}
   {:keys [object-name x y width height question-id] :as props}
   data-names]
  (let [substrate-name (str object-name "-substrate")
        image-group-name (str image-name "-group")
        actions (get-option-actions option data-names)]
    (cond-> {:objects {(keyword object-name) {:type        "group"
                                              :object-name object-name
                                              :x           x
                                              :y           y
                                              :children    (cond-> [substrate-name]
                                                                   :always (conj image-name))}}}
            :always (merge-data (create-substrate {:object-name substrate-name
                                                   :x           0
                                                   :y           0
                                                   :width       width
                                                   :height      height
                                                   :question-id question-id
                                                   :value       value
                                                   :actions     actions}))
            :always (merge-data (create-image option
                                              {:object-name image-group-name
                                               :width       width
                                               :height      height
                                               :actions     actions})))))

(defn- create-text-option
  [{:keys [text-name value] :as option}
   {:keys [object-name x y width height question-id] :as props}
   data-names]
  (let [text-padding (get-in common-params/option [:padding :text])
        substrate-name (str object-name "-substrate")
        actions (get-option-actions option data-names)]
    (merge-data {:objects {(keyword object-name) {:type     "group"
                                                  :x        x
                                                  :y        y
                                                  :children [substrate-name text-name]}}}
                (create-substrate {:object-name substrate-name
                                   :x           0
                                   :y           0
                                   :width       width
                                   :height      height
                                   :question-id question-id
                                   :value       value
                                   :actions     actions})
                (create-text option
                             {:x       text-padding
                              :y       text-padding
                              :width   width
                              :height  height
                              :actions actions}))))

(defn- create-thumbs-option
  [{:keys [image-name value] :as option}
   {:keys [object-name x y width height question-id] :as props}
   data-names]
  (let [substrate-name (str object-name "-substrate")
        border-radius (/ width 2)
        image-group-name (str image-name "-group")
        actions (get-option-actions option data-names)]
    (merge-data {:objects {(keyword object-name) {:type     "group"
                                                  :x        x
                                                  :y        y
                                                  :children [substrate-name image-group-name]}}}
                (create-substrate {:object-name   substrate-name
                                   :x             0
                                   :y             0
                                   :width         width
                                   :height        height
                                   :question-id   question-id
                                   :value         value
                                   :actions       actions
                                   :border-radius (-> width (/ 2) utils/round)})
                (create-image option
                              {:object-name   image-group-name
                               :width         width
                               :height        height
                               :border-radius (- border-radius 10)
                               :actions       actions}))))

(defn create
  [option {:keys [question-type] :as props} data-names]
  (case question-type
    "multiple-choice-image" (create-image-with-text-options option props data-names)
    "multiple-choice-text" (create-text-option option props data-names)
    "thumbs-up-n-down" (create-thumbs-option option props data-names)))
