(ns webchange.editor-v2.scene.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.card.views :refer [list-card] :as card]
    [webchange.editor-v2.scene.views-canvas :refer [scene-canvas]]
    [webchange.editor-v2.scene.views-data :refer [data get-scenes-options]]
    [webchange.editor-v2.scene.views-diagram :refer [diagram]]
    [webchange.routes :refer [redirect-to]]
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
   :scene-container {:flex-grow 1}})

(defn scenes-list
  []
  (let [course @(re-frame/subscribe [::subs/current-course])
        scenes @(re-frame/subscribe [::subs/course-scenes])
        scenes-options (get-scenes-options scenes)
        list-styles (card/get-styles)]
    [list-card {:title       "Scenes"
                :full-height true}
     [ui/list {:style (:list-full-height list-styles)}
      (for [scene scenes-options]
        ^{:key (:value scene)}
        [ui/list-item
         [ui/list-item-text {:primary (:text scene)}]
         [ui/list-item-secondary-action
          [ui/icon-button {:aria-label "Edit"
                           :on-click #(redirect-to :course-editor-v2-scene :id course :scene-id (:value scene)) }
           [ic/edit {:style (:action-icon list-styles)}]]]])]]))

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
