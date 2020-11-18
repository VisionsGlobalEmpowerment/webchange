(ns webchange.editor-v2.scene.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.layout.card.views :refer [list-card] :as card]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]
    [webchange.editor-v2.scene.translation-steps :as translation-steps]
    [webchange.editor-v2.scene.views-canvas :refer [scene-canvas]]
    [webchange.editor-v2.scene.views-data :refer [data]]
    [webchange.editor-v2.state :as state]
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
   :scene-container {:flex-grow       1
                     :display         "flex"
                     :justify-content "center"}})

(defn get-scenes-options
  [scenes-list]
  (map (fn [scene-id]
         {:value scene-id
          :text  (s/replace scene-id #"-" " ")})
       scenes-list))

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
                           :on-click   #(redirect-to :course-editor-v2-scene :id course :scene-id (:value scene))}
           [ic/edit {:style (:action-icon list-styles)}]]]])]]))

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
