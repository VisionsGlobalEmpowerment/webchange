(ns webchange.admin.pages.add-class.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.add-class.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [page/main-content {:title  "Add New Class"}
      [no-data]]]))
