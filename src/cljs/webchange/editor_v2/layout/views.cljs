(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.views :refer [book-creator]]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.layout.common.views :as common]
    [webchange.editor-v2.layout.book.views :as book]
    [webchange.editor-v2.layout.flipbook.views :as flipbook]
    [webchange.editor-v2.layout.utils :refer [get-activity-type]]))

(defn activity-edit-form
  [{:keys [course-id scene-data]}]
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
    (let [layout (-> (get-activity-type scene-data)
                     (case
                       "book" book/layout
                       "flipbook" book-creator
                       common/layout))]
      [layout {:course-id  course-id
               :scene-data scene-data}])))
