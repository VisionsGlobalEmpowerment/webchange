(ns webchange.game-changer.steps.select-character.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.characters.library :refer [characters]]))

(defn path-to-db
  [relative-path]
  (concat [:game-changer] relative-path))

(def available-characters-path (path-to-db [:available-characters]))

(re-frame/reg-event-fx
  ::load-characters
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db available-characters-path characters)}))

(re-frame/reg-sub
  ::available-characters-groups
  (fn [db]
    (let [characters (get-in db available-characters-path)]
      (->> characters
           (filter (fn [{:keys [skins]}]
                     (-> skins empty? not)))
           (map (fn [{:keys [skeleton preview title]}]
                  {:name  title
                   :value skeleton
                   :img   preview}))))))

(re-frame/reg-sub
  ::available-skins
  (fn [db [_ current-skeleton]]
    (let [characters (get-in db available-characters-path)
          group (some (fn [{:keys [skeleton] :as group}]
                        (and (= skeleton current-skeleton)
                             group))
                      characters)]
      (->> (get group :skins [])
           (map (fn [{:keys [skin preview name]}]
                  {:name  name
                   :value skin
                   :img   preview}))))))

(defn- get-character-preview
  [{:keys [skeleton skin]} characters]
  (let [current-group (some (fn [group-data] (and (= (:skeleton group-data) skeleton) group-data)) characters)
        current-skin (some (fn [skin-data] (and (= (:skin skin-data) skin) skin-data)) (get current-group :skins []))]
    (:preview current-skin)))

(re-frame/reg-sub
  ::characters-skins
  (fn [db [_ characters-data]]
    (let [characters (get-in db available-characters-path)]
      (map (fn [data]
             (merge data
                    {:preview (get-character-preview data characters)}))
           characters-data))))
