(ns webchange.editor.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.state.core :as state-core]))

(def path-to-db :pages/editor)

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ scene-id]]
    {:db (-> db
             (assoc :scene-id scene-id))
     :dispatch-n [[::state-core/set-current-scene-id scene-id]
                  [::warehouse/load-activity-current-version {:activity-id scene-id}
                   {:on-success [::load-scene-success]}]]}))

(re-frame/reg-event-fx
  ::load-scene-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ scene-data]]
    (let [scene-id (:scene-id db)]
      {:db (assoc db :scene scene-data)
       :dispatch [::state-core/set-scene-data {:scene-id scene-id
                                               :scene-data scene-data}]})))
