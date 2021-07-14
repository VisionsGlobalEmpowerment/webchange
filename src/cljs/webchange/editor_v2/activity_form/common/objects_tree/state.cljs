(ns webchange.editor-v2.activity-form.common.objects-tree.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.state.state :as state]))

(re-frame/reg-sub
  ::objects
  (fn []
    [(re-frame/subscribe [::state/objects-data])])
  (fn [[objects-data]]
    (->> objects-data
         (filter (fn [[_ {:keys [editable?]}]]
                   (:show-in-tree? editable?)))
         (map (fn [[object-name {:keys [alias]}]]
                (let []
                  {:alias (or alias object-name)
                   :name  object-name}))))))

(re-frame/reg-sub
  ::object-data
  (fn []
    [(re-frame/subscribe [::state/objects-data])])
  (fn [[objects-data] [_ object-name]]
    (get objects-data object-name)))

(re-frame/reg-sub
  ::visible?
  (fn [[_ object-name]]
    [(re-frame/subscribe [::object-data object-name])])
  (fn [[object-data]]
    (get object-data :visible)))

(re-frame/reg-event-fx
  ::set-object-visibility
  (fn [{:keys [_]} [_ object-name visible?]]
    {:dispatch [::state/update-scene-object
                {:object-name       object-name
                 :object-data-patch {:visible visible?}}
                {:on-success [::update-scene-object-success {:object-name object-name
                                                             :visible?    visible?}]}]}))

(re-frame/reg-event-fx
  ::update-scene-object-success
  (fn [{:keys [_]} [_ {:keys [object-name visible?]}]]
    {:dispatch [::state-renderer/change-scene-object object-name [[:set-visibility {:visible visible?}]]]}))
