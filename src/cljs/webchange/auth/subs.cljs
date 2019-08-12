(ns webchange.auth.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::user
  (fn [db]
    (get-in db [:user])))
