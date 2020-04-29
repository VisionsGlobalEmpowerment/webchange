(ns webchange.editor-v2.translator.translator-form.views-form-dialog
  (:require
    [clojure.string :refer [capitalize trim-newline]]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-current-action-data
                                                                  get-dialog-data
                                                                  trim-text]]))

(def text-input-params {:placeholder     "Dialog text"
                        :variant         "outlined"
                        :margin          "normal"
                        :disabled        true
                        :multiline       true
                        :full-width      true
                        :InputLabelProps {:shrink true}})

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
  []
  (let [selected-phrase-node (re-frame/subscribe [::editor-subs/current-action])
        graph @(re-frame/subscribe [::translator-form-subs/graph])
        current-concept @(re-frame/subscribe [::translator-form-subs/current-concept])
        data-store @(re-frame/subscribe [::translator-form-subs/edited-actions-data])
        dialog-data (get-dialog-data selected-phrase-node graph (fn [node-data]
                                                                   (get-current-action-data node-data current-concept data-store)))

        origin-text (data->text dialog-data #(get % :phrase-text))
        translated-text (data->text dialog-data #(or (get % :phrase-text-translated)
                                                     (get % :phrase-text)))]
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
