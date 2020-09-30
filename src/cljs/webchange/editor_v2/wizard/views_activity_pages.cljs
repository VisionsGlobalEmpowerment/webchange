(ns webchange.editor-v2.wizard.views-activity-pages
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def pages-validation-map {:root [(fn [value] (when (= (count value) 0) "Pages are required"))]})
(def page-validation-map {:text [(fn [value] (when (empty? value) "Text is required"))]
                          :img  [(fn [value] (when (empty? value) "Image is required"))]})

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

(defn- page-option
  [{:keys [idx data validator on-remove last?]}]
  (r/with-let [page-data (connect-data data [idx])
               {:keys [error-message destroy]} (v/init page-data page-validation-map validator)]
    [ui/paper {:style {:padding "20px 10px"}}
     [ui/grid {:container true
               :spacing   16
               :style     {:margin-top "-16px"}}
      [ui/grid {:item true :xs 12}
       [ui/form-control {:full-width true}
        [ui/text-field {:label     "Text"
                        :variant   "outlined"
                        :value     (get @page-data :text "")
                        :on-change #(swap! page-data assoc :text (-> % .-target .-value))
                        :style     {:min-width "500px"}}]
        [error-message {:field-name :text}]]]
      [ui/grid {:item true :xs 10}
       [image-field (get @page-data :img "") #(swap! page-data assoc :img %)]
       [error-message {:field-name :img}]]
      [ui/grid {:item  true :xs 2
                :style {:display         "flex"
                        :align-items     "flex-end"
                        :justify-content "flex-end"}}
       (when last?
         [ui/icon-button {:on-click   #(on-remove idx)
                          :aria-label "Delete"
                          :style      {:padding "8px"}}
          [ic/delete]])]]]
    (finally
      (destroy))))

(defn pages-option
  [{:keys [key option data validator]}]
  (r/with-let [add-tooltip-open? (r/atom false)
               pages-data (connect-data data [key])
               {:keys [error-message] :as validator} (v/init pages-data pages-validation-map validator)]
    (let [handle-add-page (fn []
                            (if (< (count @pages-data) (:max option))
                              (swap! pages-data conj {})
                              (reset! add-tooltip-open? true)))
          handle-remove-page (fn [idx]
                               (swap! pages-data (fn [data]
                                                   (let [item-to-remove (nth data idx)]
                                                     (->> data
                                                          (remove #(= item-to-remove %))
                                                          (into []))))))]
      (when (nil? @pages-data)
        (reset! pages-data []))
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
           "Add"]]]
        [error-message {:field-name :root}]]
       (let [pages-list (map-indexed list @pages-data)]
         (for [[idx _] pages-list]
           ^{:key idx}
           [ui/grid {:item  true :xs 12
                     :style {:margin-bottom "12px"}}
            [page-option {:idx       idx
                          :data      pages-data
                          :validator validator
                          :on-remove handle-remove-page
                          :last?     (->> (count pages-list) (dec) (= idx))}]]))])))
