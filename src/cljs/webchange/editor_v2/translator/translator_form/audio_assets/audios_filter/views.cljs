(ns webchange.editor-v2.translator.translator-form.audio-assets.audios-filter.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.common.views-audio-target-selector :refer [audio-target-selector]]))

(defn- get-styles
  []
  {:wrapper {:style {:margin-bottom "15px"}}
   :title   {:display "inline-block"
             :margin  "5px 0"}})

(defn audios-filter
  [{:keys [on-change]}]
  (let [empty-target-option {:text "Any" :value ""}
        handle-target-changed (fn [target] (on-change {:target target}))
        styles (get-styles)]
    [:div (:wrapper styles)
     [ui/typography {:variant "h6"
                     :style   (:title styles)}
      "Filter audios:"]
     [audio-target-selector {:default-value (:value empty-target-option)
                             :extra-options [empty-target-option]
                             :on-change     handle-target-changed}]]))
