(ns webchange.admin.pages.book-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.book-edit.state :as state]
    [webchange.admin.widgets.book-info-form.views :refer [book-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.name :refer [fullname]]))

(defn- book-form
  [{:keys [book-id]}]
  (let [{:keys [name preview created-at updated-at
                created-by-user updated-by-user metadata]} @(re-frame/subscribe [::state/book])
        locked? (:locked metadata)
        
        handle-edit-click #(re-frame/dispatch [::state/edit])
        handle-play-click #(re-frame/dispatch [::state/play])
        handle-duplicate-click #(re-frame/dispatch [::state/duplicate])
        
        form-editable? @(re-frame/subscribe [::state/form-editable?])
        removing? @(re-frame/subscribe [::state/removing?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove #(re-frame/dispatch [::state/open-books-page])
        handle-lock #(re-frame/dispatch [::state/set-locked %])]
    [:div.book-form
     [:div.header
      [:div.header-top
       [:div.info name]
       [:div.actions
        [ui/button {:icon       "system/play"
                    :class-name "play-button"
                    :on-click   handle-play-click}
         "Play"]
        (if locked?
          [ui/button {:icon       "duplicate"
                      :class-name "edit-button"
                      :on-click   handle-duplicate-click}
           "Duplicate Book"]
          [ui/button {:icon       "edit"
                      :class-name "edit-button"
                      :on-click   handle-edit-click}
           "Edit Book"])]]
      [:div.header-bottom
       [:div.date-user-info
        "Created by:" [:strong (fullname created-by-user)] " " (date-str->locale-date created-at)]
       [:div.date-user-info
        "Last Edited " [:strong (fullname updated-by-user)] " " (date-str->locale-date updated-at)]]]

     [:div.book-preview-area
      [:div.book
       [:img {:src "/images/admin/book/spine.png"}]
       [ui/image {:src        preview
                  :class-name "preview"}]
       [:img {:src "/images/admin/book/pages.png"}]]]

     [:div.book-details
      [:h2
       "Book Details"
       (when-not locked?
         [:div.actions
          [ui/button {:icon     "duplicate"
                      :on-click handle-duplicate-click}]
          [ui/button {:icon     (if form-editable? "close" "edit")
                      :on-click handle-edit-info-click}]])]
      [book-info-form {:book-id    book-id
                       :editable?  form-editable?
                       :on-save    handle-save
                       :on-cancel  handle-save
                       :on-remove  handle-remove
                       :on-lock    handle-lock
                       :class-name "info-form"}]]]))

(defn page
  [{:keys [book-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/book-loading?])]
      [page/page {:class-name    "page--book-edit"
                  :align-content "center"}
       [page/content {:class-name   "page--book-edit--content"
                      :transparent? true}
        (if loading?
          [ui/loading-overlay]
          [book-form {:book-id book-id}])]])))
