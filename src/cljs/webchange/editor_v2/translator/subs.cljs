(ns webchange.editor-v2.translator.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::translator-modal-state
  (fn [db]
    (-> db
        (get-in [:editor-v2 :translator :translator-modal-state])
        boolean)))

(re-frame/reg-sub
  ::selected-action
  (fn [db]
    (get-in db [:editor-v2 :translator :selected-action])))
