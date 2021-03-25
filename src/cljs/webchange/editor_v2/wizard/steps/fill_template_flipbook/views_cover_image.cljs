(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views-cover-image
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-control-row :refer [control-row]]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [file]]))

(def validation-map {:root [(fn [value] (when (empty? value) "Required field"))]})

(defn cover-image
  [{:keys [data option validator]}]
  (r/with-let [data (connect-data data [(-> option :key keyword)] nil)
               {:keys [destroy error-message]} (v/init data validation-map validator)]
    [:div
     [control-row {:label         "Cover Image"
                   :error-message [error-message {:field-name :root}]
                   :control       [file {:type       "image"
                                         :show-icon? false
                                         :on-change  #(swap! data assoc :src %)}]}]]
    (finally
      (destroy))))
