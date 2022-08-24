(ns webchange.admin.widgets.books-list.views
  (:require
    [webchange.ui.index :as ui]))

(defn- books-list-item
  [{:keys [id name preview on-click on-edit-click library-type]}]
  (let [handle-card-click #(on-click id)
        handle-edit-click #(do (.stopPropagation %)
                               (on-edit-click id))]
    [:div {:class-name "books-list-item"
           :on-click   handle-card-click}
     [:div.book-preview-area
      [:div.book
       [:img {:src "/images/admin/book/spine_small.png"}]
       [ui/image {:src        preview
                  :class-name "preview"
                  :lazy?      true}]
       [:img {:src "/images/admin/book/pages_small.png"}]]]
     (when library-type
       [ui/icon {:class-name "global-icon"
                 :icon "global"
                 :color (if (= "global" library-type)
                          "blue-1"
                          "yellow-1")}])
     [:div.data
      name
      [ui/button {:icon     "edit"
                  :on-click handle-edit-click}]]]))

(defn books-list
  [{:keys [data loading? on-book-click on-edit-book-click]}]
  [:div.widget--books-list
   (if loading?
     [ui/loading-overlay]
     [:div.books-list
      (for [{:keys [id] :as book} data]
        ^{:key id}
        [books-list-item (merge book
                                {:on-click      on-book-click
                                 :on-edit-click on-edit-book-click})])])])
