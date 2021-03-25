(ns webchange.editor-v2.wizard.activity-template.views-lookup
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.utils :refer [check-conditions]]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [select]]))

(def lookup-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})

(defn lookup-option
  [{:keys [key option data metadata validator]}]
  (r/with-let [lookup-data (connect-data data [key] "")
               {:keys [error-message]} (v/init lookup-data lookup-validation-map validator)]
    (let [options (->> (:options option)
                       (map (fn [{:keys [enable? name] :as option}]
                              (-> option
                                  (assoc :text name)
                                  (dissoc :name)
                                  (assoc :enable? (if (some? enable?)
                                                    (check-conditions enable? @data metadata)
                                                    true))))))]
      [:div
       [select {:value     @lookup-data
                :options   options
                :variant   "outlined"
                :on-change #(reset! lookup-data %)
                :width     160}]
       [error-message {:field-name :root}]])))
