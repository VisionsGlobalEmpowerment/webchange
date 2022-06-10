(ns webchange.admin.pages.books.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.books.state :as state]
    [webchange.admin.widgets.activities-list.views :refer [activities-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [books @(re-frame/subscribe [::state/books])
          loading? @(re-frame/subscribe [::state/books-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-book %])
          handle-edit-click #(re-frame/dispatch [::state/edit-book %])]
      [page/page {:class-name "page--books"}
     [page/header {:title "Books"
                   :icon  "book"}]
     [page/main-content
      [activities-list {:data                   books
                        :loading?               loading?
                        :on-activity-click      handle-card-click
                        :on-edit-activity-click handle-edit-click}]]])))