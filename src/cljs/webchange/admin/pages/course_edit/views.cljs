(ns webchange.admin.pages.course-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.course-edit.state :as state]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--edit-course"}
     [page/header {:title "Edit Course"
                   :icon  "presentation"}]
     [page/main-content
      "Edit Course"]]))
