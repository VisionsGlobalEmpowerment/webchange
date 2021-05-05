(ns webchange.game-changer.steps.fill-template.views
  (:require
    [webchange.editor-v2.wizard.activity-template.views :refer [options-form prepare-options]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->options filter-options]]))

(defn- empty-form
  []
  [:div.empty-form-message
   "Current template does not require additional content."])

(defn template-form
  [{:keys [data]}]
  (let [options (-> @data data->options prepare-options (filter-options ["characters"]))]
    (if-not (empty? options)
      [options-form {:options options
                     :data    (connect-data data [:options])
                     ;:metadata
                     ;:validator
                     }]
      [empty-form])))
