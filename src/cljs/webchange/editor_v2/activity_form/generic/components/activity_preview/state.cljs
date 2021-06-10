(ns webchange.editor-v2.activity-form.generic.components.activity-preview.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.editor :as editor-state]))

(re-frame/reg-event-fx
  ::create-preview
  (fn [{:keys [db]} _]
    (let [course-id (core/current-course-id db)
          scene-id (core/current-scene-id db)
          set-activity-preview (fn [result]
                                 (editor-state/show-frames)
                                 (re-frame/dispatch [::warehouse/set-activity-preview {:course-slug course-id
                                                                                       :scene-slug  scene-id
                                                                                       :preview     (:url result)}]))
          upload-asset (fn [file]
                         (re-frame/dispatch [::assets-events/upload-asset file {:type      "image"
                                                                                :on-finish set-activity-preview}]))]
      (editor-state/hide-frames)
      (app/take-screenshot upload-asset {:extract-canvas? false}))))

(defn create-preview
  []
  (re-frame/dispatch [::create-preview]))
