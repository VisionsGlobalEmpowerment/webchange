(ns webchange.editor-v2.activity-form.flipbook.views
  (:require
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.activity-form.common.object-form.views :refer [object-form]]
    [webchange.editor-v2.activity-form.flipbook.stage.views :refer [stage-block]]
    [webchange.editor-v2.activity-form.flipbook.pages.views :refer [pages-block]]
    [webchange.editor-v2.activity-form.generic.components.activity-action.views :refer [activity-action-modal]]))

(defn activity-form
  []
  [layout {:show-review? true}
   [:div.book-creator
    [:div.main-content
     [stage-block
      [object-form]]
     [pages-block]]
    [activity-action-modal]]])
