(ns webchange.ui-framework.layout.right-menu.object-tree-menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.objects-tree.state :as state]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.ui-framework.components.index :refer [icon icon-button]]))

(defn- objects-tree-item
  [{:keys [alias name type]}]
  (let [visible? @(re-frame/subscribe [::state/visible? name])
        show-remove-button? @(re-frame/subscribe [::state/show-remove-button? name])
        handle-visibility-click #(re-frame/dispatch [::state/set-object-visibility name (not visible?)])
        handle-select-object #(re-frame/dispatch [::editor-state/select-object name])
        handle-remove-click #(re-frame/dispatch [::state/remove-object name])]
    [:li.object-action-item
     [:div.object-title
      [icon {:icon       type
             :class-name "object-type"}]
      [:span alias]]
     [:div.object-actions
      (when show-remove-button?
        [icon-button {:icon       "remove"
                      :on-click   handle-remove-click
                      :class-name "object-icon-style"}])
      [icon-button {:icon       (if visible? "visibility-on" "visibility-off")
                    :title      (if visible? "Make invisible" "Make visible")
                    :on-click   handle-visibility-click
                    :class-name "object-icon-style"}]
      [icon-button {:icon       "settings"
                    :class-name "object-icon-style"
                    :on-click   handle-select-object}]]]))

(defn objects-tree-menu
  []
  (let [objects @(re-frame/subscribe [::state/objects])]
    (when-not (empty? objects)
      [:div.objects-tree-card
       [:ul
        (for [{:keys [name] :as object} objects]
          ^{:key name}
          [objects-tree-item object])]])))
