(ns webchange.lesson-builder.widgets.confirm.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.initializer :as initializer]))

(def path-to-db :lesson-builder/confirm)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(initializer/on-init path-to-db
                     #(re-frame/dispatch [::close-confirm-window]))
;; confirm window

(def confirm-window-key :confirm-window)

(defn- get-confirm-window
  [db]
  (get db confirm-window-key))

(re-frame/reg-sub
  ::confirm-window
  :<- [path-to-db]
  #(get-confirm-window %))

(re-frame/reg-event-fx
  ::show-confirm-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [message on-confirm on-cancel title confirm-text cancel-text]}]]
    {:db (assoc db confirm-window-key {:open?      true
                                       :message    message
                                       :title      title
                                       :on-confirm on-confirm
                                       :on-cancel  on-cancel
                                       :confirm-text confirm-text
                                       :cancel-text  cancel-text})}))

(re-frame/reg-event-fx
  ::handle-confirm-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-confirm]} (get-confirm-window db)]
      (cond-> {:db (assoc db confirm-window-key {:open? false})}
              (some? on-confirm) (assoc :dispatch on-confirm)))))

(re-frame/reg-event-fx
  ::close-confirm-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-cancel]} (get-confirm-window db)]
      (cond-> {:db (assoc db confirm-window-key {:open? false})}
              (some? on-cancel) (assoc :dispatch on-cancel)))))

;; message window

(def message-window-key :message-window)

(defn- get-message-window
  [db]
  (get db message-window-key))

(re-frame/reg-sub
  ::message-window
  :<- [path-to-db]
  #(get-message-window %))

(re-frame/reg-event-fx
  ::show-message-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [message on-confirm title]}]]
    {:db (assoc db message-window-key {:open?      true
                                       :message    message
                                       :title      title
                                       :on-confirm on-confirm})}))

(re-frame/reg-event-fx
  ::close-message-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-confirm]} (get-message-window db)]
      (cond-> {:db (assoc db message-window-key {:open? false})}
              (some? on-confirm) (assoc :dispatch on-confirm)))))

(comment
  @(re-frame/subscribe [path-to-db]))
