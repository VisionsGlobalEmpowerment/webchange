(ns webchange.templates.library.flipbook.display-names
  (:require
    [webchange.utils.flipbook :as flipbook]
    [webchange.utils.scene-action-data :refer [get-inner-action text-animation-action?]]
    [webchange.utils.scene-data :refer [get-action]]))

(defn get-dialog-display-name
  [page-idx]
  (cond
    (or (= page-idx 0)
        (= page-idx :cover)) "Cover Page"
    (or (= page-idx 2)
        (= page-idx :authors)) "Authors Page"
    (= page-idx :cover-back) "Back Cover Page"
    :default (str "Page " page-idx)))

(defn- set-dialog-name
  [activity-data action-name page-idx]
  (->> (get-dialog-display-name page-idx)
       (assoc-in activity-data [:actions (keyword action-name) :phrase-description])))

(defn get-text-display-name
  [page-idx text-idx]
  (-> (get-dialog-display-name page-idx)
      (str " - " (if (number? text-idx)
                   (str "Text " text-idx)
                   text-idx))))

(defn- set-text-display-name
  [activity-data text-name page-idx text-idx]
  (->> {:display-name (get-text-display-name page-idx text-idx)
        :page-idx     page-idx
        :text-idx     text-idx}
       (update-in activity-data [:objects (keyword text-name) :metadata] merge)))

(defn- set-texts-display-names
  [activity-data texts-names page-idx]
  (->> texts-names
       (map-indexed (fn [idx text-name]
                      [(inc idx) text-name]))
       (reduce (fn [activity-data [text-idx text-name]]
                 (set-text-display-name activity-data text-name page-idx text-idx))
               activity-data)))

(defn update-display-names
  [activity-data]
  (let [book-name (-> activity-data flipbook/get-book-object-name keyword)]
    (->> (get-in activity-data [:objects book-name :pages])
         (map-indexed vector)
         (reduce (fn [activity-data [page-idx {:keys [action]}]]
                   (if (some? action)
                     (let [action-data (->> (keyword action) (get-action activity-data))
                           text-objects (->> (:data action-data)
                                             (map get-inner-action)
                                             (filter text-animation-action?)
                                             (map :target)
                                             (distinct))]
                       (-> (set-dialog-name activity-data action page-idx)
                           (set-texts-display-names text-objects page-idx)))
                     activity-data))
                 activity-data))))
