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
  ::reset-object-action
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :select-object-action)}))

(re-frame/reg-event-fx
  ::select-object-action
  (fn [{:keys [db]} [_ scene-id name action]]
    {:db (assoc-in db [:editor :selected-object-action] {:scene-id scene-id :name name :action action})}))

(re-frame/reg-event-fx
  ::edit-object-action
  (fn [{:keys [db]} [_ {:keys [scene-id target action state]}]]
    {:db (update-in db [:scenes scene-id :objects (keyword target) :actions (keyword action)] merge state)}))

(re-frame/reg-event-fx
  ::reset-scene-action
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :scene-action)}))

(re-frame/reg-event-fx
  ::select-scene-action
  (fn [{:keys [db]} [_ scene-id action]]
    {:db (assoc-in db [:editor :selected-scene-action] {:scene-id scene-id :action action})}))

(re-frame/reg-event-fx
  ::edit-scene-action
  (fn [{:keys [db]} [_ {:keys [scene-id action state]}]]
    {:db (update-in db [:scenes scene-id :actions (keyword action)] merge state)}))

(re-frame/reg-event-fx
  ::show-scene-action
  (fn [{:keys [db]} [_ scene-id action]]
    {:db (assoc-in db [:editor :shown-scene-action] {:scene-id scene-id :action action})}))