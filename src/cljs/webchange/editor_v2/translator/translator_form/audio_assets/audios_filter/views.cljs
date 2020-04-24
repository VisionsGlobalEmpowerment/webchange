(ns webchange.editor-v2.translator.translator-form.audio-assets.audios-filter.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.audio-assets.common.views-target-selector :refer [target-selector]]))

(defn audios-filter
  [{:keys [on-change]}]
  (let [handle-target-changed (fn [target]
                                (on-change {:target target}))]
    [:div {:style {:margin-bottom "15px"}}
     [ui/typography {:variant "h6"
                     :style   {:display "inline-block"
                               :margin  "5px 0"}}
      "Show for target:"]
     [target-selector {:default-value ""
                       :extra-options [{:text  "Any"
                                        :value ""}]
                       :on-change     handle-target-changed}]]))
