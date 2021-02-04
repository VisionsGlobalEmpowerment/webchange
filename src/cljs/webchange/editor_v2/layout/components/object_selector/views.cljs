(ns webchange.editor-v2.layout.components.object-selector.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.events :as editor-events]
    [webchange.subs :as subs]))

(defn- scene-data->objects-list
  [scene-data]
  (->> (:objects scene-data)
       (filter (fn [[_ object-data]] (= "text" (:type object-data))))))

(defn object-selector
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene-data (re-frame/subscribe [::subs/scene @scene-id])
        objects (scene-data->objects-list @scene-data)]
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
          [ui/menu-item {:value object-id} (str (name object-id) " (" type ")")])]])))
