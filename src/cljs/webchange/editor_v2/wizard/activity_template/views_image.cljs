(ns webchange.editor-v2.wizard.activity-template.views-image
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def string-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})

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
                 (when value
                   [ui/grid {:item  true :xs 12
                             :style {:display         "flex"
                                     :justify-content "center"}}
                    (if @uploading
                      [ui/circular-progress]
                      [:img {:src   value
                             :style {:max-width "100%"}}])])

                 [ui/grid {:item  true :xs 12
                           :style {:display "flex"}}
                  [select-file-form :image uploading on-change]
                  [ui/text-field {:value     (str value)
                                  :style     (:image-src styles)
                                  :on-change #(on-change (-> % .-target .-value))}]]])))



(defn image-option
  [{:keys [key option data validator]}]
  (println "key option data" key option data)
  (r/with-let [image-data (connect-data data [key] {:img ""})
               {:keys [error-message]} (v/init image-data string-validation-map validator)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     16
              :align-items "center"}
     [ui/grid {:item true :xs 10}
      [image-field (get @image-data :img "") #(do
                                                (println "image-data 1" image-data)
                                                (println "image-data 2" %)
                                                (swap! image-data assoc :img %))]
      [error-message {:field-name :img}]]])

  )
