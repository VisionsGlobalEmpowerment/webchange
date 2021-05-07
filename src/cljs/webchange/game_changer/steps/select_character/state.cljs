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
