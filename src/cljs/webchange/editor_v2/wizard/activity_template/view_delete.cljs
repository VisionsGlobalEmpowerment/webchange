(ns webchange.editor-v2.wizard.activity-template.view_delete
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.interpreter.renderer.state.editor :as editor]))

(defn delete-object-option
  [{:keys [key option data validator]}]
  (let [current-scene-objects @(re-frame/subscribe [::editor/selected-object])
        ]
    [:div
     (if (not current-scene-objects)
       [:span "You should select object to delete, on scene preview screen"]
       (do
         (swap! data assoc :object current-scene-objects)
         [:span "Are you sure you want to delete ball?"]))]))
