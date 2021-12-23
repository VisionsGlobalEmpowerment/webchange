(ns webchange.editor-v2.wizard.activity-template.question.views-preview
  (:require
    [webchange.interpreter.components :refer [get-scene-objects-data-by-scene-data get-activity-resources]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.stage :refer [stage]]))

(defn question-preview
  [{:keys [scene-data]}]
  [:div.preview-stage-wrapper
   (let [id (random-uuid)
         objects (get-scene-objects-data-by-scene-data scene-data)
         resources (get-activity-resources nil scene-data)]
     ^{:key id}
     [stage {:mode                ::modes/editor
             :scene-data          {:scene-id  (str "question-preview-" id)
                                   :objects   objects
                                   :resources resources}
             :show-loader-screen? false
             :reset-resources?    false}])])
