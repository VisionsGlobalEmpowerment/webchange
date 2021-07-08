(ns webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions
  (:require
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-empty-action get-inner-action]]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [prepare-nodes get-node-data]]
    [webchange.utils.scene-data :refer [get-scene-object]]))

(defn- set-parallel-marks
  [actions]
  (map-indexed (fn [idx {:keys [y] :as data}]
                 (let [next-action (nth actions (inc idx) nil)
                       next-y (get next-action :y 0)]
                   (->> (cond
                          (and (= y 0) (> next-y 0)) :start
                          (and (> y 0) (> next-y 0)) :middle
                          (and (> y 0) (= next-y 0)) :end
                          :else :none)
                        (assoc data :parallel-mark))))
               actions))

(defn- set-action-data
  [actions {:keys [concept-data scene-data]}]
  (map (fn [{:keys [action-path node-path concept parallel-mark]}]
         (let [concept-acton? (boolean concept)]
           {:parallel-mark  parallel-mark
            :concept-acton? concept-acton?
            :action-data    (if concept-acton?
                              (get-in (:data concept-data) action-path)
                              (get-in (:actions scene-data) action-path))
            :action-path    action-path
            :node-data      (get-node-data {:concept-node?   concept-acton?
                                            :current-concept concept-data
                                            :scene-data      scene-data
                                            :action-path     action-path
                                            :node-path       node-path})}))
       actions))

(defn set-action-type
  [actions {:keys [available-effects]}]
  {:post [(every? (fn [{:keys [type]}] (some #{type} [:effect :phrase :text-animation :unknown])) %)
          (every? (fn [{:keys [source]}] (some #{source} [:concept :scene])) %)]}
  (map (fn [{:keys [action-data concept-acton?] :as data}]
         (let [inner-action-id (-> action-data (get-inner-action) (get :id))
               inner-action-type (-> action-data (get-inner-action) (get :type))
               inner-action-phrase-text (-> action-data (get-inner-action) (get :phrase-text))
               action-type (cond
                             (some #{inner-action-id} available-effects) :effect
                             (= inner-action-type "text-animation") :text-animation
                             (some? inner-action-phrase-text) :phrase
                             :else :unknown)]
           (-> data
               (assoc :type action-type)
               (assoc :source (if concept-acton? :concept :scene)))))
       actions))

(defn- get-component-data
  [actions {:keys [concept-data scene-data]}]
  (map (fn [{:keys [type source action-data action-path node-data parallel-mark]}]
         (let [concept-name (:name concept-data)
               {:keys [duration]} (get-empty-action action-data)
               {:keys [id phrase-text phrase-placeholder target]} (get-inner-action action-data)]
           (cond-> {:type          type
                    :source        source
                    :delay         duration
                    :path          action-path
                    :node-data     node-data
                    :parallel-mark parallel-mark}
                   (= type :phrase) (merge {:character   target
                                            :text        phrase-text
                                            :placeholder phrase-placeholder})
                   (= type :text-animation) (merge {:text-object target
                                                    :text        (->> (keyword target)
                                                                      (get-scene-object scene-data)
                                                                      (:text))})
                   (= type :effect) (merge {:effect id})
                   (= source :concept) (merge {:concept-name concept-name}))))
       actions))

(defn- set-selected
  [actions current-action-path]
  (map (fn [{:keys [path] :as data}]
         (->> (= path current-action-path)
              (assoc data :selected?)))
       actions))

(defn prepare-phrase-actions
  [{:keys [dialog-action-path concept-data scene-data available-effects current-action-path]}]
  (-> (prepare-nodes scene-data concept-data dialog-action-path)
      (set-parallel-marks)
      (set-action-data {:concept-data concept-data
                        :scene-data   scene-data})
      (set-action-type {:available-effects available-effects})
      (get-component-data {:concept-data concept-data
                           :scene-data   scene-data})
      (set-selected current-action-path)))
