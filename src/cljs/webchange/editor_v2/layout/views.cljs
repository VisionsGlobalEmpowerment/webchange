(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.layout.common.views :as common]
    [webchange.editor-v2.layout.book.views :as book]
    [webchange.editor-v2.layout.utils :refer [get-activity-type]]
    [webchange.subs :as subs]))

(defn activity-edit-form
  [{:keys [course-id]}]
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
          layout (-> (get-activity-type scene-data)
                     (case
                       "book" book/layout
                       "flipbook" book/layout
                       common/layout))]
      [layout {:course-id   course-id
               :scene-data  scene-data}])))
