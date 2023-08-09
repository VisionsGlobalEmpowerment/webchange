(ns webchange.lesson-builder.layout.menu.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.utils.uid :refer [get-uid]]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]))

(def path-to-db :lesson-builder/menu)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; history

(def history-key :history)

(defn- reset-history
  [db]
  (assoc db history-key []))

(defn- get-history
  [db]
  (get db history-key))

(defn- get-current-component
  [db]
  (-> (get-history db)
      (last)
      (get :component-id)))

(defn- get-current-handlers
  [db]
  (-> (get-history db)
      (last)
      (select-keys [:on-back])))

(defn- push-history
  ([db component-id]
   (push-history db component-id {}))
  ([db component-id {:keys [on-back title]}]
   (let [uid (get-uid)
         events-list? (-> on-back first vector?)]
     (->> (cond-> {:uid          uid
                   :component-id component-id}
                  (some? on-back) (assoc :on-back (if events-list? on-back [on-back]))
                  (some? title) (assoc :title title))
          (update db history-key conj)))))

(defn- pop-history
  [db]
  (update db history-key #(-> % butlast vec)))

(re-frame/reg-sub
  ::history
  :<- [path-to-db]
  #(get-history %))

;; subs

(re-frame/reg-sub
  ::open-components
  :<- [::history]
  (fn [history]
    (let [last-idx (-> history count dec)]
      (->> history
           (map-indexed (fn [idx history-item]
                          (-> history-item
                              (select-keys [:component-id :uid])
                              (assoc :hidden? (< idx last-idx)))))))))

(re-frame/reg-sub
  ::show-history-back?
  :<- [::history]
  (fn [history]
    (-> (count history)
        (> 1))))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [history (get-history db)]
      (cond-> {}
              (empty? history) (assoc :db (-> db
                                              (reset-history)
                                              (push-history :default)))))))

(re-frame/reg-event-fx
  ::open-component
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ component-id {:keys [on-back reset-history? title] :or {reset-history? false}}]]
    (let [current-component (get-current-component db)]
      {:db (cond-> db
                   reset-history? (reset-history)
                   (not= component-id current-component) (push-history component-id {:title   title
                                                                                     :on-back on-back}))})))

(re-frame/reg-event-fx
  ::history-back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-back]} (get-current-handlers db)]
      (cond-> {:db (-> db (pop-history))}
              (seq on-back) (assoc :dispatch-n on-back)))))

(re-frame/reg-sub
  ::undo-active?
  (fn [db]
    (state/undo-available? db)))

(re-frame/reg-event-fx
  ::undo
  (fn [{:keys [_db]} [_]]
    {:dispatch [::stage-actions/undo]}))
