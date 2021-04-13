(ns webchange.ui-framework.layout.toolbar.views
  (:require
    [webchange.book-creator.course-status.views :refer [review-status]]
    [webchange.sync-status.views :refer [sync-status]]
    [webchange.ui-framework.layout.avatar.views :refer [avatar]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]))

(defn toolbar
  [{:keys [actions]}]
  [:div.page-toolbar
   [:div.left-side
    [avatar]
    [logo]]
   [:div.right-side
    [sync-status {:class-name "sync-status"}]
    [review-status]
    (when (some? actions)
      actions)]])
