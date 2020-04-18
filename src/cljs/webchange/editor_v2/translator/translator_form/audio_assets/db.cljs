(ns webchange.editor-v2.translator.translator-form.audio-assets.db)

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :translator :audio-assets] relative-path))
