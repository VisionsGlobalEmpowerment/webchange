(ns webchange.admin.components.form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :component/form)

;; Form Data

(def from-data-key :form)

(defn- get-form-data
  [db]
  (get db from-data-key {}))

(defn- set-form-data
  [db data]
  (assoc db from-data-key data))

(defn- reset-form-data
  [db]
  (set-form-data db {}))

(defn- update-form-data
  [db data-patch]
  (update db from-data-key merge data-patch))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

;; Init

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys []}]]
    (print "init")
    {}))