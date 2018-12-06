(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::edit-object
  (fn [{:keys [db]} [_ {:keys [target state]}]]
    (let [scene-id (:current-scene db)]
      {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state)})))

(re-frame/reg-event-fx
  ::set-screen
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:editor :screen] screen)}))