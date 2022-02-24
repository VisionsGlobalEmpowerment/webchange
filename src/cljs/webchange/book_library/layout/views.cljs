(ns webchange.book-library.layout.views
  (:require
    [reagent.core :as r]
    [webchange.book-library.layout.icons.index :refer [icons]]
    [webchange.book-library.layout.side-menu.views :refer [side-menu]]
    [webchange.page-title.views :refer [page-title]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- toolbar
  [{:keys [title]}]
  [:div.toolbar
   [:h1 title]])

(defn- get-title
  [sub-title]
  (let [title "Book Library"]
    (if (some? sub-title)
      (str title " - " sub-title)
      title)))

(defn layout
  [{:keys [class-name show-navigation? show-toolbar? title document-title]
    :or   {show-navigation? true
           show-toolbar?    true}}]
  [:div.book-library-layout
   [side-menu {:show-navigation? show-navigation?}]
   [:div.main-block
    (when show-toolbar?
      [toolbar {:title title}])
    (into [:div {:class-name (get-class-name {"content"  true
                                              class-name (some? class-name)})}]
          (-> (r/current-component) (r/children)))]
   [page-title {:title (get-title document-title)}]])
