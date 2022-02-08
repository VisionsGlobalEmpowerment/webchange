(ns webchange.state.warehouse-recognition
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
    (if-let [script-data (get-audio-script db audio-url)]
      (warehouse/dispatch-if-defined on-success script-data)
      {:dispatch [::warehouse/load-audio-script-polled
                  {:file audio-url}
                  {:on-success [::load-audio-script-data-success {:audio-url audio-url} handlers]
                   :on-failure on-failure}]})))

(re-frame/reg-event-fx
  ::load-audio-script-data-success
  (fn [{:keys [_]} [_ {:keys [audio-url]} {:keys [on-success]} script-data]]
    (let [fixed-data (->> script-data (remove #(= "[unk]" (:word %))))]
      {:dispatch-n (cond-> [[::store-audio-script audio-url fixed-data]]
                           (some? on-success) (conj (conj on-success fixed-data)))})))
