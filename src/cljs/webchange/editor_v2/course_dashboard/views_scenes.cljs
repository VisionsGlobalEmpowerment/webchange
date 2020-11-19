(ns webchange.editor-v2.course-dashboard.views-scenes
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.card.views :refer [list-card] :as card]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]))

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
