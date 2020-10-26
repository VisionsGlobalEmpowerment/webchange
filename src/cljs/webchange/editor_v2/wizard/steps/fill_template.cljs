(ns webchange.editor-v2.wizard.steps.fill-template
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.steps.common :refer [progress-block]]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]))

(defn- get-styles
  []
  {:form              {:padding-bottom "16px"}
   :text-input        {:margin-top "16px"}
   :control-container {:margin-top "8px"}})

(defn form
  [{:keys [data data-key validator template-id]}]
  (r/with-let [_ (re-frame/dispatch [::state-activity/load-templates])
               data (connect-data data data-key {} {})
               {:keys [destroy] :as validator} (validator/init data {} validator)]
    (let [templates @(re-frame/subscribe [::state-activity/templates])]
      (if (some? templates)
        (let [current-template (->> templates (filter #(= template-id (:id %))) first)]
          (if-not (empty? (:options current-template))
            [template {:template  current-template
                     :data      data
                     :validator validator}]
            [ui/typography "Selected template doesn't have any options. You can skip this step."]))
        [progress-block]))
    (finally
      (destroy))))

(def data
  {:label     "Fill Template"
   :header    "Start Your Activity Creation"
   :component form})
