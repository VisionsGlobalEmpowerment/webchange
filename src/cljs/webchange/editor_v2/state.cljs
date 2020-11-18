(ns webchange.editor-v2.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (concat [:editor-v2] relative-path))

;; Diagram state

(re-frame/reg-sub
  ::diagram-state
  (fn [db]
    (get-in db (path-to-db [:diagram-state]))))

(re-frame/reg-sub
  ::diagram-fullscreen?
  (fn []
    [(re-frame/subscribe [::diagram-state])])
  (fn [[diagram-state]]
    (= diagram-state :fullscreen)))

(re-frame/reg-event-fx
  ::diagram-fullscreen
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:diagram-state]) :fullscreen)}))

(re-frame/reg-event-fx
  ::diagram-exit-fullscreen
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:diagram-state]) :normal)}))
