(ns webchange.lesson-builder.tools.template-options.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.state :as layout-state]
    [webchange.lesson-builder.tools.stage-actions :as stage]
    [webchange.utils.scene-data :refer [get-scene-background]]))

(def path-to-db :lesson-builder/template-options)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-template-options
  [activity-data]
  (-> activity-data
      :metadata
      :actions
      :template-options
      :options))

(defn get-saved-props
  [activity-data]
  (-> activity-data
      :metadata
      :saved-props
      :template-options))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_]]
    (let [options (get-template-options activity-data)
          saved-props (get-saved-props activity-data)          ]
      {:db (-> db
               (assoc :options options)
               (assoc :saved-props saved-props)
               (assoc :form saved-props))})))

(re-frame/reg-sub
  ::options
  :<- [path-to-db]
  #(get % :options))

(re-frame/reg-sub
  ::saved-props
  :<- [path-to-db]
  #(get % :saved-props))

(re-frame/reg-sub
  ::saved-field-value
  :<- [::saved-props]
  (fn [saved-props [_ key]]
    (get saved-props (keyword key))))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get % :form))

(re-frame/reg-sub
  ::field-value
  :<- [::form-data]
  (fn [data [_ key]]
    (get data (keyword key))))

(re-frame/reg-event-fx
  ::set-field
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key value]]
    {:db (assoc-in db [:form (keyword key)] value)}))

(defn handle-change-field
  [key]
  (fn [value]
    (re-frame/dispatch [::set-field key value])))
