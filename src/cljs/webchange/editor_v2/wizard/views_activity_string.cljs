(ns webchange.editor-v2.wizard.views-activity-string
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn string-option
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
    [ui/text-field {:label     "Value"
                    :variant   "outlined"
                    :value     (get @data key "")
                    :style     {:min-width "300px"}
                    :on-change #(swap! data assoc key (-> % .-target .-value))}]]])
