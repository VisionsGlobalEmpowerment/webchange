(ns webchange.admin.pages.dashboard.views
  (:require
    [webchange.admin.widgets.toolbar.views :refer [toolbar]]))

(defn page
  []
  [:div
   [toolbar {:title   "My Dashboard"
             :actions [{:id      :book1
                        :icon    "cancel"
                        :title   "Books"
                        :handler #(print "book")}
                       {:id      :book2
                        :icon    "cancel"
                        :title   "Books"
                        :handler #(print "book")}
                       {:id      :book3
                        :icon    "cancel"
                        :title   "Books"
                        :handler #(print "book")}]}]])
