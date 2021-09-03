(ns webchange.editor-v2.layout.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :scene-data] relative-path))

;; Current content

(def current-content-path (path-to-db [:current-content]))

(defn get-current-content
  [db]
  (get-in db current-content-path :activity-stage))

(re-frame/reg-sub
  ::current-content
  get-current-content)

(re-frame/reg-event-fx
  ::set-current-content
  (fn [{:keys [db]} [_ content-key]]
    {:db (assoc-in db current-content-path content-key)}))

(re-frame/reg-event-fx
  ::set-activity-dialogs
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-current-content :activity-dialogs]}))

(re-frame/reg-event-fx
  ::set-activity-stage
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-current-content :activity-stage]}))

(re-frame/reg-event-fx
  ::switch-content-mode
  (fn [{:keys [db]} [_]]
    (let [current-mode (get-current-content db)]
      (if (= current-mode :activity-stage)
        {:dispatch [::set-activity-dialogs]}
        {:dispatch [::set-activity-stage]}))))
