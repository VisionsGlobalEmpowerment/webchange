(ns webchange.state.state-course
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db [relative-path] (concat [:course] relative-path))

;; Course Data

(def course-data-path (path-to-db [:course-data]))

(defn get-course-data
  [db]
  (get-in db course-data-path))

(re-frame/reg-sub
  ::course-data
  (fn [db]
    (get-in db course-data-path {})))

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course-data]]
    {:db (assoc-in db course-data-path course-data)}))

(re-frame/reg-event-fx
  ::load-course-data
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-course course-slug {:on-success [::set-course-data]}]}))

;; Course Info

(def course-info-path (path-to-db [:course-info]))

(re-frame/reg-sub
  ::course-info
  (fn [db]
    (get-in db course-info-path {})))

(re-frame/reg-event-fx
  ::load-course-info
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-course-info course-slug {:on-success [::set-course-info]}]}))

(re-frame/reg-event-fx
  ::set-course-info
  (fn [{:keys [db]} [_ course-info]]
    {:db (assoc-in db course-info-path course-info)}))

(re-frame/reg-event-fx
  ::set-course-status
  (fn [{:keys [db]} [_ status]]
    {:db (assoc-in db (concat course-info-path [:status]) status)}))

;; Course  status

(re-frame/reg-event-fx
  ::publish-course
  (fn [{:keys [db]} [_ course-slug]]
    (let [course-slug (or course-slug (core/current-course-id db))]
      {:dispatch [::warehouse/publish-course course-slug {:on-success [::set-course-info]}]})))
