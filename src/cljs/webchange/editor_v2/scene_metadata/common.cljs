(ns webchange.editor-v2.scene-metadata.common
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as editor-events]
    [webchange.subs :as subs]))

(defn get-scene-metadata
  [scene-data]
  (:metadata scene-data))

(defn current-scene-metadata
  [db]
  (let [scene-data (subs/current-scene-data db)]
    {:data (get-scene-metadata scene-data)}))

(re-frame/reg-sub
  ::current-scene-metadata
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])])
  (fn [[scene-data]]
    {:data (get-scene-metadata scene-data)}))

(re-frame/reg-event-fx
  ::update-current-scene-metadata
  (fn [{:keys [db]} [_ metadata-patch]]
    (let [scene-id (subs/current-scene db)
          metadata (-> db current-scene-metadata :data)
          new-metadata (merge metadata metadata-patch)]
      {:dispatch [::editor-events/update-scene scene-id {:metadata new-metadata}]})))
