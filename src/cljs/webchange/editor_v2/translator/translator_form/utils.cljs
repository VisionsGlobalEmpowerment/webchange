(ns webchange.editor-v2.translator.translator-form.utils
  (:require
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]))

(defn trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn get-graph
  [{:keys [scene-data start-node concept]}]
  (let [start-node-name (or (:origin-name start-node)
                            (keyword (:name start-node)))
        params {:start-node   start-node-name
                :concept-data {:current-concept concept}}
        diagram-mode :translation]
    (when-not (nil? start-node-name)
      (get-diagram-graph scene-data diagram-mode params))))

(defn audios->assets
  [audios]
  (map (fn [url] {:type "audio"
                  :size 1
                  :url  url}) audios))

(defn node-data->phrase-data
  [action-data]
  (select-keys (:data action-data)
               [:phrase-text
                :phrase-text-translated
                :target]))
