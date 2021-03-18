(ns webchange.book-creator.views
  (:require
    [webchange.book-creator.stage.views :refer [stage-block]]
    [webchange.book-creator.pages.views :refer [pages-block]]
    [webchange.book-creator.text-form.views :refer [text-form]]
    [webchange.editor-v2.layout.components.activity-action.views :refer [action-modal-container]]))

(defn book-creator
  []
  (let []
    [:div.book-creator
     [:div.main-content
      [stage-block
       [text-form]]
      [pages-block]]
     [action-modal-container]]))
