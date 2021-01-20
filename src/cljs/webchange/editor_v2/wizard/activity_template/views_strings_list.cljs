(ns webchange.editor-v2.wizard.activity-template.views-strings-list
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def strings-list-validation-map {:root [(fn [value] (when (empty? value) "Must not be empty"))]})
(def string-validation-map {:root [(fn [value] (when (= value "") "Must not be empty"))]})

(defn- string-option
  [{:keys [idx data validator on-remove last?]}]
  (r/with-let [string-data (connect-data data [idx] "")
               {:keys [error-message destroy]} (v/init string-data string-validation-map validator)]
    [ui/grid {:container true
              :spacing   16
              :style     {:margin-top "-16px"}}
     [ui/grid {:item true :xs 10}
      [ui/form-control {:full-width true}
       [ui/text-field {:label     "Name"
                       :variant   "outlined"
                       :value     @string-data
                       :style     {:margin-top "16px"}
                       :on-change #(reset! string-data (-> % .-target .-value))}]
       [error-message {:field-name :root}]]]
     [ui/grid {:item  true :xs 2
               :style {:display         "flex"
                       :align-items     "flex-end"
                       :justify-content "flex-start"}}
      (when last?
        [ui/icon-button {:on-click   #(on-remove idx)
                         :aria-label "Delete"
                         :style      {:padding "8px"}}
         [ic/delete]])]]
    (finally
      (destroy))))

(defn strings-list-option
  [{:keys [key option data validator]}]
  (r/with-let [add-tooltip-open? (r/atom false)
               strings-list-data (connect-data data [key] [])
               {:keys [error-message]} (v/init strings-list-data strings-list-validation-map validator)]
    (let [handle-add-option (fn []
                              (if (< (count @strings-list-data) (:max option))
                                (swap! strings-list-data conj "")
                                (reset! add-tooltip-open? true)))
          handle-remove-option (fn []
                                 (->> @strings-list-data
                                      (drop-last)
                                      (into [])
                                      (reset! strings-list-data)))]
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
                      :on-click handle-add-option}
           "Add"]]]
        [error-message {:field-name :root}]]
       (let [string-list (map-indexed list @strings-list-data)]
         (for [[idx] string-list]
           ^{:key idx}
           [ui/grid {:item true :xs 12}
            [string-option {:idx       idx
                            :data      strings-list-data
                            :validator validator
                            :on-remove handle-remove-option
                            :last?     (= idx (dec (count string-list)))}]]))])))
