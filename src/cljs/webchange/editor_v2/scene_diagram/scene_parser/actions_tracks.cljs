(ns webchange.editor-v2.scene-diagram.scene-parser.actions-tracks)

(def default-track "Etc")

(defn get-actions-tracks
  [scene-data dialog-actions-names]
  (let [metadata-tracks (get-in scene-data [:metadata :tracks] [])
        metadata-tracks-actions (->> metadata-tracks
                                     (map :nodes)
                                     (flatten)
                                     (filter (fn [{:keys [type]}] (= type "dialog")))
                                     (map :action-id)
                                     (map keyword))
        actions-data (->> dialog-actions-names
                          (filter (fn [action-name] (not (some #{action-name} metadata-tracks-actions))))
                          (map (fn [action-name]
                                 [action-name (get-in scene-data [:actions action-name])])))]
    (->> actions-data
         (reduce (fn [result [action-path action-data]]
                   (let [track (:dialog-track action-data)]
                     (update-in result [(or track default-track)] conj action-path)))
                 {})
         (reduce (fn [result [track-name track-actions]]
                   (conj result {:title track-name
                                 :nodes (map (fn [action-id] {:type      "dialog"
                                                              :action-id action-id})
                                             track-actions)}))
                 metadata-tracks))))
