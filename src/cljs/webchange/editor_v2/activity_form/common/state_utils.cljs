(ns webchange.editor-v2.activity-form.common.state-utils
  (:require
    [webchange.utils.scene-data :as scene-data-utils]))

(defn prepare-selected-objects
  [{:keys [objects-names scene-data]}]
  (let [objects-names (if (sequential? objects-names) objects-names [objects-names])]
    {:names objects-names
     :data  (->> (first objects-names)
                 (scene-data-utils/get-scene-object scene-data))}))
