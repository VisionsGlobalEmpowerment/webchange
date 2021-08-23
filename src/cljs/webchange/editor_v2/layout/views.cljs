(ns webchange.editor-v2.layout.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.layout.views :as ui]
    [webchange.editor-v2.layout.components.course-status.views :refer [review-status]]
    [webchange.editor-v2.layout.components.sandbox.views :refer [share-button]]
    [webchange.editor-v2.layout.components.sync-status.views :refer [sync-status]]))

(defn layout
  [{:keys [show-preview show-review? scene-data]
    :or   {show-preview true
           show-review? false}}]
  (r/with-let [this (r/current-component)]
    (into [ui/layout {:actions   (cond-> [[sync-status {:class-name "sync-status"}]]
                                   show-review? (conj [review-status])
                                   show-preview (conj [share-button]))
                      :scene-data scene-data}]
          (r/children this))))
