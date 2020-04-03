(ns webchange.editor-v2.translator.translator-form.views-form-dialog
  (:require
    [clojure.string :refer [capitalize trim-newline]]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]))

(defn- data->text
  [data get-text]
  (let [text (reduce (fn [result {:keys [target] :as phrase-data}]
                       (let [current-text (get-text phrase-data)]
                         (if-not (nil? current-text)
                           (let [text (if-not (nil? target)
                                        (str (capitalize target) ": " current-text)
                                        current-text)]
                             (str result text "\n"))
                           current-text)))
                     ""
                     data)]
    (if-not (nil? text)
      (->> text
           trim-newline
           trim-text)
      nil)))

(defn dialog-block
  [{:keys [dialog-data]}]
  (let [origin-text (data->text dialog-data #(get % :phrase-text))
        translated-text (data->text dialog-data #(or (get % :phrase-text-translated)
                                                     (get % :phrase-text)))
        text-input-params {:placeholder     "Dialog text"
                           :variant         "outlined"
                           :margin          "normal"
                           :disabled        true
                           :multiline       true
                           :full-width      true
                           :InputLabelProps {:shrink true}}]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label "Origin Dialog"
                             :value (or origin-text "")})]]
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label "Translated Dialog"
                             :value (or translated-text "")})]]]))
