(ns webchange.editor-v2.wizard.activity-template.views-video
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def video-validation-map {:root [(fn [value] (when-not (some? value) "video is required"))]})

(defn- get-styles
  []
  {:video-src {:padding     "8px"
               :margin-left "16px"
               :flex-grow   1}})

(defn- select-file-form
  [type uploading-atom on-change]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        on-change (fn [js-file]
                    (reset! uploading-atom true)
                    (re-frame/dispatch [::concepts-events/upload-asset js-file {:type type :on-finish on-finish}]))]
    [file-input/select-file-form {:loading @uploading-atom
                                  :on-change on-change
                                  :styles    {:wrapper      {:display "inline-block"}
                                              :button       {:padding "0 25px"}
                                              :icon-wrapper {:margin "-2px 16px 4px 0px"}
                                              :icon         {:font-size "24px"}}}]))

(defn video-field
  ([value on-change]
   (video-field value on-change nil))
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
        [select-file-form :video uploading on-change upload-options]
        [ui/text-field {:value     (str value)
                        :style     (:video-src styles)
                        :on-change #(on-change (-> % .-target .-value))}]]]))))


(defn video-option
  [{:keys [key option data validator]}]
  (r/with-let [page-data (connect-data data [key] nil)
               {:keys [error-message destroy]} (v/init page-data video-validation-map validator)]
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
      [video-field (or @page-data "") #(reset! page-data %) (:options option)]]]
    (finally
      (destroy))))
