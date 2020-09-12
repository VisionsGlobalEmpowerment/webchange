(ns webchange.editor-v2.diagram-utils.modes.dialog.widget-phrase
  (:require
    [clojure.string :refer [capitalize]]
    [webchange.editor-v2.translator.translator-form.utils :refer [node-data->phrase-data]]))

(defn get-node-sppech
  [node-data]
  {:data (get-in node-data [:data :data 1])})

(defn- get-styles
  []
  {:header       {:display "flex"}
   :title        {:margin      "0"
                  :padding     "10px"
                  :text-align  "left"
                  :max-width   "250px"
                  :min-width   "180px"
                  :font-size   "1.5rem"
                  :line-height "1.28571429em"}
   :title-target {:font-size       "1.7rem"
                  :font-weight     "bold"
                  :margin-right    "16px"
                  :text-decoration "underline"}})

(defn phrase
  [node]
  (let [
        action? (= "action" (get-in node [:data :data 1 :type]))
        action-id (get-in node [:data :data 1 :id])
        action-name (if (:name node) (:name node) (if action? action-id))
        phrase-data (node-data->phrase-data node)
        phrase-target (when-not (nil? (:target phrase-data))
                        (-> (:target phrase-data) (capitalize) (str ":")))
        phrase-text (or (:phrase-text-translated phrase-data)
                        (:phrase-text phrase-data))
        styles (get-styles)]
    (if-not (nil? phrase-text)
      [:div {:style (:title styles)}
       (when-not (nil? phrase-target)
         [:span {:style (:title-target styles)} phrase-target])
       [:span phrase-text]]
      [:div {:style (:title styles)}
       [:p "New action"]
       [:p action-name]])))
