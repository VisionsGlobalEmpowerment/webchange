(ns webchange.editor-v2.scene.views
  (:require
    [webchange.editor-v2.scene.views-canvas :refer [scene-canvas]]
    [webchange.editor-v2.scene.views-data :refer [data get-scenes-options]]
    [webchange.editor-v2.scene.views-diagram :refer [diagram]]))

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
   :scene-container {:flex-grow 1}})

(defn scene-translate
  []
  (let [styles (get-styles)]
    [:div {:style (:main-container styles)}
     [:div {:style (:top-panel styles)}
      [:div {:style (:data-container styles)}
       [data]]
      [:div {:style (:scene-container styles)}
       [scene-canvas]]]
     [diagram]]))
