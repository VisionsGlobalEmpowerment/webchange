(ns webchange.editor-v2.activity-form.flipbook.views
  (:require
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.activity-form.flipbook.stage.views :refer [stage-block]]
    [webchange.editor-v2.activity-form.flipbook.pages.views :refer [pages-block]]))

(defn activity-form
  []
  [layout {:show-review? true}
   [:div.book-creator
    [:div.main-content
     [stage-block]
     [pages-block]]]])
