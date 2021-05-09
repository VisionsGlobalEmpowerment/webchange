(ns webchange.editor-v2.dialog.dialog-form.views-form-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]))

(defn- get-styles
  []
  {:wrapper {:display     "flex"
             :align-items "baseline"}
   :label   {:margin-right "20px"}})

(defn- get-concept
  [concepts concept-id]
  (some
    (fn [{:keys [id] :as concept}]
      (and (= id concept-id)
           concept))
    concepts))

(defn concepts-block
  []
  (let [current-concept @(re-frame/subscribe [::translator-form.concepts/current-concept])
        concepts-list (->> @(re-frame/subscribe [::translator-form.concepts/concepts-list]))
        all-concepts-complete? (every? :complete? concepts-list)
        handle-concept-changed (fn [id]
                                 (re-frame/dispatch [::translator-form.concepts/prepare-concept (:id id)])
                                 (re-frame/dispatch [::translator-form.concepts/set-current-concept (:id id)]))
        styles (get-styles)]
    [:div
     [:div {:style (:wrapper styles)}
      (when-not all-concepts-complete?
        [warning-icon {:styles {:main {:position     "relative"
                                       :top          "3px"
                                       :font-size    "16px"
                                       :margin-right "7px"}}}])
      [ui/typography {:variant "body1"
                      :style   (:label styles)}
       "Concept:"]
      [ui/form-control {:full-width true
                        :margin     "normal"}
       [ui/select {:value        (or (:id current-concept) "")
                   :render-value (fn [value] (some #(and (= (:id %) value) (:name %)) concepts-list))
                   :variant      "outlined"
                   :on-change    #(handle-concept-changed (get-concept concepts-list (.. % -target -value)))}
        (for [concept concepts-list]
          ^{:key (:id concept)}
          [ui/menu-item {:value (:id concept)
                         :style {:display "flex"}}

           [ui/list-item-text
            (:name concept)]
           (when-not (:complete? concept)
             [ui/list-item-icon
              [warning-icon {:styles {:main {:font-size "16px"}}}]])])]]]]))
