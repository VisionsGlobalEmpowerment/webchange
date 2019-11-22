(ns webchange.editor-v2.translator.translator-form.views-form-phrase
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn phrase-block
  [{:keys [node-data]}]
  (let [phrase-text (-> node-data
                        (get-in [:data :phrase-text])
                        (trim-text))]
    [ui/text-field
     {:label           "Phrase Text"
      :placeholder     "Enter phrase text"
      :variant         "outlined"
      :full-width      true
      :value           phrase-text
      :margin          "normal"
      :multiline       true
      :disabled        true
      :InputLabelProps {:shrink true}}]))
