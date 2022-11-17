(ns webchange.editor-v2.activity-form.common.object-form.video-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:video-form])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [video-data (merge (select-keys objects-data [:volume]))]
      {:dispatch-n [[::state/init id {:data  video-data
                                      :names objects-names}]]})))

;; Volume

(re-frame/reg-sub
  ::volume
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :volume)))

(re-frame/reg-event-fx
  ::set-volume
  (fn [{:keys [_]} [_ id volume]]
    {:dispatch [::state/update-current-data id {:volume volume}]}))
