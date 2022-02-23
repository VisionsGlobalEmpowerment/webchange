(ns webchange.editor-v2.activity-dialogs.form.utils
  (:require
    [clojure.set :refer [difference]]
    [webchange.editor-v2.activity-dialogs.form.utils.get-phrases-sequence :refer [get-phrases-sequence]]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-empty-action get-inner-action skip-effects guide-effects]]
    [webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory :refer [get-node-data]]
    [webchange.utils.scene-data :refer [get-dialog-actions get-scene-object get-tracks]]))

(defn- get-action-type
  [{:keys [action-data available-effects-ids]}]
  (let [{:keys [id type phrase-text]} (get-inner-action action-data)]
    (cond
      (some #{id} available-effects-ids)
      :effect

      (= type "text-animation")
      :text-animation

      (some #{type} ["add-animation" "remove-animation"])
      :character-animation

      (some #{type} ["char-movement"])
      :character-movement

      (some? phrase-text)
      :phrase

      (some #{type} ["start-skip-region" "end-skip-region"])
      :skip

      (some #{type} ["mute-background-music" "unmute-background-music"])
      :background-music

      (some #{type} ["show-guide" "hide-guide" "highlight-guide"])
      :guide

      :else :unknown)))

(defn- set-common-action-data
  [actions {:keys [available-effects concept-data scene-data]}]
  (let [available-effects-ids (map :action available-effects)]
    (->> actions
         (map (fn [{:keys [scene-action-path concept-action-path parallel-mark]}]
                (let [concept-acton? (some? concept-action-path)
                      action-data (if concept-acton?
                                    (get-in (:data concept-data) concept-action-path)
                                    (get-in (:actions scene-data) scene-action-path))
                      {:keys [duration]} (get-empty-action action-data)]
                  (cond-> {:type          (get-action-type {:action-data           action-data
                                                            :available-effects-ids available-effects-ids})
                           :source        (if concept-acton? :concept :scene)
                           :action-data   action-data
                           :action-path   (cond-> {:scene (vec scene-action-path)}
                                                  concept-acton? (assoc :concept (vec concept-action-path)))

                           ;; backward compatibility:
                           :path          (if concept-acton? (vec concept-action-path) (vec scene-action-path))

                           :delay         duration
                           :parallel-mark parallel-mark}
                          concept-acton? (assoc :concept-name (:name concept-data)))))))))

(defn- get-chunked-text-data
  [text text-chunks action-chunks]
  (let [action-chunks-numbers (map :chunk action-chunks)]
    (map-indexed (fn [idx {:keys [start end]}]
                   {:text    (subs text start end)
                    :filled? (->> action-chunks-numbers
                                  (some #{idx})
                                  (boolean))})
                 text-chunks)))

(def data-getters
  {:background-music    (fn [{:keys [action-data]} {:keys [_]}]
                          (let [{:keys [type]} (get-inner-action action-data)]
                            {:effect-name (get-in skip-effects [(keyword type) :text])}))

   :character-animation (fn [{:keys [action-data]} {:keys [_]}]
                          (let [{:keys [id target track]} (get-inner-action action-data)]
                            {:animation-object target
                             :animation-name   id
                             :animation-track  track}))

   :character-movement  (fn [{:keys [action-data]} {:keys [_]}]
                          (let [{:keys [action target transition-id]} (get-inner-action action-data)]
                            {:action    action
                             :character transition-id
                             :target    target}))

   :effect              (fn [{:keys [action-data]} {:keys [available-effects]}]
                          (let [{:keys [id]} (get-inner-action action-data)]
                            {:effect      id
                             :effect-name (->> available-effects
                                               (some (fn [available-effect]
                                                       (and (= (:action available-effect) id)
                                                            available-effect)))
                                               (:name))}))

   :guide               (fn [{:keys [action-data]}]
                          (let [{:keys [type]} (get-inner-action action-data)]
                            {:effect-name (get-in guide-effects [(keyword type) :text])}))

   :phrase              (fn [{:keys [action-data]}]
                          (let [{:keys [target phrase-text phrase-placeholder]} (get-inner-action action-data)]
                            {:character   target
                             :text        phrase-text
                             :placeholder phrase-placeholder}))

   :skip                (fn [{:keys [action-data]} {:keys [_]}]
                          (let [{:keys [type]} (get-inner-action action-data)]
                            {:effect-name (get-in skip-effects [(keyword type) :text])}))

   :text-animation      (fn [{:keys [action-data]} {:keys [scene-data]}]
                          (let [{:keys [data target]} (get-inner-action action-data)]
                            {:text-object  target
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
                                                (:text))}))})

(defn- set-specific-action-data
  [actions params]
  (->> actions
       (map (fn [{:keys [type] :as data}]
              (let [specific-data-getter (get data-getters type (constantly {}))]
                (->> (specific-data-getter data params)
                     (merge data)))))))

(defn- set-selected
  [actions current-action-path]
  (map (fn [data]
         (->> (get data :path)
              (= current-action-path)
              (assoc data :selected?)))
       actions))

(defn prepare-phrase-actions
  [{:keys [dialog-action-path concept-data scene-data available-effects current-action-path]
    :or   {concept-data      nil
           available-effects []}}]
  (-> (get-phrases-sequence {:action-path  dialog-action-path
                             :scene-data   scene-data
                             :concept-data concept-data})
      (set-common-action-data {:concept-data      concept-data
                               :scene-data        scene-data
                               :available-effects available-effects})
      (set-specific-action-data {:concept-data      concept-data
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
