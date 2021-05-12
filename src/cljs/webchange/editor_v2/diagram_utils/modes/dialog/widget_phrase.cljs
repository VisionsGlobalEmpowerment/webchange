(ns webchange.editor-v2.diagram-utils.modes.dialog.widget-phrase
  (:require
    [clojure.string :refer [capitalize]]
    [webchange.editor-v2.translator.translator-form.utils :refer [node-data->phrase-data]]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :refer [get-inner-action]]))

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
  (let [inner-action (-> node :data get-inner-action)
        max-len 17
        action? (= "action" (get inner-action :type))
        action-id (get inner-action :id)
        action-name (if (not (nil? (:name node))) (:name node) (if action? action-id))
        phrase-data (node-data->phrase-data {:data inner-action})
        phrase-target (when-not (nil? (:target phrase-data))
                        (-> (:target phrase-data) (capitalize) (str ":")))
        phrase-text (if-not (empty? (:phrase-text-translated phrase-data))
                      (:phrase-text-translated phrase-data)
                      (:phrase-text phrase-data))
        phrase-text (if (>  (count phrase-text) max-len) (str (subs phrase-text 0 max-len) "...") phrase-text)
        styles (get-styles)]
    (if-not (nil? phrase-text)
      [:div {:style (:title styles)}
       (when-not (nil? phrase-target)
         [:span {:style (:title-target styles)} phrase-target])
       [:span phrase-text]]
      [:div {:style (:title styles)}
       (if-not action-name
       [:p "New action"]
       [:p action-name])])))
