(ns webchange.dashboard.students.utils)

(defn- flatten-student-data
  [data]
  (->> (merge data (:user data))
       (filter (comp #{:id
                       :first-name
                       :last-name
                       :email
                       :password
                       :confirm-password
                       :class-id} first))
       (into {})))

(def flatten-student (fnil flatten-student-data {:data {}}))

