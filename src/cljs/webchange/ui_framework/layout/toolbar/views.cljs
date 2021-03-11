(ns webchange.ui-framework.layout.toolbar.views
  (:require
    [webchange.ui-framework.layout.avatar.views :refer [avatar]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]))

(defn toolbar
  []
  [:div.page-toolbar
   [avatar]
   [logo]])
