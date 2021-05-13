(ns webchange.editor-v2.activity-form.flipbook.views
  (:require
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.activity-form.flipbook.asset-form.views :refer [asset-form]]
    [webchange.editor-v2.activity-form.flipbook.stage.views :refer [stage-block]]
    [webchange.editor-v2.activity-form.flipbook.pages.views :refer [pages-block]]
    [webchange.editor-v2.layout.components.activity-action.views :refer [action-modal-container]]))

(defn activity-form
  []
  [layout
   [:div.book-creator
    [:div.main-content
     [stage-block
      [asset-form]]
     [pages-block]]
    [action-modal-container]]])
