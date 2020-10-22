(ns webchange.editor-v2.wizard.steps.fill-template
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
  [ui/typography "<<< Fill Template >>>"])

(defn get-step
  [{:keys [data]}]
  {:label      "Fill Template"
   :header     "Start Your Activity Creation"
   :content    [form {:parent-data data}]})
