(ns webchange.editor-v2.layout.components.object-selector.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.events :as editor-events]
    [webchange.editor-v2.layout.components.object-selector.state :as state]))

(defn object-selector
  []
  (let [objects @(re-frame/subscribe [::state/text-objects])]
    (when (not-empty objects)
      [ui/form-control {:full-width true
                        :margin     "normal"
                        :style      {:margin-top 0}}
       [ui/input-label "Select Object"]
       [ui/select {:value     ""
                   :variant   "outlined"
                   :on-change #(let [object-id (-> % (.. -target -value) (keyword))]
                                 (re-frame/dispatch [::editor-events/show-configure-object-form {:path [object-id]}]))}
        (for [[object-id {:keys [type]}] objects]
          ^{:key (name object-id)}
          [ui/menu-item {:value object-id} (str (name object-id) " (" type ")")])]])))
