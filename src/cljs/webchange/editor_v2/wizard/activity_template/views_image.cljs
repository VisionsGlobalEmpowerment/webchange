(ns webchange.editor-v2.wizard.activity-template.views-image
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [file]]))

(def image-validation-map {:src [(fn [value] (when-not (some? value) "Image is required"))]})

(defn- get-styles
  []
  {:image-src {:padding     "8px"
               :margin-left "16px"
               :flex-grow   1}})

(defn- select-file-form
  [type uploading-atom on-change options]
  (let [on-finish (fn [result]
                    (on-change (:url result))
                    (reset! uploading-atom false))
        on-change (fn [js-file]
                    (reset! uploading-atom true)
                    (re-frame/dispatch [::assets-events/upload-asset js-file {:options options
                                                                              :type    type :on-finish on-finish}]))]
    [file-input/select-file-form {:on-change on-change
                                  :styles    {:wrapper      {:display "inline-block"}
                                              :button       {:padding "0 25px"}
                                              :icon-wrapper {:margin "-2px 16px 4px 0px"}
                                              :icon         {:font-size "24px"}}}]))

(defn image-field
  ([value on-change]
   (image-field value on-change nil))
  ([value on-change upload-options]
   (r/with-let [uploading (r/atom false)]
     (let [styles (get-styles)]
       [ui/grid {:container true :justify "flex-start" :align-items "flex-end"
                 :spacing   16}
        [ui/grid {:item  true :xs 12
                  :style {:display         "flex"
                          :justify-content "flex-start"}}
         (if @uploading
           [ui/circular-progress]
           (when-not (empty? value)
             [:img {:src   value
                    :style {:max-width     "100%"
                            :max-height    "400px"
                            :border        "1px solid #4b4b4b"
                            :border-radius "4px"
                            :padding       "4px"}}]))]
        [ui/grid {:item  true :xs 12
                  :style {:display "flex"}}
         [select-file-form :image uploading on-change upload-options]
         [ui/text-field {:value     (str value)
                         :style     (:image-src styles)
                         :on-change #(on-change (-> % .-target .-value))}]]]))))


(defn image-option
  [{:keys [key data validator]}]
  (r/with-let [page-data (connect-data data [key] nil)
               {:keys [error-message destroy]} (v/init page-data image-validation-map validator)]
    [:div
     [file {:type      "image"
            :on-change #(swap! page-data assoc :src %)}]
     [error-message {:field-name :src}]]
    (finally
      (destroy))))
