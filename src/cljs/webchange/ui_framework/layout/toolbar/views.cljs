(ns webchange.ui-framework.layout.toolbar.views
  (:require
    [webchange.ui-framework.layout.avatar.views :refer [avatar]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]))

(defn toolbar
  [{:keys [actions]}]
  [:div.page-toolbar
   [:div.left-side
    [avatar]
    [logo]]
   [:div.right-side
    (when (some? actions)
      actions)]])
