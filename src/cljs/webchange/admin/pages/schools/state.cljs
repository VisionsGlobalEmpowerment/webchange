(ns webchange.admin.pages.schools.state
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.state :as parent-state]
            [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:schools])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} _]
    (if (nil? (get-in db (path-to-db [])))
      {:dispatch [::warehouse/load-schools
                  {:on-success [::load-schools-success]}]})))

(re-frame/reg-event-fx
  ::load-schools-success
  (fn [{:keys [_]} [_ schools]]
    {:dispatch [::set-schools schools]}))

(re-frame/reg-event-fx
  ::set-schools
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [])
                   (apply merge (map (fn [item]
                                       {(:id item) item})
                                     (:schools value))))}))

(re-frame/reg-event-fx
  ::set-school
  (fn [{:keys [db]} [_ school-id value]]
    {:db (assoc-in db (path-to-db [(js/parseInt school-id)]) value)}))

(re-frame/reg-event-fx
  ::save-schools
  (fn [{:keys [db]} [_ value]]
    ;; save schools on backend #################################
    ))

(re-frame/reg-sub
  ::schools-list
  (fn [db]
    (get-in db (path-to-db []))))
