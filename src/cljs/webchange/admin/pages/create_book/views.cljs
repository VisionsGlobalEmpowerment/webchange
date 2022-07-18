(ns webchange.admin.pages.create-book.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.admin.pages.create-book.state :as state]
    [webchange.utils.languages :refer [language-options]]))

(defn- authors-input
  []
  (let [authors-values @(re-frame/subscribe [::state/authors])
        authors-number @(re-frame/subscribe [::state/authors-number])
        handle-add #(re-frame/dispatch [::state/add-author])]
    [:div.authors
     [:div.authors-header
      [ui/input-label "Authors"]
      [ui/icon {:icon "plus"
                :on-click handle-add}]]
     (for [idx (range authors-number)]
       ^{:key idx}
       [ui/input {:value (get authors-values idx)
                  :on-change #(re-frame/dispatch [::state/change-author idx %])
                  :placeholder "Add Author Name"}])]))

(defn- illustrators-input
  []
  (let [illustrators-values @(re-frame/subscribe [::state/illustrators])
        illustrators-number @(re-frame/subscribe [::state/illustrators-number])
        handle-add-illustrator #(re-frame/dispatch [::state/add-illustrator])]
    [:div.illustrators
     [:div.illustrators-header
      [ui/input-label "Illustrators"]
      [ui/icon {:icon "plus"
                :on-click handle-add-illustrator}]]
     (for [idx (range illustrators-number)]
       ^{:key idx}
       [ui/input {:value (get illustrators-values idx)
                  :on-change #(re-frame/dispatch [::state/change-illustrator idx %])
                  :placeholder "Add Illustrator Name"}])]))

(defn- info
  []
  (let [title-value @(re-frame/subscribe [::state/title])
        language-value @(re-frame/subscribe [::state/language])
        handle-change-title #(re-frame/dispatch [::state/change-title %])
        handle-change-lang #(re-frame/dispatch [::state/change-lang %])
        errors @(re-frame/subscribe [::state/errors])]
    [:div.info
     [ui/input {:label "Cover Title"
                :value title-value
                :error (:cover-title errors)
                :required? true
                :on-change handle-change-title}]
     [ui/select {:label "Select Language"
                 :value language-value
                 :error (:lang errors)
                 :required? true
                 :options language-options
                 :on-change handle-change-lang}]
     [authors-input]
     [illustrators-input]]))

(defn- change-event->file
  [event]
  (-> event
      (.. -target -files)
      (.item 0)))

(defn- cover-image
  []
  (r/with-let [handle-upload #(re-frame/dispatch [::state/upload-cover %])
               file-input (atom nil)
               uploading? (r/atom false)]
    (let [cover-image-value @(re-frame/subscribe [::state/cover-image])
          errors @(re-frame/subscribe [::state/errors])]
      [:div.cover-image
       [:h3 "Cover image"]
       (when (:cover-image errors)
         [ui/input-error (:cover-image errors)])
       [:div.options
        ^{:key cover-image-value}
        [ui/image {:src cover-image-value}]
        [:input {:type      "file"
                 :accept    ["gif" "jpg" "jpeg" "png"]
                 :on-change #(-> % change-event->file handle-upload)
                 :ref       #(reset! file-input %)}]
        [ui/button {:on-click #(.click @file-input)}
         (if @uploading? "Uploading..." "Upload New")]]])))

(defn- layout
  []
  (let [layout-value @(re-frame/subscribe [::state/layout])
        errors @(re-frame/subscribe [::state/errors])]
    [:div.choose-layout
     [:h3 "Choose Layout"]
     (when (:cover-layout errors)
       [ui/input-error (:cover-layout errors)])
     [:div.options
      [:div.option {:on-click #(re-frame/dispatch [::state/choose-layout "title-top"])}
       [:div.image-wrapper
        [ui/image {:src "/images/admin/create_book/layout_title_top.svg"}]]
       [:div.option-name
        "Title at the top"
        [ui/icon {:icon "check"
                  :color (if (= layout-value "title-top")
                           "blue-1"
                           "grey-4")}]]]
      [:div.option {:on-click #(re-frame/dispatch [::state/choose-layout "title-bottom"])}
       [:div.image-wrapper
        [ui/image {:src "/images/admin/create_book/layout_title_bottom.svg"}]]
       [:div.option-name
        "Title at the bottom"
        [ui/icon {:icon "check"
                  :color (if (= layout-value "title-bottom")
                           "blue-1"
                           "grey-4")}]]]]]))

(defn- note
  []
  [:div.note
   [ui/icon {:icon "info"}]
   [:div "Build your book cover here. Once saved you will build out all the pages in your book."]])

(defn- book-form
  []
  [:div.book-form
   [:div.left-form
    [info]]
   [:div.right-form
    [:div.top-form
     [cover-image]
     [layout]]
    [note]]])

(defn page
  [props]
  (re-frame/dispatch [::state/init])
  (fn []
    (let [handle-build #(re-frame/dispatch [::state/build])
          handle-back #(re-frame/dispatch [::state/back])]
      [page/page {:class-name "page--create-book"}
       [page/header {:title      "Create Book"
                     :icon       "book"
                     :icon-color "blue-2"
                     :class-name "page--create-book--header"}]
       [page/content {:title "Start Book Creation"
                      :icon "edit"
                      :class-name "page--create-book--content"}
        [book-form]]
       [page/footer
        [ui/button {:class-name "footer-button-back"
                    :color "blue-1"
                    :on-click handle-back}
         "Back"]
        [ui/button {:class-name "footer-button-confirm"
                    :on-click handle-build}
         "Save & Build"]]])))
