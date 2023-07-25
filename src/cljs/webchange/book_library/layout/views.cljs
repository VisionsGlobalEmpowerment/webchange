(ns webchange.book-library.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-library.layout.side-menu.views :refer [side-menu]]
    [webchange.page-title.views :refer [page-title]]
    [webchange.i18n.translate :as i18n]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- toolbar
  [{:keys [title toolbar-control]}]
  [:div.toolbar
   [:h1 title]
   (when (some? toolbar-control)
     toolbar-control)])

(defn- get-title
  [sub-title]
  (let [title @(re-frame/subscribe [::i18n/t [:book-library]])]
    (if (some? sub-title)
      (str title " - " sub-title)
      title)))

(defn layout
  [{:keys [class-name show-navigation? show-toolbar? title toolbar-control document-title]
    :or   {show-navigation? true
           show-toolbar?    true}}]
  [:div.book-library-layout
   [side-menu {:show-navigation? show-navigation?}]
   [:div.main-block
    (when show-toolbar?
      [toolbar {:title           title
                :toolbar-control toolbar-control}])
    (into [:div {:class-name (get-class-name {"content"  true
                                              class-name (some? class-name)})}]
          (-> (r/current-component) (r/children)))]
   [page-title {:title (get-title document-title)}]])
