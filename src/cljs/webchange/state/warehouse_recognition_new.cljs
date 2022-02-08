(ns webchange.state.warehouse-recognition-new
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:recognition])
       (warehouse/path-to-db)))

;; Audio scripts

(def audio-scripts-path (path-to-db [:audio-scripts]))

(defn get-audio-script
  [db audio-url]
  (->> (conj audio-scripts-path audio-url)
       (get-in db)))

(re-frame/reg-event-fx
  ::store-audio-script
  (fn [{:keys [db]} [_ audio-url script-data]]
    {:db (update-in db audio-scripts-path assoc audio-url script-data)}))

(re-frame/reg-event-fx
  ::load-audio-script-data
  (fn [{:keys [db]} [_ {:keys [audio-url]} {:keys [on-success on-failure] :as handlers}]]
    (if-not (-> (get-audio-script db audio-url) (some?))
      {:dispatch [::warehouse/load-audio-script-polled
                  {:file audio-url}
                  {:on-success [::load-audio-script-data-success {:audio-url audio-url} handlers]
                   :on-failure on-failure}]}
      (warehouse/dispatch-if-defined on-success))))

(re-frame/reg-event-fx
  ::load-audio-script-data-success
  (fn [{:keys [_]} [_ {:keys [audio-url]} {:keys [on-success]} script-data]]
    {:dispatch-n (cond-> [[::store-audio-script audio-url script-data]]
                         (some? on-success) (conj on-success))}))
