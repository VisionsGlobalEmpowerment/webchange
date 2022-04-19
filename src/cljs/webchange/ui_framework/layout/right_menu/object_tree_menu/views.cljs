(ns webchange.ui-framework.layout.right-menu.object-tree-menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.objects-tree.state :as state]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.logger.index :as logger]
    [webchange.ui-framework.components.index :refer [icon icon-button]]))

(defn- edit-control
  [{:keys [name]}]
  (let [handle-click #(re-frame/dispatch [::editor-state/select-object name])]
    [icon-button {:icon       "settings"
                  :class-name "object-icon-style"
                  :on-click   handle-click}]))

(defn- remove-control
  [{:keys [name]}]
  (let [handle-click #(re-frame/dispatch [::state/remove-object name])]
    [icon-button {:icon       "remove"
                  :on-click   handle-click
                  :class-name "object-icon-style"}]))

(defn- visibility-control
  [{:keys [name]}]
  (let [visible? @(re-frame/subscribe [::state/visible? name])
        handle-click #(re-frame/dispatch [::state/set-object-visibility name (not visible?)])]
    [icon-button {:icon       (if visible? "visibility-on" "visibility-off")
                  :title      (if visible? "Make invisible" "Make visible")
                  :on-click   handle-click
                  :class-name "object-icon-style"}]))

(def actions-map {"edit"       edit-control
                  "remove"     remove-control
                  "visibility" visibility-control})

(defn- objects-tree-item-actions
  [{:keys [actions] :as props}]
  [:div.object-actions
   (for [action actions]
     (if-let [control (get actions-map action)]
       ^{:key action}
       [control props]
       (logger/warn (str "Control for action '" action "' is not defined"))))])

(defn- objects-tree-item
  [{:keys [alias type] :as props}]
  [:li.object-action-item
   [:div.object-title
    [icon {:icon       type
           :class-name "object-type"}]
    [:span alias]]
   [objects-tree-item-actions props]])

(defn objects-tree-menu
  []
  (let [objects @(re-frame/subscribe [::state/objects])]
    (when-not (empty? objects)
      [:div.objects-tree-card
       [:ul
        (for [{:keys [name] :as object} objects]
          ^{:key name}
          [objects-tree-item object])]])))
