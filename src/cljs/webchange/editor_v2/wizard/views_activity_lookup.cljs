(ns webchange.editor-v2.wizard.views-activity-lookup
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn lookup-option
  [key option data]
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
     [ui/select {:value     (get @data key "")
                 :on-change #(swap! data assoc key (-> % .-target .-value))
                 :style     {:min-width "150px"}}
      (for [{:keys [name value]} (:options option)]
        ^{:key value}
        [ui/menu-item {:value value} name])]]]])
