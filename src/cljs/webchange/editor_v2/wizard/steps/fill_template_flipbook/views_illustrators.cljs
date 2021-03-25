(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-illustrators
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-strings-list :refer [strings-list]]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def validation-map {:root [(fn [value] (when (or (empty? value)
                                                  (and (= (count value) 1)
                                                       (= (first value) ""))) "Required field"))]})

(defn illustrators
  [{:keys [data option validator]}]
  (r/with-let [data (connect-data data [(-> option :key keyword)] [""])
               {:keys [destroy error-message]} (v/init data validation-map validator)]
    [:div
     [strings-list {:data          data
                    :label         "Illustrator"
                    :max           (:max option)
                    :error-message [error-message {:field-name :root}]}]]
    (finally
      (destroy))))
