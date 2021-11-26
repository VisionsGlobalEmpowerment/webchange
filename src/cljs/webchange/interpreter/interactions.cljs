(ns webchange.interpreter.interactions)

(defonce user-interactions-blocked? (atom false))

(defn block-user-interaction
  []
  (when-not @user-interactions-blocked?
    (reset! user-interactions-blocked? true)))

(defn unblock-user-interaction
  []
  (when @user-interactions-blocked?
    (reset! user-interactions-blocked? false)))
