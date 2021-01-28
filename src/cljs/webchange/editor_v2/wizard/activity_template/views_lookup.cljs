(ns webchange.editor-v2.wizard.activity-template.views-lookup
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.activity-template.utils :refer [check-conditions]]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def lookup-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})

(defn lookup-option
  [{:keys [key option data metadata validator]}]
  (r/with-let [lookup-data (connect-data data [key] "")
               {:keys [error-message]} (v/init lookup-data lookup-validation-map validator)]
    (let [options (->> (:options option)
                       (map (fn [{:keys [enable?] :as option}]
                              (assoc option :enable? (if (some? enable?)
                                                       (check-conditions enable? @data metadata)
                                                       true)))))]
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
         [ui/select {:value     @lookup-data
                     :variant   "outlined"
                     :on-change #(reset! lookup-data (-> % .-target .-value))
                     :style     {:min-width "150px"}}
          (for [{:keys [name value enable?]} options]
            ^{:key value}
            [ui/menu-item {:value    value
                           :disabled (not enable?)}
             name])]]
        [error-message {:field-name :root}]]])))
