(ns webchange.editor-v2.wizard.activity-template.views-string
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def string-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})

(defn string-option
  [{:keys [key option data validator]}]
  (r/with-let [string-data (connect-data data [key] "")
               {:keys [error-message destroy]} (v/init string-data string-validation-map validator)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     16
              :align-items "center"}
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "h6"
                      :style   {:display      "inline-block"
                                :margin-right "16px"}}
       (:label option)]
      [ui/text-field {:label     "Value"
                      :variant   "outlined"
                      :value     @string-data
                      :style     {:min-width "300px"}
                      :on-change #(reset! string-data (-> % .-target .-value))}]
      [error-message {:field-name :root}]]]
    (finally
      (destroy))))
