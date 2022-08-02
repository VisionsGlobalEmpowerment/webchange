(ns webchange.lesson-builder.tools.object-form.animation-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.tools.object-form.state :as object-form-state :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    (let [scale (-> (get-in db [:objects object-name :scale :x])
                    (js/Math.abs))]
      {:db (assoc-in db [:values object-name :scale] scale)})))

(re-frame/reg-event-fx
  ::flip
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name]]
    (let [scale (-> db
                    (get-in [:objects object-name :scale])
                    (update :x #(* -1 %)))]
      {:db (assoc-in db [:objects object-name :scale] scale)
       :dispatch [::state-renderer/set-scene-object-state object-name {:scale scale}]})))

(re-frame/reg-sub
  ::scale-value
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:values object-name :scale])))

(re-frame/reg-event-fx
  ::set-scale
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    (if (< 0 (js/Number.parseFloat value))
      (let [flip (-> db (get-in [:objects object-name :scale :x]) (< 0))
            scale {:x (if flip (- value) value)
                   :y value}]
        {:db (-> db
                 (assoc-in [:values object-name :scale] value)
                 (assoc-in [:objects object-name :scale] scale))
         :dispatch [::state-renderer/set-scene-object-state object-name {:scale scale}]})
      {:db (assoc-in db [:values object-name :scale] value)})))

(re-frame/reg-sub
  ::character-value
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name])))

(re-frame/reg-event-fx
  ::set-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name {:keys [change-skeleton? name skin-params] :as value}]]
    (let [object-state (cond-> {}
                               change-skeleton? (assoc :name name)
                               (string? skin-params) (assoc :skin skin-params)
                               (map? skin-params) (assoc :skin-names skin-params))]
      {:db (-> db
               (update-in [:objects object-name] merge object-state))
       :dispatch [::state-renderer/set-scene-object-state object-name object-state]})))
