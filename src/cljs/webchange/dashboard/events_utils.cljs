(ns webchange.dashboard.events-utils)

(defn when-valid
  [entity-type co-effects event-effect]
  (let [{:keys [db validation-errors]} co-effects]
    (if-not validation-errors
      event-effect
      {:db (assoc-in db
                     (conj [:errors] entity-type)
                     validation-errors)})))

(defn clear-errors
  [db entity-type]
  (update-in db [:errors] dissoc entity-type))