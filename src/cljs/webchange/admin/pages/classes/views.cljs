(ns webchange.admin.pages.classes.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.classes.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]))

(defn- header
  []
  (let [school-name @(re-frame/subscribe [::state/school-name])
        handle-add-click #(print "Add New Class")]
    [page/header {:title   school-name
                  :icon    "school"
                  :actions [{:text     "New Class"
                             :icon     "add"
                             :on-click handle-add-click}]}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [header]
     [page/main-content {:title "Classes"}
      [no-data]]
     [page/side-bar
      [no-data]]]))
