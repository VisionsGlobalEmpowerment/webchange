(ns webchange.editor-v2.diagram-utils.modes.question.widget-phrase
  (:require
    [clojure.string :refer [capitalize]]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-phrase :as dialog.widget-phrase]
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

(defn- get-phrase-node
  [node]
  (assoc {:data (get-in node [:data :action :data 0])} :path (:path node)))

(defn phrase
  [node]
  (if (= :dialog (get-in node [:data :type]))
    (dialog.widget-phrase/phrase (get-phrase-node node))
    (let [text (case (get-in node [:data :type])
                 :question (get-in node [:data :action :data :text])
                 :answer (get-in node [:data :answer :text]))
          styles (get-styles)]
      (if-not (nil? text)
        [:div {:style (:title styles)}
         [:span text text text]]
        [:div {:style (:title styles)}]))))
