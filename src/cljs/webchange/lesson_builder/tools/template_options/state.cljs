(ns webchange.lesson-builder.tools.template-options.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as lesson-builder-state]
    [webchange.lesson-builder.blocks.menu.state :as menu-state]
    [webchange.utils.alphabets :as alphabets]))

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
   (re-frame/inject-cofx :activity-info)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data activity-info]} [_]]
    (let [options (get-template-options activity-data)
          saved-props (get-saved-props activity-data)]
      {:db (-> db
               (assoc :lang (:lang activity-info))
               (assoc :options options)
               (assoc :saved-props saved-props)
               (assoc :form saved-props)
               (assoc :overlays []))})))

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

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [template-options (get-in db [:form])]
      {:dispatch-n [[::lesson-builder-state/apply-template-options template-options]
                    [::menu-state/history-back]]})))

(re-frame/reg-event-fx
  ::update-template
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch-n [[::lesson-builder-state/update-template]
                  [::menu-state/history-back]]}))

(re-frame/reg-sub
  ::letter-options
  :<- [path-to-db]
  (fn [db [_]]
    (let [lang (:lang db)]
      (alphabets/options-for lang))))

(defn- show-last-overlay-only
  [overlays]
  (if (seq overlays)
    (let [last-index (-> (count overlays) dec)]
      (as-> overlays o
            (map #(assoc % :hidden true) o)
            (into [] o)
            (update o last-index assoc :hidden false)))
    overlays))

(re-frame/reg-sub
  ::opened-overlays
  :<- [path-to-db]
  (fn [db]
    (let [overlays (get db :overlays)]
      (->> overlays
           (remove nil?)
           (show-last-overlay-only)))))

(re-frame/reg-event-fx
  ::show-overlay
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ label key]]
    {:db (-> db
             (update :overlays concat [{:key key
                                        :label label}]))
     :dispatch [::menu-state/on-back [::close-overlay]]}))

(re-frame/reg-event-fx
  ::close-overlay
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (update :overlays drop-last))}))
