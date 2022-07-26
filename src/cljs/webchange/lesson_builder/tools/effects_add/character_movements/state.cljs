(ns webchange.lesson-builder.tools.effects-add.character-movements.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.effects-add.character-movements.utils :refer [get-characters-with-movements]]
    [webchange.lesson-builder.tools.effects-add.state :as effects-state]
    [webchange.utils.animations :as animation-utils]
    [webchange.utils.scene-data :as utils]))

(def path-to-db :lesson-builder/effects-movements)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))


;; targets

(def current-target-key :current-target)

(re-frame/reg-sub
  ::current-target
  :<- [path-to-db]
  #(get % current-target-key nil))

(defn- set-current-target
  [db value]
  (assoc db current-target-key value))

(re-frame/reg-event-fx
  ::set-current-target
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-current-target db value)}))

(re-frame/reg-sub
  ::target-options
  :<- [::current-character]
  :<- [::current-action]
  :<- [::state/activity-data]
  :<- [::effects-state/animations]
  (fn [[current-character current-action activity-data animations]]
    (if (and (some? current-character)
             (some? current-action))
      (let [{:keys [target-type target-required-animations]} (get animation-utils/action-animations current-action)]
        (->> (utils/get-scene-objects activity-data)
             (filter (fn [[_ target-data]]
                       (let [proper-type? (if (some? target-type)
                                            (some #{(:type target-data)} target-type)
                                            true)
                             has-required-animations? (if (some? target-required-animations)
                                                        (let [target-animations (some (fn [{:keys [animations name]}]
                                                                                        (and (= name (:name target-data)) animations))
                                                                                      animations)]
                                                          (clojure.set/subset? (set target-required-animations)
                                                                               (set target-animations)))
                                                        true)]
                         (and proper-type? has-required-animations?))))
             (map (fn [[object-name]]
                    (let [object-name (clojure.core/name object-name)]
                      {:text  (animation-utils/object->display-name activity-data object-name)
                       :value object-name})))
             (sort-by :text)))
      [])))

;; actions

(def current-action-key :current-action)

(re-frame/reg-sub
  ::current-action
  :<- [path-to-db]
  #(get % current-action-key nil))

(defn- set-current-action
  [db value]
  (assoc db current-action-key value))

(re-frame/reg-event-fx
  ::set-current-action
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (set-current-action value)
             (set-current-target nil))}))

(re-frame/reg-sub
  ::action-options
  :<- [::current-character]
  :<- [::effects-state/activity-characters]
  :<- [::effects-state/animations]
  (fn [[current-character activity-characters animations]]
    (if (some? current-character)
      (let [character-name (some (fn [[object-name {:keys [name]}]]
                                   (and (= current-character (clojure.core/name object-name)) name))
                                 activity-characters)
            character-animations (some (fn [{:keys [animations name]}]
                                         (and (= name character-name) animations))
                                       animations)]
        (->> animation-utils/action-animations
             (filter (fn [[_ {:keys [required-animations]}]]
                       (clojure.set/subset? (set required-animations)
                                            (set character-animations))))
             (map (fn [[action-name {:keys [title]}]]
                    {:text  title
                     :value action-name}))
             (sort-by :text)))
      [])))

;; characters

(re-frame/reg-sub
  ::character-options
  :<- [::state/activity-data]
  :<- [::effects-state/activity-characters]
  :<- [::effects-state/animations]
  (fn [[activity-data characters animations]]
    (get-characters-with-movements {:activity-data activity-data
                                    :characters    characters
                                    :animations    animations})))

(def current-character-key :current-character)

(re-frame/reg-sub
  ::current-character
  :<- [path-to-db]
  #(get % current-character-key nil))

(defn- set-current-character
  [db value]
  (assoc db current-character-key value))

(re-frame/reg-event-fx
  ::set-current-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (set-current-character value)
             (set-current-action nil)
             (set-current-target nil))}))

;; add action data

(re-frame/reg-sub
  ::add-action-data
  :<- [::state/activity-data]
  :<- [::current-character]
  :<- [::current-action]
  :<- [::current-target]
  (fn [[activity-data character action target]]
    (when (every? some? [character action target])
      {:title (animation-utils/action->display-name {:activity-data activity-data
                                                     :character     character
                                                     :target        target
                                                     :action        action})
       :data  {:character character
               :movement  action
               :target    target}})))
