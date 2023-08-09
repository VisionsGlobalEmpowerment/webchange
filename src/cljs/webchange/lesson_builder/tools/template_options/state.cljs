(ns webchange.lesson-builder.tools.template-options.state
  (:require
    [clojure.walk :as w]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.utils.alphabets :as alphabets]
    [webchange.utils.uid :refer [get-uid]]))

(def path-to-db :lesson-builder/template-options)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn get-template-options
  [activity-data]
  (->> activity-data
       :metadata
       :actions
       :template-options
       :options
       (map (fn [option-data]
              (assoc option-data :uid (get-uid))))))

(defn get-saved-props
  [activity-data]
  (-> activity-data
      :metadata
      :saved-props
      :template-options))

(defn- linked-value?
  [x]
  (and (map? x) (:linked-object x) (:linked-attribute x)))

(defn get-form-data
  [activity-data]
  (let [value (fn [x] (get-in activity-data [:objects (keyword (:linked-object x)) (keyword (:linked-attribute x))]))]
    (->> activity-data
         :metadata
         :saved-props
         :template-options
         (w/prewalk (fn [x] (if (linked-value? x) (value x) x))))))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (re-frame/inject-cofx :activity-info)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data activity-info]} [_]]
    (let [options (get-template-options activity-data)
          saved-props (get-saved-props activity-data)
          form-data (get-form-data activity-data)]
      {:db (-> db
               (assoc :lang (:lang activity-info))
               (assoc :options options)
               (assoc :saved-props saved-props)
               (assoc :form form-data)
               (assoc :overlays []))})))

(re-frame/reg-sub
  ::options
  :<- [path-to-db]
  #(get % :options))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get % :form))

(re-frame/reg-sub
  ::field-value
  :<- [::form-data]
  (fn [data [_ key]]
    (get data (keyword key))))

(defn render-prop
  [saved-props key value]
  (let [src? (and (map? value) (:src value))
        path (cond-> key
                     (string? key) (keyword)
                     (not (vector? key)) (vector)
                     src? (conj :src))
        saved-value (get-in saved-props path)]
    (when (linked-value? saved-value)
      {:dispatch [::state-renderer/set-scene-object-state
                  (-> saved-value :linked-object keyword)
                  {(-> saved-value :linked-attribute keyword) (if src? (:src value) value)}]})))

(re-frame/reg-event-fx
  ::set-field
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key value]]
    (let [saved-props (:saved-props db)
          render-prop-event (render-prop saved-props key value)]
      (merge {:db (assoc-in db [:form (keyword key)] value)}
             render-prop-event))))

(defn handle-change-field
  [key]
  (fn [value]
    (re-frame/dispatch [::set-field key value])))

;; apply

(def loading-key :loading?)

(defn- set-loading
  [db value]
  (assoc db loading-key value))

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [template-options (get-in db [:form])]
      {:db       (set-loading db true)
       :dispatch [::lesson-builder-state/save-activity
                  {:on-success [::stage-actions/apply-template-options
                                template-options
                                {:on-success [::apply-success]
                                 :on-failure [::apply-failure]}]}]})))

(re-frame/reg-event-fx
  ::apply-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-loading db false)
     :dispatch [::menu-state/history-back]}))

(re-frame/reg-event-fx
  ::apply-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-loading db false)}))

(re-frame/reg-event-fx
  ::update-template
  [(i/path path-to-db)]
  (fn [{:keys []} [_]]
    {:dispatch-n [[::lesson-builder-state/update-template]
                  [::menu-state/history-back]]}))

(re-frame/reg-sub
  ::letter-options
  :<- [path-to-db]
  (fn [db [_]]
    (let [lang (:lang db)]
      (alphabets/options-for lang))))

(re-frame/reg-event-fx
  ::reset-stage
  [(i/path path-to-db)]
  (fn [{:keys []} [_]]
    {:dispatch-n [[::stage-state/reset]]}))
