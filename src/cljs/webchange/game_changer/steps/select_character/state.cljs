(ns webchange.game-changer.steps.select-character.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(def available-skeletons ["senoravaca" "vera" "mari"])

(defn path-to-db
  [relative-path]
  (concat [:game-changer] relative-path))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-animation-skins {:on-success [::set-available-animation-skins]}]}))

;; Available Animation Skins

(def available-animation-skins-path (path-to-db [:available-animation-skins]))

(re-frame/reg-event-fx
  ::set-available-animation-skins
  (fn [{:keys [db]} [_ skins-data]]
    {:db (assoc-in db available-animation-skins-path skins-data)}))

(defn- get-available-animation-skins
  [db]
  (get-in db available-animation-skins-path []))

(re-frame/reg-sub
  ::available-animation-skins
  get-available-animation-skins)

;; Characters Groups

(re-frame/reg-sub
  ::available-characters-groups
  (fn []
    [(re-frame/subscribe [::available-animation-skins])])
  (fn [[available-animation-skins]]
    (->> available-animation-skins
         (filter (fn [{:keys [name]}]
                   (some #{name} available-skeletons)))
         (map (fn [{:keys [name preview]}]
                (cond-> {:name  name
                         :value name}
                        (some? preview) (assoc :img preview)))))))

;; Characters Group Skins

(re-frame/reg-sub
  ::available-characters-skins
  (fn []
    [(re-frame/subscribe [::available-animation-skins])])
  (fn [[available-animation-skins] [_ group-name]]
    (->> available-animation-skins
         (some (fn [{:keys [name skins]}]
                 (and (= name group-name)
                      skins)))
         (map (fn [{:keys [name preview]}]
                (cond-> {:name  name
                         :value name}
                        (some? preview) (assoc :img preview)))))))

;; Selected Characters

(defn- get-character-preview
  [db {:keys [skeleton skin]}]
  (->> (get-available-animation-skins db)
       (some (fn [{:keys [name skins]}]
               (and (= name skeleton) skins)))
       (some (fn [{:keys [name preview]}]
               (and (= name skin) preview)))))

(re-frame/reg-sub
  ::characters-skins
  (fn [db [_ characters-data]]
    (map (fn [data]
           (merge data
                  {:preview (get-character-preview db data)}))
         characters-data)))
