(ns webchange.ui.widgets.file.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/file)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; state

(defn- get-state
  [db uid]
  (get db uid))

(defn- set-state
  [db uid data]
  (assoc db uid data))

(defn- update-state
  [db uid data-patch]
  (update db uid merge data-patch))

(defn- get-language
  [db uid]
  (-> (get-state db uid)
      (get :language)))

(defn- get-change-handler
  [db uid]
  (-> (get-state db uid)
      (get :on-change)))

(re-frame/reg-sub
  ::state
  :<- [path-to-db]
  (fn [db [_ uid]]
    (get-state db uid)))

(defn- set-loading
  [db uid value]
  (update-state db uid {:loading? value}))

(re-frame/reg-sub
  ::loading?
  (fn [[_ uid]]
    (re-frame/subscribe [::state uid]))
  (fn [state]
    (get state :loading? false)))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ uid {:keys [language on-change]}]]
    {:db (set-state db uid {:language  language
                            :on-change on-change})}))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ uid]]
    {:db (dissoc db uid)}))

(re-frame/reg-event-fx
  ::upload-audio
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ uid js-file-value]]
    (let [file-name (.-name js-file-value)
          language (get-language db uid)
          asset-data {:alias file-name
                      :date  (.now js/Date)
                      :lang  language}]
      {:db       (set-loading db uid true)
       :dispatch [::warehouse/upload-file
                  {:file        js-file-value
                   :form-params [["lang" language]]}
                  {:on-success [::upload-audio-success uid asset-data]}]})))

(re-frame/reg-event-fx
  ::upload-audio-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ uid asset-data response]]
    (let [on-change (get-change-handler db uid)
          asset-data (merge response asset-data)]
      {:db       (set-loading db uid false)
       :callback [on-change asset-data]})))
