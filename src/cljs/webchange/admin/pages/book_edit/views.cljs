(ns webchange.admin.pages.book-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.book-edit.state :as state]
    [webchange.admin.widgets.book-info-form.views :refer [book-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.date :refer [date-str->locale-date]]))

(defn- book-form
  [{:keys [book-id]}]
  (let [{:keys [name preview created-at updated-at]} @(re-frame/subscribe [::state/book])
        handle-edit-click #(re-frame/dispatch [::state/edit])
        handle-play-click #(re-frame/dispatch [::state/play])

        form-editable? @(re-frame/subscribe [::state/form-editable?])
        removing? @(re-frame/subscribe [::state/removing?])
        handle-edit-info-click #(re-frame/dispatch [::state/toggle-form-editable])
        handle-save #(re-frame/dispatch [::state/set-form-editable false])
        handle-remove #(re-frame/dispatch [::state/open-books-page])]
    [:div.book-form
     [:div.header
      [:div.info
       name
       [:dl
        [:dt "Created"]
        [:dd (date-str->locale-date created-at)]
        [:dt "Last Edited"]
        [:dd (date-str->locale-date updated-at)]]]
      [:div.actions
       [ui/button {:icon       "system-book"
                   :class-name "play-button"
                   :on-click   handle-play-click}
        "Read"]
       [ui/button {:icon       "edit"
                   :class-name "edit-button"
                   :on-click   handle-edit-click}
        "Edit Activity"]]]

     [:div.book-preview-area
      [:div.book
       [:img {:src "/images/admin/book/spine.png"}]
       [ui/image {:src        preview
                  :class-name "preview"}]
       [:img {:src "/images/admin/book/pages.png"}]]]

     [:div.book-details
      [:h2
       "Book Details"
       [:div.actions
        [ui/button {:icon     "duplicate"
                    :on-click handle-edit-info-click}]
        [ui/button {:icon     (if form-editable? "close" "edit")
                    :on-click handle-edit-info-click}]]]
      [book-info-form {:book-id     book-id
                       :editable?   form-editable?
                       :on-save     handle-save
                       :on-cancel   handle-save
                       :on-remove   handle-remove
                       :class-name  "info-form"}]]]))

(defn page
  [{:keys [book-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/book-loading?])]
      [page/page {:class-name "page--book-edit"}
       [page/content {:class-name "page--book-edit--content"}
        (if loading?
          [ui/loading-overlay]
          [book-form {:book-id book-id}])]])))
