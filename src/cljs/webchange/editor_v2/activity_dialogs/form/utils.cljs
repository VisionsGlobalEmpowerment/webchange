(ns webchange.editor-v2.activity-dialogs.form.utils
  (:require
    [clojure.set :refer [difference]]
    [webchange.editor-v2.activity-dialogs.form.utils.get-phrases-sequence :refer [get-phrases-sequence]]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-empty-action get-inner-action skip-effects guide-effects]]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [get-node-data]]
    [webchange.utils.scene-data :refer [get-dialog-actions get-scene-object get-tracks]]))

(defn- set-action-data
  [actions {:keys [concept-data scene-data]}]
  (map (fn [{:keys [scene-action-path concept-action-path parallel-mark]}]
         (let [concept-acton? (some? concept-action-path)]
           {:parallel-mark  parallel-mark
            :concept-acton? concept-acton?
            :action-data    (if concept-acton?
                              (get-in (:data concept-data) concept-action-path)
                              (get-in (:actions scene-data) scene-action-path))
            :action-path    (if concept-acton?
                              concept-action-path
                              scene-action-path)
            :node-data      (get-node-data {:concept-node?   concept-acton?
                                            :current-concept concept-data
                                            :scene-data      scene-data
                                            :action-path     (if concept-acton?
                                                               concept-action-path
                                                               scene-action-path)
                                            :node-path       scene-action-path})}))
       actions))

(defn set-action-type
  [actions {:keys [available-effects]}]
  {:post [(every? (fn [{:keys [type]}]
                    (some #{type} [:background-music
                                   :character-animation
                                   :character-movement
                                   :effect
                                   :phrase
                                   :skip
                                   :guide
                                   :text-animation
                                   :unknown])) %)
          (every? (fn [{:keys [source]}]
                    (some #{source} [:concept :scene])) %)]}
  (let [available-effects-ids (map :action available-effects)]
    (map (fn [{:keys [action-data concept-acton?] :as data}]
           (let [inner-action-id (-> action-data (get-inner-action) (get :id))
                 inner-action-type (-> action-data (get-inner-action) (get :type))
                 inner-action-phrase-text (-> action-data (get-inner-action) (get :phrase-text))
                 action-type (cond
                               (some #{inner-action-id} available-effects-ids)
                               :effect

                               (= inner-action-type "text-animation")
                               :text-animation

                               (some #{inner-action-type} ["add-animation" "remove-animation"])
                               :character-animation

                               (some #{inner-action-type} ["char-movement"])
                               :character-movement

                               (some? inner-action-phrase-text)
                               :phrase

                               (some #{inner-action-type} ["start-skip-region" "end-skip-region"])
                               :skip

                               (some #{inner-action-type} ["mute-background-music" "unmute-background-music"])
                               :background-music

                               (some #{inner-action-type} ["show-guide" "hide-guide" "highlight-guide"])
                               :guide

                               :else :unknown)]
             (-> data
                 (assoc :type action-type)
                 (assoc :source (if concept-acton? :concept :scene)))))
         actions)))

(defn- get-chunked-text-data
  [text text-chunks action-chunks]
  (let [action-chunks-numbers (map :chunk action-chunks)]
    (map-indexed (fn [idx {:keys [start end]}]
                   {:text    (subs text start end)
                    :filled? (->> action-chunks-numbers
                                  (some #{idx})
                                  (boolean))})
                 text-chunks)))

(defn- get-component-data
  [actions {:keys [available-effects concept-data scene-data]}]
  (map (fn [{:keys [type source action-data action-path node-data parallel-mark]}]
         (let [concept-name (:name concept-data)
               {:keys [duration]} (get-empty-action action-data)
               {action-type :type
                :keys       [action
                             data
                             id
                             phrase-text
                             phrase-placeholder
                             target
                             track
                             transition-id]} (get-inner-action action-data)]
           (cond-> {:type          type
                    :source        source
                    :delay         duration
                    :path          action-path
                    :node-data     node-data
                    :parallel-mark parallel-mark}
                   (= type :phrase)
                   (merge {:character   target
                           :text        phrase-text
                           :placeholder phrase-placeholder})

                   (= type :text-animation)
                   (merge {:text-object  target
                           :chunked-text (get-chunked-text-data
                                           (->> (keyword target)
                                                (get-scene-object scene-data)
                                                (:text))
                                           (->> (keyword target)
                                                (get-scene-object scene-data)
                                                (:chunks))
                                           data)
                           :text         (->> (keyword target)
                                              (get-scene-object scene-data)
                                              (:text))})

                   (= type :character-animation)
                   (merge {:animation-object target
                           :animation-name   id
                           :animation-track  track})

                   (= type :character-movement)
                   (merge {:action    action
                           :character transition-id
                           :target    target})

                   (= type :effect)
                   (merge {:effect      id
                           :effect-name (->> available-effects
                                             (some (fn [available-effect]
                                                     (and (= (:action available-effect) id)
                                                          available-effect)))
                                             (:name))})

                   (= type :skip)
                   (merge {:effect-name (get-in skip-effects [(keyword action-type) :text])})

                   (= type :background-music)
                   (merge {:action action-type})

                   (= type :guide)
                   (merge {:effect-name (get-in guide-effects [(keyword action-type) :text])})

                   (= source :concept)
                   (merge {:concept-name concept-name}))))
       actions))

(defn- set-selected
  [actions current-action-path]
  (map (fn [{:keys [path] :as data}]
         (->> (= path current-action-path)
              (assoc data :selected?)))
       actions))

(defn- ->log
  [data message]
  (print message data)
  data)

(defn prepare-phrase-actions
  [{:keys [dialog-action-path concept-data scene-data available-effects current-action-path]
    :or   {concept-data      nil
           available-effects []}}]
  (-> (get-phrases-sequence {:action-path  dialog-action-path
                             :scene-data   scene-data
                             :concept-data concept-data})
      (set-action-data {:concept-data concept-data
                        :scene-data   scene-data})
      (set-action-type {:available-effects available-effects})
      (get-component-data {:concept-data      concept-data
                           :scene-data        scene-data
                           :available-effects available-effects})
      (set-selected current-action-path)))

(defn collect-untracked-actions
  [scene-data]
  (let [all-actions (->> (get-dialog-actions scene-data)
                         (map clojure.core/name))
        tracked-actions (->> (get-tracks scene-data)
                             (map :nodes)
                             (flatten)
                             (filter #(= (:type %) "dialog"))
                             (map :action-id))]
    (->> (difference (set all-actions)
                     (set tracked-actions))
         (vec))))
