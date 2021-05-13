(ns webchange.editor-v2.activity-form.views
  (:require
    [webchange.editor-v2.activity-form.flipbook.views :as flipbook]
    [webchange.editor-v2.activity-form.generic.views :as generic]))

(defn- get-activity-type
  [scene-data]
  (get-in scene-data [:metadata :template-name]))

(defn activity-form
  [{:keys [scene-data]}]
  (let [activity-type (get-activity-type scene-data)
        form-props {:scene-data scene-data}]
    (case activity-type
      "flipbook" [flipbook/activity-form form-props]
      [generic/activity-form form-props])))

;    ;(when (some? scene-data)
;    ;  [layout {:breadcrumbs (course-breadcrumbs course-id "Scene")
;    ;           :actions     [[sync-status {:class-name "sync-status"}]
;    ;                         [review-status]
;    ;                         [share-button]]}
;    ;   [activity-edit-form {:course-id  course-id
;    ;                        :scene-data scene-data}]])
