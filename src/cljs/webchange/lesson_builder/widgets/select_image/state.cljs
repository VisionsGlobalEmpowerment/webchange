(ns webchange.lesson-builder.widgets.select-image.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.lesson-builder.layout.menu.state :as menu-state]))

(def path-to-db :widgets/select-image)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key {:keys [on-change]}]]
    {:db (assoc-in db [key :on-change] on-change)}))

(re-frame/reg-sub
  ::uploading?
  :<- [path-to-db]
  (fn [db [_ key]]
    (get-in db [key :uploading])))

(re-frame/reg-event-fx
  ::upload
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key file]]
    {:db       (assoc-in db [key :uploading] true)
     :dispatch [::warehouse/upload-file
                {:file        file
                 :form-params [["type" "image"]]}
                {:on-success [::upload-success key]
                 :on-failure [::upload-failure key]}]}))

(re-frame/reg-event-fx
  ::upload-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key {:keys [url] :as result}]]
    (let [on-change (get-in db [key :on-change] #())]
      (on-change result))
    {:db (-> db
             (assoc-in [key :uploading] false))}))

(re-frame/reg-event-fx
  ::upload-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key result]]
    {:db (assoc-in db [key :uploading] false)}))

(re-frame/reg-sub
  ::show-choose-image?
  :<- [path-to-db]
  (fn [db]
    (get db :show-choose-image?)))

(re-frame/reg-event-fx
  ::show-choose-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ key]]
    {:db       (-> db
                   (assoc :current-key key))
     :dispatch [::menu-state/open-component :choose-image]}))

(re-frame/reg-event-fx
  ::select-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [image]}]]
    (let [current-key (get db :current-key)
          on-change (get-in db [current-key :on-change] #())]
      (on-change {:url (:path image)
                  :tags (:tags image)})
      {:dispatch [::menu-state/history-back]})))
