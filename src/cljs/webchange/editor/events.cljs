(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::edit-object
  (fn [{:keys [db]} [_ {:keys [scene-id target state]}]]
    {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state)}))

(re-frame/reg-event-fx
  ::edit-current-scene-object
  (fn [{:keys [db]} [_ {:keys [target state]}]]
    {:db (update-in db [:current-scene-data :objects (keyword target)] merge state)}))

(re-frame/reg-event-fx
  ::set-screen
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:editor :screen] screen)}))

(re-frame/reg-event-fx
  ::register-transform
  (fn [{:keys [db]} [_ transform]]
    {:db (assoc-in db [:editor :transform] transform)}))

(re-frame/reg-event-fx
  ::reset-transform
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :transform)}))

(re-frame/reg-event-fx
  ::reset-action
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :action)}))

(re-frame/reg-event-fx
  ::set-current-action
  (fn [{:keys [db]} [_ scene-id name action]]
    {:db (assoc-in db [:editor :action] {:scene-id scene-id :name name :action action})}))

(re-frame/reg-event-fx
  ::edit-action
  (fn [{:keys [db]} [_ {:keys [scene-id target action state]}]]
    {:db (update-in db [:scenes scene-id :objects (keyword target) :actions (keyword action)] merge state)}))