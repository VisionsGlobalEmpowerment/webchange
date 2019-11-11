(ns webchange.editor-v2.views-data
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.interpreter.core :refer [load-course]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.events :as editor-events]))

(def diagram-modes [:full-scene "Full Scene View"
                    :translation "Translation"])

(defn data
  []
  (let [course-id (re-frame/subscribe [::subs/current-course])
        scene-id (re-frame/subscribe [::subs/current-scene])
        scenes (re-frame/subscribe [::subs/course-scenes])
        diagram-mode (re-frame/subscribe [::editor-subs/diagram-mode])]
    (fn []
      [:div.data-selector
       [ui/form-control {:full-width true
                         :margin     "normal"}
        [ui/input-label "Scene"]
        [ui/select {:value     (or @scene-id "")
                    :on-change #(let [scene-id (.. % -target -value)]
                                  (redirect-to :course-editor-v2-scene :id @course-id :scene-id scene-id)
                                  (re-frame/dispatch [::events/select-current-scene scene-id]))}
         (for [scene-id @scenes]
           ^{:key (str scene-id)}
           [ui/menu-item {:value scene-id
                          :style {:text-transform "capitalize"}}
            (s/replace scene-id #"-" " ")])
         ]]
       [ui/form-control {:full-width true
                         :margin     "normal"}
        [ui/input-label "Diagram Mode"]
        [ui/select {:value     (or @diagram-mode "")
                    :on-change #(let [mode (-> %
                                               (.. -target -value)
                                               (keyword))]
                                  (re-frame/dispatch [::editor-events/set-diagram-mode mode]))}
         (for [[mode-value mode-text] (partition 2 diagram-modes)]
           ^{:key (str mode-value)}
           [ui/menu-item {:value mode-value}
            mode-text])
         ]]])))
