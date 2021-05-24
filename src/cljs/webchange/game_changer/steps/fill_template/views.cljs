(ns webchange.game-changer.steps.fill-template.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.views :refer [options-form prepare-options]]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->options filter-options]]))

(defn- empty-form
  []
  [:div.empty-form-message
   "Current template does not require additional content."])

(defn template-form
  [{:keys [data]}]
  (r/with-let [{:keys [valid?] :as validator} (validator/init data)]
    (let [options (-> @data data->options prepare-options (filter-options ["characters"]))
          options-data (connect-data data [:options])]

      (swap! options-data assoc :valid? (valid?))

      (if-not (empty? options)
        [options-form {:options   options
                       :data      options-data
                       :validator validator
                       ;:metadata
                       }]
        [empty-form]))))
