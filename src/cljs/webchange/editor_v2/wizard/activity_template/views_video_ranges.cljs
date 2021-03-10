(ns webchange.editor-v2.wizard.activity-template.views-video-ranges
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.editor-v2.components.slider.views :refer [slider]]
    [webchange.editor-v2.wizard.activity-template.utils-video :as vu]
    ))

(def video-validation-map {:root [(fn [value] (when (empty? value) "Select at least 1 video range"))]})

(def range-validation-map {:from [(fn [value] (when-not (number? value) "Start time is required"))]
                           :to   [(fn [value] (when-not (number? value) "Finish time is required"))]})

(defn- get-styles
  []
  {:video-container   {:display "flex"}
   :video-placeholder {:font-size "14px"
                       :color     "#7e7e7e"}
   :video             {:border        "solid 1px #757575"
                       :border-radius "8px"
                       :padding       "3px"
                       :width         "100%"
                       :max-height    "400px"}})

(defn- video
  [{:keys [src ref]}]
  (let [styles (get-styles)
        handle-ref (fn [inst]
                     (when (some? inst)
                       (ref inst)))]
    [:div {:style (:video-container styles)}
     (if (some? src)
       [:video {:src      src
                :controls false
                :style    (:video styles)
                :ref      handle-ref}]
       [ui/typography {:style (:video-placeholder styles)}
        "Select video to pick range"])]))

(defn- range-option
  [{:keys [idx data max-value validator on-play on-change on-remove last?]}]
  (r/with-let [range-data (connect-data data [idx])
               {:keys [destroy]} (v/init range-data range-validation-map validator)
               handle-slider-change (fn [val1 val2]
                                      (cond
                                        (not= val1 (:from @range-data)) (on-change val1)
                                        (not= val2 (:to @range-data)) (on-change val2))
                                      (reset! range-data {:from val1 :to val2}))]
    [ui/grid {:container true
              :spacing   16
              :style     {:margin-top "-16px"}}
     [ui/grid {:item true :xs 8}
      [:div {:style {:width      "100%"
                     :margin-top "-5px"}}
       [slider {:value1    (:from @range-data)
                :value2    (:to @range-data)
                :min       0
                :max       max-value
                :step      0.01
                :on-change handle-slider-change}]]]

     [ui/grid {:item  true :xs 2
               :style {:display     "flex"
                       :align-items "center"}}
      [ui/typography {:style {:font-family "monospace"
                              :font-size   "12px"}}
       (:from @range-data)]
      [ui/typography {:style {:font-family "monospace"
                              :font-size   "12px"}}
       "-"]
      [ui/typography {:style {:font-family "monospace"
                              :font-size   "12px"}}
       (:to @range-data)]]

     [ui/grid {:item  true :xs 2
               :style {:display         "flex"
                       :align-items     "flex-end"
                       :justify-content "flex-start"}}
      [ui/icon-button {:on-click   #(on-play @range-data)
                       :aria-label "Delete"
                       :style      {:padding "8px"}}
       [ic/play-circle-filled]]
      (when last?
        [ui/icon-button {:on-click   #(on-remove idx)
                         :aria-label "Delete"
                         :style      {:padding "8px"}}
         [ic/delete]])]]
    (finally
      (destroy))))

(defn video-ranges-option
  [{:keys [key option data validator]}]
  (r/with-let [ranges-list-data (connect-data data [key] [])
               {:keys [error-message destroy]} (v/init ranges-list-data video-validation-map validator)

               add-tooltip-open? (r/atom false)
               video-inst (r/atom nil)

               handle-add-range (fn []
                                  (if (< (count @ranges-list-data) (:max option))
                                    (swap! ranges-list-data conj {:from 0 :to (.-duration @video-inst)})
                                    (reset! add-tooltip-open? true)))
               handle-remove-range (fn []
                                     (->> @ranges-list-data
                                          (drop-last)
                                          (into [])
                                          (reset! ranges-list-data)))]
    (let [{:keys [video-param]} option
          video-src (->> (keyword video-param) (get @data))]
      [ui/grid {:container true
                :spacing   16
                :style     {:margin-top "-16px"}}
       [ui/grid {:item true :xs 12}
        [ui/typography {:variant "h6"
                        :style   {:display      "inline-block"
                                  :margin-right "16px"}}
         (:label option)]
        [ui/click-away-listener {:onClickAway #(reset! add-tooltip-open? false)}
         [ui/tooltip {:title                  (str "Max. " (:max option) " items")
                      :open                   @add-tooltip-open?
                      :placement              "right"
                      :disable-focus-listener true
                      :disable-hover-listener true
                      :disable-touch-listener true}
          [ui/button {:size     "small"
                      :on-click handle-add-range}
           "Add"]]]
        [error-message {:field-name :root}]]
       [ui/grid {:item true :xs 12}
        [video {:src video-src
                :ref #(reset! video-inst %)}]]
       [ui/grid {:item true :xs 12}

        (when (some? video-src)
          (let [ranges-list (map-indexed list @ranges-list-data)]
            (for [[idx] ranges-list]
              ^{:key idx}
              [ui/grid {:item true :xs 12}
               [range-option {:idx       idx
                              :data      ranges-list-data
                              :validator validator
                              :max-value (.-duration @video-inst)
                              :on-play   #(vu/play-range @video-inst %)
                              :on-change #(vu/set-current-time @video-inst %)
                              :on-remove handle-remove-range
                              :last?     (= idx (dec (count ranges-list)))}]])))]])
    (finally
      (destroy))))
