(ns webchange.editor-v2.concepts.utils)

(defn resource-type?
  [type]
  (some #(= type %) ["string" "audio" "image" "video"]))
