(ns webchange.editor-v2.translator.translator-form.audio-assets.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as es]
    [webchange.editor-v2.translator.translator-form.audio-assets.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.audio-assets.utils :refer [get-action-audio-data
                                                                               get-concept-actions
                                                                               get-concept-scene-actions]]
    [webchange.subs :as ws]))

(defn- list->map
  [list]
  (->> list
       (reduce (fn [result {:keys [url] :as asset}]
                 (assoc result url asset))
               {})))

(defn- get-scene-audio-assets-map
  [scene-assets]
  (->> scene-assets
       (filter (fn [{:keys [type]}]
                 (= type "audio")))
       (list->map)))

(defn- get-concepts-audio-assets-map
  [concepts-actions]
  (->> concepts-actions
       (map get-action-audio-data)
       (flatten)
       (list->map)))

(defn- fix-assets-data
  [assets-map]
  (let [assoc-when-nil (fn [data key value]
                         (if (nil? (get data key))
                           (assoc data key value)
                           data))]
    (->> assets-map
         (map (fn [[asset-key asset-data]]
                [asset-key (-> asset-data
                               (assoc-when-nil :alias "alias not defied")
                               (assoc-when-nil :type "audio"))]))
         (into {}))))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::init-assets-data])}))

(re-frame/reg-event-fx
  ::init-assets-data
  (fn [{:keys [db]} [_]]
    (let [scene-id (ws/current-scene db)
          concepts (es/course-dataset-items db)
          concepts-actions (->> (get-concept-scene-actions (es/course-concept db)
                                                           (ws/current-scene db))
                                (get-concept-actions (vals concepts)))
          concepts-audio-assets-map (get-concepts-audio-assets-map concepts-actions)
          scene-assets (ws/scene-assets db scene-id)
          scene-audio-assets-map (get-scene-audio-assets-map scene-assets)]
      {:db (assoc-in db (path-to-db [:data]) (-> (merge concepts-audio-assets-map
                                                        scene-audio-assets-map)
                                                 (fix-assets-data)))})))

(re-frame/reg-event-fx
  ::patch-asset
  (fn [{:keys [db]} [_ asset-key data-patch]]
    {:db (update-in db (path-to-db [:data asset-key]) merge data-patch)}))

(re-frame/reg-event-fx
  ::delete-asset
  (fn [{:keys [db]} [_ asset-key]]
    {:db (update-in db (path-to-db [:data]) dissoc asset-key)}))
