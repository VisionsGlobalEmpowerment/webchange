(ns webchange.editor-v2.wizard.activity-template.views-audio
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def audio-validation-map {:src [(fn [value] (when-not (some? value) "audio is required"))]})

(defn- get-styles
  []
  {:audio-src {:padding     "8px"
               :margin-left "16px"
               :flex-grow   1}})

(defn- select-file-form
  [type uploading-atom on-change options]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        on-change (fn [js-file]
                    (reset! uploading-atom true)
                    (re-frame/dispatch [::assets-events/upload-asset js-file {:type type :on-finish on-finish}]))]
    [file-input/select-file-form {:loading @uploading-atom
                                  :on-change on-change
                                  :styles    {:wrapper      {:display "inline-block"}
                                              :button       {:padding "0 25px"}
                                              :icon-wrapper {:margin "-2px 16px 4px 0px"}
                                              :icon         {:font-size "24px"}}}]))

(defn audio-field
  ([value on-change]
   (audio-field value on-change nil))
  ([value on-change upload-options]
  (r/with-let [uploading (r/atom false)]
    (let [styles (get-styles)]
      [ui/grid {:container true :justify "flex-start" :align-items "flex-end"
                :spacing   16}
       (when-not (empty? value)
         [ui/grid {:item  true :xs 12
                   :style {:display         "flex"
                           :justify-content "flex-start"}}
          (if @uploading
            [ui/circular-progress])])
       [ui/grid {:item  true :xs 12
                 :style {:display "flex"}}
        [select-file-form :audio uploading on-change upload-options]
        [ui/text-field {:value     (str value)
                        :style     (:audio-src styles)
                        :on-change #(on-change (-> % .-target .-value))}]]]))))


(defn audio-option
  [{:keys [key option data validator]}]
  (r/with-let [page-data (connect-data data [key] nil)
               {:keys [error-message destroy]} (v/init page-data audio-validation-map validator)]
    [ui/grid {:container true
              :spacing   16
              :style     {:margin-top "-16px"}}
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "h6"
                      :style   {:display      "inline-block"
                                :margin-right "16px"}}
       (:label option)]
      [error-message {:field-name :root}]]
     [ui/grid {:item true :xs 12}
      [audio-field (get @page-data :src "") #(swap! page-data assoc :src %) (:options option)]
      [error-message {:field-name :src}]]]
    (finally
      (destroy))))
