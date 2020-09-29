(ns webchange.editor-v2.wizard.views-activity-lookup
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def lookup-validation-map {:root [(fn [value] (when (nil? value) "Required field"))]})

(defn lookup-option
  [{:keys [key option data validator]}]
  (r/with-let [lookup-data (connect-data data [key])
               {:keys [error-message]} (v/init lookup-data lookup-validation-map validator)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     16
              :align-items "center"}
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "h6"
                      :style   {:display      "inline-block"
                                :margin-right "16px"}}
       (:label option)]
      [ui/form-control {:style {:margin-top "-18px"}}
       [ui/input-label "Value"]
       [ui/select {:value     (or @lookup-data "")
                   :on-change #(reset! lookup-data (-> % .-target .-value))
                   :style     {:min-width "150px"}}
        (for [{:keys [name value]} (:options option)]
          ^{:key value}
          [ui/menu-item {:value value} name])]]
      [error-message {:field-name :root}]]]))
