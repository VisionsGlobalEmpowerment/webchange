(ns webchange.editor-v2.layout.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.layout.views :as ui]
    [webchange.editor-v2.layout.components.course-status.views :refer [review-status]]
    [webchange.editor-v2.layout.components.sandbox.views :refer [share-button]]
    [webchange.editor-v2.layout.components.sync-status.views :refer [sync-status]]))

;[webchange.editor-v2.components.breadcrumbs.views :refer [course-breadcrumbs]]
;:breadcrumbs (course-breadcrumbs course-id "Scene")

(defn layout
  []
  (r/with-let [this (r/current-component)]
    (into [ui/layout {:actions [[sync-status {:class-name "sync-status"}]
                                [review-status]
                                [share-button]]}]
          (r/children this))))
