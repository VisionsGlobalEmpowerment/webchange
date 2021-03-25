(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-title
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-control-row :refer [control-row]]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [text-input]]))

(def validation-map {:root [(fn [value] (when (empty? value) "Required field"))]})

(defn title
  [{:keys [data option validator]}]
  (r/with-let [data (connect-data data [(-> option :key keyword)] "")
               {:keys [destroy error-message]} (v/init data validation-map validator)]
    [:div
     [control-row {:label         "Title"
                   :error-message [error-message {:field-name :root}]
                   :control       [text-input {:value     @data
                                               :on-change #(reset! data %)}]}]]
    (finally
      (destroy))))
