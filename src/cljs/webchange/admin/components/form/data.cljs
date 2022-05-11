(ns webchange.admin.components.form.data)

(defn- get-data
  ([data-key db]
   (get-in db [data-key] {}))
  ([data-key db form-id]
   (get-in db [form-id data-key] {})))

(defn- set-data
  ([data-key db data]
   (assoc-in db [data-key] data))
  ([data-key db form-id data]
   (assoc-in db [form-id data-key] data)))

(defn- reset-data
  ([data-key db]
   (set-data data-key db {}))
  ([data-key db form-id]
   (set-data data-key db form-id {})))

(defn- update-data
  ([data-key db data-patch]
   (update-in db [data-key] merge data-patch))
  ([data-key db form-id data-patch]
   (update-in db [form-id data-key] merge data-patch)))

(defn init
  [data-key]
  {:get-data    (partial get-data data-key)
   :set-data    (partial set-data data-key)
   :reset-data  (partial reset-data data-key)
   :update-data (partial update-data data-key)})
