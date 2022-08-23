(ns webchange.utils.name)

(defn fullname
  [user]
  (str (:first-name user) " " (:last-name user)))
