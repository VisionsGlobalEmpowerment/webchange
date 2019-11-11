(ns webchange.editor-v2.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::diagram-mode
  (fn [db]
    (get-in db [:editor-v2 :diagram-mode])))

(re-frame/reg-sub
  ::current-action
  (fn [db]
    (get-in db [:editor-v2 :current-action])))
