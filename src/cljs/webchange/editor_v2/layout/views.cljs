(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]


    [webchange.ui-framework.layout.views :as ui]
    [webchange.editor-v2.layout.components.course-status.views :refer [review-status]]
    [webchange.editor-v2.sandbox.views :refer [share-button]]
    [webchange.sync-status.views :refer [sync-status]]



    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.layout.common.views :as common]
    [webchange.editor-v2.layout.book.views :as book]
    ))

(defn activity-edit-form
  [{:keys [course-id scene-data]}]
  ;(let [layout (-> (get-activity-type scene-data)
  ;                   (case
  ;                     "book" book/layout
  ;                     "flipbook" book-creator
  ;                     common/layout))]
  ;    [layout {:course-id  course-id
  ;             :scene-data scene-data}])


  ;(r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
  ;  )

  )


(defn layout
  []
  (r/with-let [this (r/current-component)]
    (into [ui/layout {:actions [[sync-status {:class-name "sync-status"}]
                                [review-status]
                                [share-button]]}]
          (r/children this))))