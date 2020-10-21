(ns webchange.editor-v2.wizard.steps.final-step
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]))

(defn- get-styles
  []
  {:icon-container {:text-align "center"}
   :icon           {:color     "#5f5f5f"
                    :font-size "170px"}})

(defn final-form
  [{:keys [data]}]
  (print "Final form")
  (print @data)
  (let [styles (get-styles)]
    [:div {:style (:icon-container styles)}
     [ic/check-circle {:style (:icon styles)}]]))

(defn get-step
  [{:keys [data]}]
  {:header     "Finish"
   :sub-header "Activity creation completed"
   :content    [final-form {:data data}]})
