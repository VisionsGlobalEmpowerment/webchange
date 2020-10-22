(ns webchange.editor-v2.wizard.steps.skills
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]))

(defn- get-styles
  []
  {:form              {:padding-bottom "16px"}
   :text-input        {:margin-top "16px"}
   :control-container {:margin-top "8px"}})

(defn form
  [{:keys [parent-data validator]}]
  [ui/typography "<<< Skills >>>"])

(defn get-step
  [{:keys [data]}]
  {:label      "Skills"
   :header     "Skills"
   :sub-header "Decide what you will teach"
   :content    [form {:parent-data data}]})
