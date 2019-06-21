(ns webchange.dashboard.classes.class-profile.stubs)

(defn get-rand-scores [] (vec (take (+ 3 (rand-int 3)) (repeatedly #(+ 70 (rand-int 50))))))

(defn scores-stub
  [students]
  (map (fn
         [{:keys [first-name last-name]}]
         {:name   (str first-name " " last-name)
          :values (get-rand-scores)}) students))

(defn profile-stub
  [students]
  {:columns [:first-name "FirstName"
             :last-name "Last Name"
             :start-date "Program start date"
             :last-log-in "Last log-in date"]
   :data    (map (fn
                   [{:keys [id first-name last-name]}]
                   {:id          id
                    :first-name  first-name
                    :last-name   last-name
                    :start-date  "-"
                    :last-log-in "-"}) students)})
