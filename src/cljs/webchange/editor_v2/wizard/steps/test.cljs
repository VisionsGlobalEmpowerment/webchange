(ns webchange.editor-v2.wizard.steps.test
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]))

(defn- get-styles
  []
  {:card              {:margin      "12px"
                       :flex-shrink "0"
                       :max-width   "500px"}
   :form              {:padding-bottom "16px"}
   :text-input        {:margin-top "16px"}
   :control-container {:margin-top "8px"}
   :actions           {:padding "24px"}})

(defn form
  [{:keys [parent-data]}]
  (r/with-let [data (connect-data parent-data :test {:name ""})
               styles (get-styles)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     24
              :align-items "center"
              :style       (:form styles)}
     [ui/grid {:item true :xs 10}
      [ui/form-control {:full-width true
                        :style      (:control-container styles)}
       [ui/text-field {:label      "Name"
                       :full-width true
                       :variant    "outlined"
                       :value      (:name @data)
                       :on-change  #(swap! data assoc :name (-> % .-target .-value))
                       :style      (:text-input styles)}]]]]))

(defn get-step
  [{:keys [data]}]
  {:label      "Label"
   :header     "Header"
   :sub-header "Sub-header"
   :content    [form {:parent-data data}]})
