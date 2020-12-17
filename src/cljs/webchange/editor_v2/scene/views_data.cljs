(ns webchange.editor-v2.scene.views-data
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.routes :refer [redirect-to]]
    [webchange.subs :as subs]
    [webchange.interpreter.core :refer [load-course]]
    [webchange.editor-v2.scene.data.background.views-background :refer [change-background]]
    [webchange.editor-v2.scene.data.skin.views-skin :refer [change-skin]]
    [webchange.editor-v2.scene.data.stage.views :refer [select-stage]]
    [webchange.editor-v2.sandbox.views-modal :as share]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.utils :refer [keyword->caption]]))

(def diagram-modes [:full-scene "Full Scene View"
                    :phrases "Translation"])

(defn phrase-action-data?
  [action-data]
  (contains? action-data :phrase))

(defn scene-data->objects-list
  [scene-data]
  (->> (:objects scene-data)
       (filter (fn [[_ object-data]] (= "text" (:type object-data))))))

(defn data
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene-data (re-frame/subscribe [::subs/scene @scene-id])]
    (let [objects (scene-data->objects-list @scene-data)]
      [:div.data-selector
       [change-background]
       [share/share-button]
       [change-skin]
       (when (not-empty objects)
         [ui/form-control {:full-width true
                           :margin     "normal"}
          [ui/input-label "Select Object"]
          [ui/select {:value     ""
                      :variant   "outlined"
                      :on-change #(let [object-id (-> % (.. -target -value) (keyword))]
                                    (re-frame/dispatch [::editor-events/show-configure-object-form {:path [object-id]}]))}
           (for [[object-id {:keys [type]}] objects]
             ^{:key (name object-id)}
             [ui/menu-item {:value object-id} (str (name object-id) " (" type ")")])]])
       [select-stage]])))
