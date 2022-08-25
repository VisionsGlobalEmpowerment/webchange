(ns webchange.student.pages.sign-in.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.events :as ie]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.numbers :refer [try-parse-number]]))

(def code-length 4)

(def path-to-db :student-sign-in/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; school id

(def school-id-key :school-id)

(defn- get-school-id
  [db]
  (get db school-id-key))

(defn- set-school-id
  [db value]
  (assoc db school-id-key value))

(re-frame/reg-event-fx
  ::set-school-id
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-id]]
    {:db (-> db (set-school-id school-id))}))

;; current value

(def current-value-key :current-value)

(defn- get-current-value
  [db]
  (get db current-value-key []))

(defn- set-current-value
  [db value]
  (assoc db current-value-key value))

(re-frame/reg-sub
  ::current-value
  :<- [path-to-db]
  get-current-value)

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db
             (set-school-id school-id)
             (set-current-value []))}))

(re-frame/reg-sub
  ::code
  :<- [::current-value]
  (fn [current-value]
    (concat current-value
            (->> (repeat nil)
                 (take (- code-length
                          (count current-value)))))))

(re-frame/reg-event-fx
  ::enter-value
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [current-value (get-current-value db)]
      (when (< (count current-value) code-length)
        (let [new-value (-> current-value
                            (conj value)
                            (vec))]
          (cond-> {:db (-> db (set-current-value new-value))}
                  (= (count new-value) code-length) (assoc :dispatch [::sign-in])))))))

(re-frame/reg-event-fx
  ::remove-value
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [current-value (get-current-value db)]
      (when (> (count current-value) 0)
        (let [new-value (-> current-value
                            (butlast)
                            (vec))]
          {:db (-> db (set-current-value new-value))})))))

(re-frame/reg-event-fx
  ::sign-in
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [code (->> (get-current-value db)
                    (clojure.string/join ""))
          school-id (-> (or (get-school-id db))
                        (try-parse-number))]
      {:dispatch [::warehouse/student-login
                  {:access-code code
                   :school-id   school-id}
                  {:on-success [::sign-in-success]}]})))

(re-frame/reg-event-fx
  ::sign-in-success
  (fn [{:keys [db]} [_ user]]
    {:db       (-> db
                   (assoc-in [:current-course] (:course-slug user))
                   (update-in [:user] merge user))
     :dispatch [::ie/open-student-dashboard]}))

(re-frame/reg-sub
  ::user
  (fn [db]
    (get-in db [:user])))
