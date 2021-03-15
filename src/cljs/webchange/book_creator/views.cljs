(ns webchange.book-creator.views
  (:require
    [webchange.book-creator.stage.views :refer [stage-block]]
    [webchange.book-creator.pages.views :refer [pages-block]]
    [webchange.editor-v2.layout.components.activity-action.views :refer [action-modal-container]]))

(defn book-creator
  []
  (let []
    [:div.book-creator
     [:div.main-content
      [stage-block]
      [pages-block]]
     [action-modal-container]]))
