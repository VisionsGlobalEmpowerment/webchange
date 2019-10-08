(ns webchange.editor-v2.views-data
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.interpreter.core :refer [load-course]]))

(defn data
  []
  (let [course-id (re-frame/subscribe [::subs/current-course])
        scene-id (re-frame/subscribe [::subs/current-scene])
        scenes (re-frame/subscribe [::subs/course-scenes])]
    (fn []
      [:div.data-selector
       [ui/form-control {:style {:min-width 180}}
        [ui/input-label "Scene"]
        [ui/select {:value (or @scene-id "")
                    :on-change #(let [scene-id (.. % -target -value)]
                                  (redirect-to :course-editor-v2-scene :id @course-id :scene-id scene-id)
                                  (re-frame/dispatch [::events/select-current-scene scene-id]))}
         (for [scene-id @scenes]
           ^{:key (str scene-id)}
           [ui/menu-item {:value scene-id
                          :style {:text-transform "capitalize"}}
            (s/replace scene-id #"-" " ")])
         ]]])))
