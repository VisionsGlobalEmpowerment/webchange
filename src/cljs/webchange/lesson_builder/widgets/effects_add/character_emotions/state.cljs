(ns webchange.lesson-builder.widgets.effects-add.character-emotions.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.widgets.effects-add.state :as effects-state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(def path-to-db :lesson-builder/effects-emotions)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; characters

(re-frame/reg-sub
  ::character-options
  :<- [::state/activity-data]
  :<- [::effects-state/characters-with-animation-group :emotions]
  (fn [[activity-data characters]]
    (->> characters
         (map (fn [{:keys [object]}]
                (let [{:keys [scene-name]} (utils/get-scene-object activity-data object)]
                  {:text  (-> (or scene-name object)
                              (clojure.string/replace "-" " ")
                              (clojure.string/capitalize))
                   :value object})))
         (sort-by :text)
         (concat [{:text  "Select Character"
                   :value ""}]))))

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
  :<- [::effects-state/characters-with-animation-group :emotions]
  (fn [[current-character characters]]
    (if (some? current-character)
      (->> characters
           (some (fn [{:keys [object animations]}]
                   (and (= object current-character) animations)))
           (map (fn [emotion]
                  {:text  (action-utils/animation->display-name emotion)
                   :value emotion}))
           (sort-by :text))
      [])))
