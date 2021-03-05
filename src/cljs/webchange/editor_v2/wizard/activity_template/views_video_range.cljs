(ns webchange.editor-v2.wizard.activity-template.views-video-range
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.editor-v2.components.slider.views :refer [slider]]))

(def video-validation-map {:from [(fn [value] (when-not (number? value) "Start time is required"))]
                           :to [(fn [value] (when-not (number? value) "Finish time is required"))]})

(defn- get-styles
  []
  {:video-placeholder {:font-size "14px"
                       :color     "#7e7e7e"}
   :video             {:border        "solid 1px #757575"
                       :border-radius "8px"
                       :padding       "3px"}})

(defn- video
  [{:keys [src ref]}]
  (let [styles (get-styles)
        handle-ref (fn [inst]
                     (when (some? inst)
                       (ref inst)))]
    (if (some? src)
       [:video {:src      src
                :controls true
                :style    (:video styles)
                :ref      handle-ref}]
       [ui/typography {:style (:video-placeholder styles)}
        "Select video to pick range"])))

(defn- set-current-frame
  [video frame]
  (set! (.-currentTime video) frame))

;; ToDo: Fix video rewind
;In my case I had to update video serving server to return response with Status Code 206 and Content-Range and Content-Length headers

(defn video-range-option
  [{:keys [key option data validator]}]
  (r/with-let [option-data (connect-data data [key] nil)
               {:keys [error-message destroy]} (v/init option-data video-validation-map validator)
               video-inst (r/atom nil)
               handle-video-ref (fn [inst]
                                  (reset! video-inst inst))
               handle-change (fn [val1 val2]
                               (cond
                                 (not= val1 (:from @option-data)) (set-current-frame @video-inst val1)
                                 (not= val2 (:to @option-data)) (set-current-frame @video-inst val2))
                               (swap! option-data assoc :from val1)
                               (swap! option-data assoc :to val2))]
    ;; "/upload/APRLSSKIULUDWCAB.webm"
    ;; "/upload/JSURPABPEHQZXDHG.mp4"
    (let [{:keys [video-param]} option
          video-src (->> (keyword video-param) (get @data))]
      [ui/grid {:container true
                :spacing   16
                :style     {:margin-top "-16px"}}
       [ui/grid {:item true :xs 12}
        [ui/typography {:variant "h6"
                        :style   {:display      "inline-block"
                                  :margin-right "16px"}}
         (:label option)]]
       [ui/grid {:item true :xs 12}
        [video {:src video-src
                :ref handle-video-ref}]
        (when (some? @video-inst)
          [slider {:value1    (get @option-data :from 0)
                   :value2    (get @option-data :to 0)
                   :min       0
                   :max       (.-duration @video-inst)
                   :on-change handle-change}])
        [error-message {:field-name :from}]
        [error-message {:field-name :to}]]])
    (finally
      (destroy))))
