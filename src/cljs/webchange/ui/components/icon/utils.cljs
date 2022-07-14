(ns webchange.ui.components.icon.utils)

(defn with-prefix
  [prefix data]
  (reduce (fn [data [name value]]
            (assoc data (str prefix "/" name) value))
          data
          data))
