(ns webchange.admin.components.form.form-data
  (:require
    [webchange.admin.components.form.data :refer [init]]))

(def data (init :data))
(def get-data (:get-data data))
(def set-data (:set-data data))
(def reset-data (:reset-data data))
(def update-data (:update-data data))
