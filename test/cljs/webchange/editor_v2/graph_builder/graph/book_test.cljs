(ns webchange.editor-v2.graph-builder.graph.book-test
  (:require
    [cljs.test :refer [deftest testing]]
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.graph.utils :refer [compare-results]]
    [webchange.editor-v2.graph-builder.graph.data.concept-sample :as concept]
    [webchange.editor-v2.graph-builder.graph.data.book.source :as source]
    [webchange.editor-v2.graph-builder.graph.data.book.dialog--read-page :as read-page]
    [webchange.editor-v2.graph-builder.graph.data.book.dialog--read-title :as read-title]))

(let [diagram-mode :translation
      scene-data source/data
      params {:concept-data {:current-concept concept/data}}]

  (deftest test-get-dialog-graph--book--read-page
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :read-page-current-concept))
                     read-page/data))

  (deftest test-get-dialog-graph--book--read-title
    (compare-results (get-diagram-graph scene-data diagram-mode
                                        (assoc params :start-node :read-title))
                     read-title/data)))
