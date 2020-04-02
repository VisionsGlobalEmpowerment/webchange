(ns webchange.editor-v2.translator.translator-form.views-form-dialog
  (:require
    [clojure.string :refer [capitalize trim-newline]]
    [cljs-react-material-ui.reagent :as ui]))

(defn- trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn- data->text
  [data]
  (let [text (reduce (fn [result {:keys [phrase-text target]}]
                       (if-not (nil? phrase-text)
                         (let [text (if-not (nil? target)
                                      (str (capitalize target) ": " phrase-text)
                                      phrase-text)]
                           (str result text "\n"))
                         phrase-text))
                     ""
                     data)]
    (if-not (nil? text)
      (->> text
           trim-newline
           trim-text)
      nil)))

(defn dialog-block
  [{:keys [dialog-data]}]
  (let [dialog-text (data->text dialog-data)]
    [ui/text-field
     {:label           "Dialog Text"
      :placeholder     "Enter phrase text"
      :variant         "outlined"
      :full-width      true
      :value           (or dialog-text "")
      :margin          "normal"
      :multiline       true
      :disabled        true
      :InputLabelProps {:shrink true}}]))
