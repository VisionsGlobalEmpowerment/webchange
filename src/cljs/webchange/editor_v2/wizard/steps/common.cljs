(ns webchange.editor-v2.wizard.steps.common
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

(defn get-styles
  []
  {:form                         {:padding-bottom "16px"}
   :text-input                   {:margin-top "16px"}
   :control-container            {:margin-top "8px"}
   :select-control-container     {:margin-top "-16px"}
   :input-description            {:margin-bottom "8px"}
   :options-list                 {:padding 0}
   :options-list-item            {:padding "0 0 8px 0"}
   :options-list-item-last-child {:padding 0}})

(defn progress-block
  []
  [ui/grid {:container true
            :spacing   24}
   [ui/grid {:item  true
             :xs    12
             :style {:text-align "center"}}
    [ui/circular-progress]]])

(defn- last?
  [list item]
  (->> (last list)
       (= item)))

(defn- render-selected
  [{:keys [values options]}]
  (let [value->option (fn [current-value]
                        (some (fn [{:keys [value] :as option}] (and (= value current-value) option)) options))
        selected-items (map value->option values)
        styles (get-styles)]
    [ui/list {:style (:options-list styles)}
     (for [{:keys [value text] :as item} selected-items]
       ^{:key value}
       [ui/list-item {:style (merge (:options-list-item styles)
                                    (if (last? selected-items item)
                                      (:options-list-item-last-child styles)
                                      {}))}
        text])]))

(defn with-empty-item
  [empty-text options]
  (conj options {}))

(defn select-control
  [{:keys [label description value options multiple? on-change error-message]
    :or   {label       "Label"
           description "Description"
           options     []
           multiple?   false
           on-change   #()}}]
  (let [styles (get-styles)]
    [ui/grid {:item true :xs 12}
     [ui/typography {:variant "body1"
                     :style   (:input-description styles)}
      description]
     [ui/form-control {:full-width true
                       :style      (:select-control-container styles)}
      [ui/input-label label]
      [ui/select {:value        (or value (if multiple? [] ""))
                  :multiple     multiple?
                  :render-value (fn [value]
                                  (->> (fn []
                                         (let [value (js->clj value)]
                                           [render-selected {:values  (if (sequential? value) value [value])
                                                             :options options}]))
                                       (r/reactify-component)
                                       (r/create-element)))
                  :on-change    #(on-change (js->clj (-> % .-target .-value)))}
       (for [{:keys [value text]} options]
         ^{:key value}
         [ui/menu-item {:value value} text])]
      error-message]]))
