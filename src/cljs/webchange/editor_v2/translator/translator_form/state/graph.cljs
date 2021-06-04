(ns webchange.editor-v2.translator.translator-form.state.graph
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-graph]]))

(re-frame/reg-sub
  ::graph-data
  (fn []
    [(re-frame/subscribe [::translator-form.scene/scene-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.actions/current-dialog-action-name])])
  (fn [[scene-data current-concept current-dialog-action-name]]
    (get-graph {:scene-data scene-data
                :concept    current-concept
                :start-node {:origin-name current-dialog-action-name}})))

(re-frame/reg-sub
  ::graph
  (fn []
    [(re-frame/subscribe [::graph-data])])
  (fn [[graph]]
    (:data graph)))

(re-frame/reg-sub
  ::concept-required
  (fn []
    [(re-frame/subscribe [::graph-data])
     (re-frame/subscribe [::translator-form.concepts/has-concepts?])])
  (fn [[graph course-has-concepts?]]
    (and course-has-concepts? (:has-concepts? graph))))
