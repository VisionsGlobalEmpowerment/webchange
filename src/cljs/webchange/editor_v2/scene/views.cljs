(ns webchange.editor-v2.scene.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]
    [webchange.editor-v2.scene.translation-steps :as translation-steps]
    [webchange.editor-v2.scene.views-canvas :refer [scene-canvas]]
    [webchange.editor-v2.scene.views-data :refer [data]]
    [webchange.editor-v2.state :as state]
    [webchange.subs :as subs]))

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

(defn scene-translate
  []
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
          fullscreen? @(re-frame/subscribe [::state/diagram-fullscreen?])
          styles (get-styles)]
      [:div {:style (:main-container styles)}
       [:div {:style (merge (:top-panel styles)
                            (if fullscreen? {:display "none"} {}))}
        [:div {:style (:data-container styles)}
         [data]]
        [:div {:style (:scene-container styles)}
         [scene-canvas]]]
       [:div {:style {:margin-bottom "16px"}}
        [translation-steps/fill-dialogs]]
       [dialogs-diagram {:scene-data scene-data}]])))
