(ns webchange.editor-v2.layout.card.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn- get-styles
  []
  {:card             {:display        "flex"
                      :flex-direction "column"}
   :card-full-height {:height "100%"}})

(defn list-card
  [{:keys [title title-action on-add-click full-height style]} & children]
  (let [custom-style (or style {})
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
      [:div {:style {:flex-grow 1
                     :position  "relative"}}
       children]]
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
