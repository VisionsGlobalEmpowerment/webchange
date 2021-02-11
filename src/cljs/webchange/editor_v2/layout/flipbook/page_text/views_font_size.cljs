(ns webchange.editor-v2.layout.flipbook.page-text.views-font-size
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]))

(def font-sizes [8 9 10 11 12 14 18 24 30 36 48 60 72 96])

(defn- get-styles
  []
  {:container {:display     "flex"
               :align-items "center"}
   :control   {:margin    "0 16px"
               :min-width "74px"
               :width     "74px"}})

(defn- get-available-font-sizes
  [current-value]
  (->> (conj font-sizes current-value)
       (remove nil?)
       (distinct)
       (sort)))

(defn font-size-control
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-size id])
        handle-change (fn [event]
                        (let [font-size (.. event -target -value)]
                          (re-frame/dispatch [::state/set-current-font-size id font-size])))
        available-font-sizes (get-available-font-sizes value)
        loading? @(re-frame/subscribe [::state/loading? id])
        styles (get-styles)]
    [:div {:style (:container styles)}
     [ui/typography {:variant "body1"}
      "Font Size:"]
     [ui/select {:value     value
                 :on-change handle-change
                 :variant   "outlined"
                 :width     36
                 :disabled  loading?
                 :style     (:control styles)}
      (for [size available-font-sizes]
        ^{:key size}
        [ui/menu-item {:value size} size])]]))
