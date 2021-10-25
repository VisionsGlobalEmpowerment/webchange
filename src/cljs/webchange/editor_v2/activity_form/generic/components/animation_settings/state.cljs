(ns webchange.editor-v2.activity-form.generic.components.animation-settings.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]
    [webchange.subs :as subs]
    [webchange.utils.scene-data :refer [get-idle-animation-enabled]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :animation-settings-form])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))

;; Idle animation

(re-frame/reg-sub
  ::idle-animation-enabled?
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])])
  (fn [[scene-data]]
    (get-idle-animation-enabled scene-data)))

(re-frame/reg-event-fx
  ::set-idle-animation
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::state-activity/call-activity-common-action {:action :set-animation-settings
                                                              :data   {:idle-animation-enabled? value}}]}))
