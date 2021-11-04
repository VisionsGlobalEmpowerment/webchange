(ns webchange.interpreter.events-register
  (:require
    [re-frame.core :as re-frame]
    [webchange.logger.index :as logger]))

(re-frame/reg-event-db
  ::register-animation
  (fn [db [_ name animation]]
    (let [scene-id (:current-scene db)]
      (assoc-in db [:scenes scene-id :animations name] animation))))

(re-frame/reg-event-db
  ::register-transition
  (fn [db [_ name component]]
    (logger/trace-folded "register transition" name component)
    (let [scene-id (:current-scene db)]
      (assoc-in db [:transitions scene-id name] component))))

(re-frame/reg-event-db
  ::unregister-transition
  (fn [db [_ name]]
    (let [scene-id (:current-scene db)]
      (update-in db [:transitions scene-id] dissoc name))))
