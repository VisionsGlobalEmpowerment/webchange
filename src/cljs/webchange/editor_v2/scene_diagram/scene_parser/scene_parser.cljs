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
  (loop [que (get-entry-actions scene-data)
         used-map {}
         result []]
    (if-not (empty? que)
      (let [[current-path & rest-que] que
            current-data (get-action-data scene-data current-path)]
        (if-not (contains? used-map current-path)
          (recur (concat rest-que (get-action-children {:action-path current-path
                                                        :action-data current-data}))
                 (assoc used-map current-path true)
                 (if (dialog-action? current-data)
                   (conj result current-path)
                   result))
          (recur rest-que used-map result)))
      result)))

(defn scene-data->actions-tracks
  [scene-data]
  (->> scene-data
       (get-dialog-actions)
       (get-actions-tracks scene-data)))
