(ns webchange.lesson-builder.tools.template-options.select-book.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.select-book.state :as state]
    [webchange.ui.index :as ui]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [label] :as props}]
    (let [book-options @(re-frame/subscribe [::state/book-options])
          book-value @(re-frame/subscribe [::state/selected-book])
          loading? @(re-frame/subscribe [::state/books-loading?])
          select-book #(re-frame/dispatch [::state/select-book %])]
      [:div.select-book
       [ui/select {:label label
                   :type "int"
                   :value book-value
                   :required? true
                   :disabled? loading?
                   :options book-options
                   :on-change select-book}]])))
