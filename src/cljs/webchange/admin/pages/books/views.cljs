(ns webchange.admin.pages.books.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.books.state :as state]
    [webchange.admin.widgets.books-list.views :refer [books-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.search.views :refer [search]]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- type-selector
  []
  (let [selected-type @(re-frame/subscribe [::state/selected-type])
        books-counter @(re-frame/subscribe [::state/books-counter])]
    [:div {:class-name "type-selector"}
     [:div {:class-name (ui/get-class-name {"type-selector-item"        true
                                            "type-selector-item-active" (= selected-type :visible)})
            :on-click   #(re-frame/dispatch [::state/select-type :visible])}
      "Global library"
      [ui/chip {:counter (:visible books-counter)}]]
     [:div.type-spacer]
     [:div {:class-name (ui/get-class-name {"type-selector-item"        true
                                            "type-selector-item-active" (= selected-type :my)})
            :on-click   #(re-frame/dispatch [::state/select-type :my])}
      "My Books"
      [ui/chip {:counter (:my books-counter)}]]]))

(defn- search-bar
  []
  (let [value @(re-frame/subscribe [::state/search-string])
        handle-change #(re-frame/dispatch [::state/set-search-string %])]
    [search {:value      value
             :on-change  handle-change
             :class-name "books-search-input"}]))

(defn- language-selector
  []
  (let [current-language @(re-frame/subscribe [::state/current-language])
        handle-select-language #(re-frame/dispatch [::state/select-language %])]
    [ui/select {:label     "Language"
                :value     current-language
                :options   language-options
                :on-change handle-select-language}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [books @(re-frame/subscribe [::state/books])
          loading? @(re-frame/subscribe [::state/books-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-book %])
          handle-edit-click #(re-frame/dispatch [::state/edit-book %])

          show-my-global? @(re-frame/subscribe [::state/show-my-global?])]
      [page/single-page {:class-name "page--books"
                         :search     [search-bar]
                         :header     {:title      "Books"
                                      :icon       "book"
                                      :icon-color "blue-2"
                                      :controls   [[type-selector]
                                                   [ui/switch {:class-name "show-global-selector"
                                                               :label      "Show My Global Books"
                                                               :checked?   show-my-global?
                                                               :on-change  #(re-frame/dispatch [::state/set-show-global (not show-my-global?)])}]
                                                   [language-selector]]}}
       [books-list {:data               books
                    :loading?           loading?
                    :on-book-click      handle-card-click
                    :on-edit-book-click handle-edit-click}]])))
