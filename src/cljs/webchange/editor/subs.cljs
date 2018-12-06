(ns webchange.editor.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::screen
  (fn [db]
    (get-in db [:editor :screen])))