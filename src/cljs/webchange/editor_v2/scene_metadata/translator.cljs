(ns webchange.editor-v2.scene-metadata.translator
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.scene-metadata.common :as common]))

(def translator-state-key :translator)

(defn- get-translator-data
  [metadata]
  (translator-state-key metadata))

(defn- scene->translator-metadata
  [metadata]
  (-> metadata
      (assoc :data (get-in metadata [:data translator-state-key]))))

(defn translator-metadata
  [db]
  (let [metadata (common/current-scene-metadata db)]
    (scene->translator-metadata metadata)))

(re-frame/reg-sub
  ::translator-metadata
  (fn []
    [(re-frame/subscribe [::common/current-scene-metadata])])
  (fn [[metadata]]
    (scene->translator-metadata metadata)))

(re-frame/reg-sub
  ::translation-steps
  (fn []
    [(re-frame/subscribe [::translator-metadata])])
  (fn [[metadata]]
    (-> metadata
        (assoc :data (->> [:data :translation-steps]
                          (get-in metadata)
                          (map keyword))))))

(re-frame/reg-event-fx
  ::add-translation-step
  (fn [{:keys [db]} [_ step-name]]
    (let [metadata (-> db translator-metadata :data)
          new-metadata (update metadata :translation-steps conj step-name)]
      {:dispatch [::common/update-current-scene-metadata (assoc {} translator-state-key new-metadata)]})))
