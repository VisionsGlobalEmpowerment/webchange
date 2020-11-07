(ns webchange.editor-v2.scene-diagram.scene-parser.actions-tracks)

(def default-track "Etc")

(defn get-actions-tracks
  [scene-data dialog-actions-paths]
  (let [actions-data (map (fn [action-path]
                            [action-path (get-in scene-data (concat [:actions] action-path))])
                          dialog-actions-paths)]
    (reduce (fn [result [action-path action-data]]
              (let [track (:dialog-track action-data)]
                (update-in result [(or track default-track)] conj action-path)))
            {}
            actions-data)))
