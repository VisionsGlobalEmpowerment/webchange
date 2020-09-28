(ns webchange.editor-v2.wizard.views-activity-characters
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

(def animation-names ["senoravaca" "vera" "mari"])

(defn characters-option
  [key option data]
  (r/with-let [add-tooltip-open? (r/atom false)]
              (let [values (get @data key)
                    handle-add-option (fn []
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
                                :on-click handle-add-option}
                     "Add"]]]]

                 (for [[idx character] (map-indexed list values)]
                   ^{:key idx}
                   [ui/grid {:item true :xs 12}
                    [ui/grid {:container true
                              :spacing   16
                              :style     {:margin-top "-16px"}}
                     [ui/grid {:item true :xs 6}
                      [ui/form-control {:full-width true}
                       [ui/text-field {:label     "Name"
                                       :variant   "outlined"
                                       :value     (get character :name "")
                                       :style     {:margin-top "16px"}
                                       :on-change #(swap! data assoc-in [key idx :name] (-> % .-target .-value))}]]]
                     [ui/grid {:item true :xs 6}
                      [ui/form-control {:full-width true}
                       [ui/input-label "Character"]
                       [ui/select {:value     (get character :skeleton "")
                                   :on-change #(swap! data assoc-in [key idx :skeleton] (-> % .-target .-value))}
                        (for [animation-name animation-names]
                          ^{:key animation-name}
                          [ui/menu-item {:value animation-name} animation-name])]]]]])])))
