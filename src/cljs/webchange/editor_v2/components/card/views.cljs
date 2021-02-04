(ns webchange.editor-v2.components.card.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn get-styles
  []
  {:card             {:display        "flex"
                      :flex-direction "column"}
   :card-full-height {:height "100%"}
   :list-full-height {:overflow "auto"
                      :position "absolute"
                      :width    "100%"
                      :height   "100%"}
   :action-icon      {:font-size "1.3rem"}})

(defn list-card
  []
  (let [this (r/current-component)
        {:keys [title title-action on-add-click full-height style]} (r/props this)
        custom-style (or style {})
        styles (get-styles)]
    [ui/card {:style (if full-height
                       (merge custom-style
                              (:card styles)
                              (:card-full-height styles))
                       (merge custom-style
                              (:card styles)))}
     [ui/card-content {:style {:flex-grow      1
                               :height         "100%"
                               :display        "flex"
                               :flex-direction "column"}}
      [ui/typography {:variant "h5"
                      :style   {:margin-bottom   "12px"
                                :display         "flex"
                                :justify-content "space-between"}}
       title
       (when-not (nil? title-action)
         title-action)]
      (into [:div {:style {:flex-grow 1
                     :position  "relative"}}]
            (r/children this))]
     (when-not (nil? on-add-click)
       [ui/card-actions {:style {:position   "relative"
                                 :min-height "74px"}}
        [fab {:size     "small"
              :style    {:position "absolute"
                         :right    0
                         :bottom   0
                         :margin   "16px"}
              :on-click on-add-click}
         [ic/add]]])]))
