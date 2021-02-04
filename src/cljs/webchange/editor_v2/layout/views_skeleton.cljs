(ns webchange.editor-v2.layout.views-skeleton
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.state :as state]))

(defn- get-styles
  []
  {:main-container  {:height         "100%"
                     :display        "flex"
                     :flex-direction "column"}
   :top-panel       {:display        "flex"
                     :width          "100%"
                     :padding-bottom "30px"}
   :data-container  {:flex-grow 0
                     :width     "300px"}
   :scene-container {:flex-grow       1
                     :display         "flex"
                     :justify-content "center"}})

(defn skeleton
  [{:keys [top-left-component top-right-component bottom-component]}]
  (let [fullscreen? @(re-frame/subscribe [::state/diagram-fullscreen?])
        styles (get-styles)]
    [:div {:style (:main-container styles)}
     [:div {:style (merge (:top-panel styles)
                          (if fullscreen? {:display "none"} {}))}
      [:div {:style (:data-container styles)}
       [:div.data-selector
        top-left-component]]
      [:div {:style (:scene-container styles)}
       top-right-component]]
     bottom-component]))
