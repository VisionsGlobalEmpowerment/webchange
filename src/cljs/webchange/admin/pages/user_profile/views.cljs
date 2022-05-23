(ns webchange.admin.pages.user-profile.views
  (:require
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (fn []
    [page/page {:class-name "page--user-profile"}
     [page/header {:title "User Profile"
                   :icon  "user"}]
     [page/main-content
      [no-data]]]))