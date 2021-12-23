(ns webchange.editor-v2.activity-form.common.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.state-utils :refer [prepare-selected-objects]]
    [webchange.editor-v2.state :as state-editor]
    [webchange.state.state :as state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:activity-form])
       (state-editor/path-to-db)))

(def selected-objects-path (path-to-db [:selected-objects]))

(re-frame/reg-sub
  ::selected-objects-paths
  (fn [db]
    (get-in db selected-objects-path [])))

(re-frame/reg-sub
  ::selected-objects
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::selected-objects-paths])])
  (fn [[scene-data selected-objects-paths]]
    (prepare-selected-objects {:objects-names selected-objects-paths
                               :scene-data    scene-data})))

(re-frame/reg-event-fx
  ::select-objects
  (fn [{:keys [db]} [_ selected-objects]]
    {:db (assoc-in db selected-objects-path selected-objects)}))

(re-frame/reg-event-fx
  ::reset-selection
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db selected-objects-path [])}))
