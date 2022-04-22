(ns webchange.admin.pages.dashboard.views
  (:require
    [webchange.admin.widgets.toolbar.views :refer [toolbar]]))

(defn page
  []
  [:div "Dashboard"
   [toolbar {:title   "My Dashboard"
             :actions [{:id      :book
                        :icon    "book"
                        :title   "Books"
                        :handler #(print "book")}]}]])
