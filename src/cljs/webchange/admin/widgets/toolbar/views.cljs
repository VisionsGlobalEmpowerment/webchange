(ns webchange.admin.widgets.toolbar.views
  (:require
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- toolbar-action
  [{:keys [handler icon title]}]
  [icon-button {:class-name "toolbar-action"
                :icon       icon
                :title      title
                :on-click   handler}])

(defn toolbar
  [{:keys [title actions]}]
  [:div.toolbar
   [:h1.title title]
   [:ul.actions
    (for [{:keys [id] :as action} actions]
      ^{:key id}
      [toolbar-action action])]])
