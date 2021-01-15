(ns webchange.editor-v2.wizard.activity-template.views-image
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def image-validation-map {:src [(fn [value] (when-not (some? value) "Image is required"))]})

(defn- get-styles
  []
  {:image-src {:padding     "8px"
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
    [file-input/select-file-form {:on-change on-change
                                  :styles    {:wrapper      {:display "inline-block"}
                                              :button       {:padding "0 25px"}
                                              :icon-wrapper {:margin "-2px 16px 4px 0px"}
                                              :icon         {:font-size "24px"}}}]))

(defn image-field
  [value on-change]
  (r/with-let [uploading (r/atom false)]
    (let [styles (get-styles)]
      [ui/grid {:container true :justify "flex-start" :align-items "flex-end"
                :spacing   16}
       (when-not (empty? value)
         [ui/grid {:item  true :xs 12
                   :style {:display         "flex"
                           :justify-content "flex-start"}}
          (if @uploading
            [ui/circular-progress]
            [:img {:src   value
                   :style {:max-width     "100%"
                           :max-height    "400px"
                           :border        "1px solid #4b4b4b"
                           :border-radius "4px"
                           :padding       "4px"}}])])
       [ui/grid {:item  true :xs 12
                 :style {:display "flex"}}
        [select-file-form :image uploading on-change]
        [ui/text-field {:value     (str value)
                        :style     (:image-src styles)
                        :on-change #(on-change (-> % .-target .-value))}]]])))


(defn image-option
  [{:keys [key option data validator]}]
  (r/with-let [page-data (connect-data data [key] nil)
               {:keys [error-message destroy]} (v/init page-data image-validation-map validator)]
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
      [image-field (get @page-data :src "") #(swap! page-data assoc :src %)]
      [error-message {:field-name :src}]]]
    (finally
      (destroy))))
