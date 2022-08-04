(ns webchange.lesson-builder.tools.object-form.image-form.state
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
                    (or {:x 1 :y 1})
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
  ::image-size-value
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :image-size])))

(re-frame/reg-event-fx
  ::set-image-size
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    (let [image-size-value (name value)]
      {:db (-> db
               (assoc-in [:objects object-name :image-size] value))
       :dispatch [::state-renderer/set-scene-object-state object-name {:image-size value}]})))

(re-frame/reg-sub
  ::src-value
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :src])))

(re-frame/reg-event-fx
  ::set-image-src
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (-> db
             (assoc-in [:objects object-name :src] value))
     :dispatch [::state-renderer/set-scene-object-state object-name {:src value}]}))

(re-frame/reg-sub
  ::visible-value
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :visible])))

(re-frame/reg-event-fx
  ::set-visible
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ object-name value]]
    {:db (-> db
             (assoc-in [:objects object-name :visible] value))
     :dispatch [::state-renderer/set-scene-object-state object-name {:visible value}]}))

(re-frame/reg-sub
  ::options
  :<- [path-to-db]
  (fn [db [_ object-name]]
    (get-in db [:objects object-name :editable? :edit-form])))

(defn- get-object-keys-by-tag
  "Return object names for each object in scene with given tag"
  [activity-data tag]
  (let [objects (:objects activity-data)]
    (->> objects
         (filter (fn [[object-name object]]
                   (some #{tag} (get-in object [:editable? :edit-tags]))))
         (map first))))

(re-frame/reg-event-fx
  ::apply-to-all
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_ object-name]]
    (let [state (-> db
                    (get-in [:objects object-name])
                    (select-keys [:src :scale]))
          tag (-> db (get-in [:objects object-name :editable? :edit-tags]) (first))
          object-keys (get-object-keys-by-tag activity-data tag)
          objects (as-> (:objects activity-data) o
                        (select-keys o object-keys)
                        (map (fn [[key object]]
                               [key (merge object state)]) o)
                        (into {}))
          render-events (map (fn [object-key]
                               [::state-renderer/set-scene-object-state object-key state])
                             object-keys)]
      {:db (update db :objects merge objects)
       :dispatch-n render-events})))
