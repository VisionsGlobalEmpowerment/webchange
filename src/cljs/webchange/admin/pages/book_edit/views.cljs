(ns webchange.admin.pages.book-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.book-edit.state :as state]
    [webchange.admin.widgets.book-info-form.views :refer [book-info-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]
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
        handle-remove-book #(re-frame/dispatch [::state/remove])
        handle-remove-click #(ui/with-confirmation {:message    "Remove book?"
                                                    :on-confirm handle-remove-book})]
    [:div.book-form
     [:h1
      name
      [:dl
       [:dt "Created"]
       [:dd (date-str->locale-date created-at)]
       [:dt "Last Edited"]
       [:dd (date-str->locale-date updated-at)]]]
     [ui/image {:src        preview
                :class-name "preview"}
      [:div.actions
       [ui/icon-button {:icon     "read"
                        :on-click handle-play-click}
        "Read"]
       [ui/icon-button {:icon     "edit"
                        :on-click handle-edit-click}
        "Edit book"]]]

     [:h2
      "Book Details"
      [ui/icon-button {:icon     (if form-editable? "close" "edit")
                       :on-click handle-edit-info-click
                       :variant  "light"}]]
     [book-info-form {:book-id book-id
                      :editable?   form-editable?
                      :on-save     handle-save
                      :class-name  "info-form"}]
     (when-not form-editable?
       [ui/icon-button {:icon       "remove"
                        :variant    "light"
                        :class-name "remove-button"
                        :loading?   removing?
                        :on-click   handle-remove-click}
        "Delete Book"])]))

(defn page
  [{:keys [book-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/book-loading?])]
      [page/page {:class-name "page--book-edit"}
       [page/header {:title "book"
                     :icon  "book"}]
       [page/main-content {:class-name "page--book-edit--content"}
        (if loading?
          [ui/loading-overlay]
          [book-form {:book-id book-id}])]])))
