(ns webchange.admin.pages.students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.students.state :as state]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]))

(defn- header
  [])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let []
      [page/page
       [page/header {:title "School"
                     :icon  "school"}
        [header]]
       [page/main-content
        "Content"]])))
