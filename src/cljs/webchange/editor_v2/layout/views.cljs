(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.creation-progress.state :as progress-state]
    [webchange.editor-v2.layout.common.views :as common]
    [webchange.editor-v2.state :as state]
    [webchange.subs :as subs]))

(defn- get-activity-type
  [scene-data]
  (get-in scene-data [:metadata :template-name]))

(defn activity-edit-form
  [{:keys [course-id]}]
  (r/with-let [_ (re-frame/dispatch [::progress-state/show-translation-progress])]
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
          fullscreen? @(re-frame/subscribe [::state/diagram-fullscreen?])
          layout (-> (get-activity-type scene-data)
                     (case
                       common/layout))]
      [layout {:course-id   course-id
               :scene-data  scene-data
               :fullscreen? fullscreen?}])))
