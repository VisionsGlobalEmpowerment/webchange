(ns webchange.editor-v2.question.question-form.views-form-diagram
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.question.question-form.diagram.items-factory.nodes-factory :refer [get-diagram-items]]
    [webchange.editor-v2.diagram-utils.diagram-widget :refer [diagram-widget]]
    [webchange.editor-v2.diagram-utils.diagram-model.init :refer [init-diagram-model]]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]))


(defn simple-dialog
  [{:keys [path]}]                                          ;; data coming in is a string
  (let [scene-data @(re-frame/subscribe [::translator-form.scene/scene-data])
        {:keys [nodes links]} (get-diagram-items scene-data path)
        {:keys [engine]} (init-diagram-model :question nodes links {:locked? true})]
    [diagram-widget {:engine engine
                     :zoom?  true}]))

(defn diagram-block
  []
  (let [action @(re-frame/subscribe [::question-form.actions/current-question-action-info])
        path (:question-path action)]
    [:div [simple-dialog {:path path}]]))
