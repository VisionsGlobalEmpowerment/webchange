(ns webchange.admin.pages.books.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.books.state :as state]
    [webchange.admin.widgets.books-list.views :refer [books-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [books @(re-frame/subscribe [::state/books])
          loading? @(re-frame/subscribe [::state/books-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-book %])
          handle-edit-click #(re-frame/dispatch [::state/edit-book %])
          current-language @(re-frame/subscribe [::state/current-language])
          handle-select-language #(re-frame/dispatch [::state/select-language %])]
      [page/single-page {:class-name "page--books"
                         :header     {:title      "Books"
                                      :icon       "book"
                                      :icon-color "blue-2"
                                      :controls   [[ui/select {:label     "Language"
                                                               :value     current-language
                                                               :options   language-options
                                                               :on-change handle-select-language}]]}}
       [books-list {:data               books
                    :loading?           loading?
                    :on-book-click      handle-card-click
                    :on-edit-book-click handle-edit-click}]])))
