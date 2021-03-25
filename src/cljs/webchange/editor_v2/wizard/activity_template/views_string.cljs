(ns webchange.editor-v2.wizard.activity-template.views-string
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [text-area]]))

(def string-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})

(defn string-option
  [{:keys [key data validator]}]
  (r/with-let [string-data (connect-data data [key] "")
               {:keys [error-message destroy]} (v/init string-data string-validation-map validator)]
    [:div {:style {:width 416}}
     [text-area {:placeholder "Place your text"
                 :variant     "outlined"
                 :value       @string-data
                 :on-change   #(reset! string-data %)}]
     [error-message {:field-name :root}]]
    (finally
      (destroy))))
