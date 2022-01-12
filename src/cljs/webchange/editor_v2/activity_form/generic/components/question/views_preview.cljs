(ns webchange.editor-v2.activity-form.generic.components.question.views-preview
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.views :refer [object-form]]
    [webchange.editor-v2.activity-form.generic.components.question.state :as state]
    [webchange.interpreter.components :refer [get-scene-objects-data-by-scene-data get-activity-resources]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]))

(defn- question-preview
  [{:keys [scene-data]}]
  (let [id (random-uuid)
        objects (get-scene-objects-data-by-scene-data scene-data)
        resources (get-activity-resources nil scene-data)]
    [:div.preview-stage-wrapper
     ^{:key id}
     [stage {:mode                ::modes/editor
             :scene-data          {:scene-id  (str "question-preview-" id)
                                   :objects   objects
                                   :resources resources}
             :show-loader-screen? false
             :reset-resources?    false}]]))

(defn preview
  []
  (let [scene-data @(re-frame/subscribe [::state/scene-data])]
    [:div.preview
     [question-preview {:scene-data scene-data}]
     [object-form {:scene-data  scene-data
                   :hot-update? false
                   :on-save     (fn [params]
                                  (re-frame/dispatch [::state/update-form-data params]))}]]))
