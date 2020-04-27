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
  ::blocking-progress
  (fn [db]
    (-> db
        (get-in [:editor-v2 :translator :blocking-progress])
        boolean)))

(defn selected-action
  [db]
  (get-in db [:editor-v2 :translator :selected-action]))

(re-frame/reg-sub
  ::selected-action
  selected-action)

(defn current-concept
  [db]
  (get-in db [:editor-v2 :translator :current-concept]))

(re-frame/reg-sub
  ::current-concept
  current-concept)
