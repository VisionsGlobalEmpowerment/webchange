(ns webchange.editor-v2.activity-form.generic.components.guide-settings.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]
    [webchange.subs :as subs]
    [webchange.utils.scene-data :refer [get-guide-enabled get-guide-character]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :guide-settings-form])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    (let [current-scene-data (subs/current-scene-data db)
          data (get-in current-scene-data [:metadata :guide-settings])]
      {:db (-> db
               (assoc-in modal-state-path true)
               (assoc-in (path-to-db [:data]) data))})))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))


(re-frame/reg-sub
  ::show-guide
  (fn [db]
    (get-in db (path-to-db [:data :show-guide]) false)))

(re-frame/reg-sub
  ::character
  (fn [db]
    (get-in db (path-to-db [:data :character]))))

(re-frame/reg-event-fx
  ::set-show-guide
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:data :show-guide]) value)}))

(re-frame/reg-event-fx
  ::set-character
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db (path-to-db [:data :character]) value)}))

(re-frame/reg-event-fx
  ::save-settings
  (fn [{:keys [db]} [_]]
    (let [data (get-in db (path-to-db [:data]))]
      {:dispatch-n (list [::state-activity/call-activity-common-action {:action :set-guide-settings
                                                                        :data   data}]
                         [::close])})))
