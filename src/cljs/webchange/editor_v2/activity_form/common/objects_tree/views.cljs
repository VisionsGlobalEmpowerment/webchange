(ns webchange.editor-v2.activity-form.common.objects-tree.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.objects-tree.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- objects-tree-item
  [{:keys [alias name]}]
  (let [visible? @(re-frame/subscribe [::state/visible? name])
        handle-visibility-click #(re-frame/dispatch [::state/set-object-visibility name (not visible?)])]
    [:div.scene-objects-tree-item
     [:div alias]
     [:div.actions
      [icon-button {:icon     (if visible? "visibility-on" "visibility-off")
                    :title    (if visible? "Make invisible" "Make visible")
                    :on-click handle-visibility-click}]]]))

(defn objects-tree
  []
  (let [objects @(re-frame/subscribe [::state/objects])]
    (when-not (empty? objects)
      [:div.scene-objects-tree
       (for [{:keys [name] :as object} objects]
         ^{:key name}
         [objects-tree-item object])])))
