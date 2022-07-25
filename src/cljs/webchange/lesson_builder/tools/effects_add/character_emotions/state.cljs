(ns webchange.lesson-builder.tools.effects-add.character-emotions.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.effects-add.character-emotions.utils :as utils]
    [webchange.lesson-builder.tools.effects-add.state :as effects-state]))

(def path-to-db :lesson-builder/effects-emotions)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; characters

(re-frame/reg-sub
  ::character-options
  :<- [::state/activity-data]
  :<- [::effects-state/activity-characters]
  :<- [::effects-state/animations]
  (fn [[activity-data characters animations]]
    (utils/get-characters-with-emotions {:activity-data activity-data
                                         :characters    characters
                                         :animations    animations})))

(def current-character-key :current-character)

(defn- get-current-character
  [db]
  (get db current-character-key))

(re-frame/reg-sub
  ::current-character
  :<- [path-to-db]
  get-current-character)

(re-frame/reg-event-fx
  ::set-current-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db current-character-key value)}))

;; emotions

(re-frame/reg-sub
  ::emotion-options
  :<- [::current-character]
  :<- [::effects-state/activity-characters]
  :<- [::effects-state/animations]
  (fn [[current-character characters animations]]
    (if (some? current-character)
      (utils/get-character-emotions {:character  current-character
                                     :characters characters
                                     :animations animations})
      [])))
