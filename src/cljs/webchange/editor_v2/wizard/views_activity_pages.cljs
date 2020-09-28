(ns webchange.editor-v2.wizard.views-activity-pages
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]))

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

(defn- image-field
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

(defn pages-option
  [key option data]
  (r/with-let [add-tooltip-open? (r/atom false)]
              (let [values (get @data key)
                    handle-add-page (fn []
                                      (if (< (count values) (:max option))
                                        (swap! data update key conj {})
                                        (reset! add-tooltip-open? true)))]
                (when (nil? values)
                  (swap! data assoc key []))
                [ui/grid {:container   true
                          :justify     "center"
                          :spacing     16
                          :align-items "center"}
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
                                :on-click handle-add-page}
                     "Add"]]]]
                 (for [[idx page] (map-indexed list values)]
                   ^{:key idx}
                   [ui/grid {:item  true :xs 12
                             :style {:margin-bottom "12px"}}
                    [ui/paper {:style {:padding "20px 10px"}}
                     [ui/grid {:container true
                               :spacing   16
                               :style     {:margin-top "-16px"}}
                      [ui/grid {:item true :xs 12}
                       [ui/form-control {:full-width true}
                        [ui/text-field {:label     "Text"
                                        :variant   "outlined"
                                        :value     (get page :text "")
                                        :on-change #(swap! data assoc-in [key idx :text] (-> % .-target .-value))
                                        :style     {:min-width "500px"}}]]]
                      [ui/grid {:item true :xs 12}
                       [image-field (:img page) #(swap! data assoc-in [key idx :img] %)]]]]])])))
