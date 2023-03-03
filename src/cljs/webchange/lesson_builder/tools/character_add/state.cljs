(ns webchange.lesson-builder.tools.character-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.layout.menu.state :as menu-state]
    [webchange.lesson-builder.state :as lesson-builder-state]))

(def path-to-db :lesson-builder/character-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::select-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :form value)}))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [data (get db :form)]
      {:dispatch-n [[::lesson-builder-state/save-activity
                     {:on-success [::lesson-builder-state/add-character data]}]
                    [::menu-state/history-back]]})))
