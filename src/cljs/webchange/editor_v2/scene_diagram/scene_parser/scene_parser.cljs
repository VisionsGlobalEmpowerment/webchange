(ns webchange.editor-v2.scene-diagram.scene-parser.scene-parser
  (:require
    [webchange.editor-v2.scene-diagram.scene-parser.action-children :refer [get-action-children]]
    [webchange.editor-v2.scene-diagram.scene-parser.actions-tracks :refer [get-actions-tracks]]
    [webchange.editor-v2.scene-diagram.scene-parser.scene-entries :refer [get-entry-actions]]))

(defn- get-action-data
  [scene-data action-path]
  (->> action-path
       (concat [:actions])
       (get-in scene-data)))

(defn- dialog-action?
  [action-data]
  (contains? action-data :phrase))

(defn- get-dialog-actions
  [scene-data]
  (->> (:actions scene-data)
       (filter (fn [[_ action-data]] (dialog-action? action-data)))
       (map first)))

(defn scene-data->actions-tracks
  [scene-data]
  (->> scene-data
       (get-dialog-actions)
       (get-actions-tracks scene-data)))
