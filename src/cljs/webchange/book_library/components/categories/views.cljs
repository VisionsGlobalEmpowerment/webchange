(ns webchange.book-library.components.categories.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.components.categories.state :as state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn category-image
  [{:keys [value]}]
  (let [src @(re-frame/subscribe [::state/category-image value])]
    [:img {:src src}]))

(defn category-item
  [{:keys [image name value selected? on-click]}]
  (let [handle-click #(on-click (if selected? nil {:name  name
                                                   :value value}))]
    [:li {:class-name (get-class-name {"category-item" true
                                       "selected"      selected?})
          :title      name
          :on-click   handle-click}
     [:img {:src image}]]))

(defn book-categories
  []
  (re-frame/dispatch [::state/init])
  (fn [{:keys [value on-change]}]
    (let [categories @(re-frame/subscribe [::state/categories-options])]
      [:ul.book-categories
       (for [category categories]
         ^{:key (:value category)}
         [category-item (merge category
                               {:on-click  on-change
                                :selected? (= value (:value category))})])])))
