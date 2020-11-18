(ns webchange.editor-v2.wizard.steps.common
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn get-styles
  []
  {:form                         {:padding-bottom "16px"}
   :text-input                   {:margin-top "16px"}
   :control-container            {:margin-top "8px"}
   :select-control-container     {:margin-top "-16px"}
   :input-description            {:margin-bottom "8px"}
   :options-list                 {:padding 0}
   :options-list-item            {:padding "0 0 8px 0"}
   :options-list-item-last-child {:padding 0}
   :progress-title               {:color (get-in-theme [:palette :text :secondary])}})

(defn progress-block
  [{:keys [title]}]
  (let [styles (get-styles)]
    [ui/grid {:container true
              :spacing   24}
     [ui/grid {:item  true
               :xs    12
               :style {:text-align "center"}}
      [ui/circular-progress]
      (when (some? title)
        [ui/typography {:style (:progress-title styles)} title])]]))

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

(defn select-control
  [{:keys [label description value options multiple? close-on-change? on-change error-message]
    :or   {label            "Label"
           description      "Description"
           options          []
           multiple?        false
           close-on-change? true
           on-change        #()}}]
  (r/with-let [open? (r/atom false)
               handle-open #(reset! open? true)
               handle-close #(reset! open? false)
               handle-change #(do (when close-on-change? (handle-close))
                                  (on-change (js->clj (-> % .-target .-value))))
               styles (get-styles)]
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
                  :open         @open?
                  :on-close     handle-close
                  :on-open      handle-open
                  :on-change    handle-change}
       (for [{:keys [value text]} options]
         ^{:key value}
         [ui/menu-item {:value value} text])]
      error-message]]))
