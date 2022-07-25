(ns webchange.lesson-builder.tools.effects-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.tools.effects-add.character-movements.utils :refer [get-characters-with-movements]]
    [webchange.utils.scene-data :as utils]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :lesson-builder/effects)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; available animations

(def animations-loading-key :animations-loading?)

(defn- set-animations-loading
  [db value]
  (assoc db animations-loading-key value))

(re-frame/reg-sub
  ::animations-loading?
  :<- [path-to-db]
  #(get % animations-loading-key true))

(def animations-key :animations)

(defn- get-animations
  [db]
  (get db animations-key))

(defn- set-animations
  [db value]
  (assoc db animations-key value))

(re-frame/reg-sub
  ::animations
  :<- [path-to-db]
  get-animations)

(re-frame/reg-event-fx
  ::load-animations
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-animations-loading db true)
     :dispatch [::warehouse/load-animation-skins
                {:on-success [::load-animation-skins-success]
                 :on-failure [::load-animation-skins-failure]}]}))

(re-frame/reg-event-fx
  ::load-animation-skins-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ animations]]
    {:db (-> db
             (set-animations-loading false)
             (set-animations animations))}))

(re-frame/reg-event-fx
  ::load-animation-skins-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (set-animations-loading false))}))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [animations (get-animations db)]
      (if (nil? animations)
        {:dispatch [::load-animations]}
        {:db (set-animations-loading db false)}))))

(re-frame/reg-sub
  ::activity-characters
  :<- [::state/activity-data]
  (fn [activity-data]
    (->> (utils/get-scene-objects activity-data)
         (filter (fn [[_ {:keys [type]}]]
                   (= type "animation"))))))

(defn- get-character-animations-group
  [character-name animations animations-group]
  (let [character-animations (some (fn [{:keys [name animation-groups]}]
                                     (and (= name character-name) animation-groups))
                                   animations)]
    (get character-animations animations-group [])))

(re-frame/reg-sub
  ::characters-with-animation-group
  :<- [::activity-characters]
  :<- [::animations]
  (fn [[activity-characters animations] [_ animations-group]]
    (->> activity-characters
         (map (fn [[object-name {:keys [name]}]]
                {:object     (clojure.core/name object-name)
                 :character  name
                 :animations (get-character-animations-group name animations animations-group)}))
         (filter (fn [{:keys [animations]}]
                   (-> animations empty? not))))))

(re-frame/reg-sub
  ::activity-effects
  :<- [::state/activity-data]
  (fn [activity-data]
    (->> (utils/get-available-effects activity-data)
         (filter (fn [{:keys [type]}]
                   (not= type "question"))))))

(re-frame/reg-sub
  ::available-effects
  :<- [::state/activity-data]
  :<- [::activity-characters]
  :<- [::animations]
  :<- [::activity-effects]
  :<- [::characters-with-animation-group :emotions]
  (fn [[activity-data characters animations activity-effects characters-with-emotions]]
    (let [characters-with-movements (get-characters-with-movements {:activity-data activity-data
                                                                    :characters    characters
                                                                    :animations    animations})]
      ;; ToDo: add selected action effects: webchange/editor_v2/activity_dialogs/menu/state.cljs:57
      (cond-> []
              (-> activity-effects empty? not)
              (conj {:title   "Template effect"
                     :effects (map (fn [{:keys [action name]}]
                                     {:text      name
                                      :action-id action})
                                   activity-effects)})

              (-> characters-with-emotions empty? not)
              (conj {:title     "Character emotions"
                     :component "character-emotions"})

              (-> characters-with-movements empty? not)
              (conj {:title     "Character movements"
                     :component "character-movements"})

              :always
              (concat [{:title   "Guide effect"
                        :effects [{:text        "Hide guide"
                                   :action-type "hide-guide"}
                                  {:text        "Show guide"
                                   :action-type "show-guide"}
                                  {:text        "Highlight guide"
                                   :action-type "highlight-guide"}]}
                       {:title   "Skip activity"
                        :effects [{:text        "Start skip"
                                   :action-type "start-skip-region"}
                                  {:text        "End skip"
                                   :action-type "end-skip-region"}]}
                       {:title   "Sound effect"
                        :effects [{:text        "Mute background music"
                                   :action-type "mute-background-music"}
                                  {:text        "Unute background music"
                                   :action-type "unmute-background-music"}]}])))))
