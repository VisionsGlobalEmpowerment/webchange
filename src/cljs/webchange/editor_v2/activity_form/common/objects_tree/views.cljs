(ns webchange.editor-v2.activity-form.common.objects-tree.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.objects-tree.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- objects-tree-item
  [{:keys [name]}]
  [:div.scene-objects-tree-item
   [:div name]
   [:div
    [icon-button {:icon "visibility-on"}]
    [icon-button {:icon "visibility-off"}]
    [icon-button {:icon "preview"}]]])

(defn objects-tree
  []
  (let [objects @(re-frame/subscribe [::state/objects])]
    [:div.scene-objects-tree
     (for [{:keys [object-name] :as object} objects]
       ^{:key object-name}
       [objects-tree-item object])]))
