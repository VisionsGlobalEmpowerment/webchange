(ns webchange.editor-v2.wizard.activity-template.views-characters
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def animation-names ["senoravaca" "vera" "mari"])

(def characters-validation-map {:root [(fn [value] (when (= (count value) 0) "Characters are required"))]})

(def character-validation-map {:name     [(fn [value] (when (empty? value) "Name is required"))]
                               :skeleton [(fn [value] (when (nil? value) "Character is required"))]})

(defn- character-option
  [{:keys [idx data validator on-remove last?]}]
  (r/with-let [character-data (connect-data data [idx])
               {:keys [error-message destroy]} (v/init character-data character-validation-map validator)]
    [ui/grid {:container true
              :spacing   16
              :style     {:margin-top "-16px"}}
     [ui/grid {:item true :xs 5}
      [ui/form-control {:full-width true}
       [ui/text-field {:label     "Name"
                       :variant   "outlined"
                       :value     (get @character-data :name "")
                       :style     {:margin-top "16px"}
                       :on-change #(swap! character-data assoc :name (-> % .-target .-value))}]
       [error-message {:field-name :name}]]]
     [ui/grid {:item true :xs 5}
      [ui/form-control {:full-width true}
       [ui/input-label "Character"]
       [ui/select {:value     (get @character-data :skeleton "")
                   :variant   "outlined"
                   :on-change #(swap! character-data assoc :skeleton (-> % .-target .-value))}
        (for [animation-name animation-names]
          ^{:key animation-name}
          [ui/menu-item {:value animation-name} animation-name])]
       [error-message {:field-name :skeleton}]]]
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

(defn characters-option
  [{:keys [key option data validator]}]
  (r/with-let [add-tooltip-open? (r/atom false)
               characters-data (connect-data data [key] [])
               {:keys [error-message] :as validator} (v/init characters-data characters-validation-map validator)]
    (let [handle-add-option (fn []
                              (if (< (count @characters-data) (:max option))
                                (swap! characters-data conj {})
                                (reset! add-tooltip-open? true)))
          handle-remove-option (fn [idx]
                                 (swap! characters-data (fn [data]
                                                          (let [item-to-remove (nth data idx)]
                                                            (->> data
                                                                 (remove #(= item-to-remove %))
                                                                 (into []))))))]
      (when (nil? @characters-data)
        (reset! characters-data []))

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

       (let [characters-list (map-indexed list @characters-data)]
         (for [[idx _] characters-list]
           ^{:key idx}
           [ui/grid {:item true :xs 12}
            [character-option {:idx       idx
                               :data      characters-data
                               :validator validator
                               :on-remove handle-remove-option
                               :last?     (= idx (dec (count characters-list)))}]]))])))
