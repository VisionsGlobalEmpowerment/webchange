(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

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
    {:db (update-in db [:editor] dissoc :selected-object-action)}))

(re-frame/reg-event-fx
  ::select-object-action
  (fn [{:keys [db]} [_ scene-id name action]]
    {:db (assoc-in db [:editor :selected-object-action] {:scene-id scene-id :name name :action action})}))

(re-frame/reg-event-fx
  ::edit-object-action
  (fn [{:keys [db]} [_ {:keys [scene-id target action state]}]]
    {:db (update-in db [:scenes scene-id :objects (keyword target) :actions (keyword action)] merge state)}))

(re-frame/reg-event-fx
  ::reset-object-state
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-object-state)}))

(re-frame/reg-event-fx
  ::select-object-state
  (fn [{:keys [db]} [_ scene-id name state]]
    {:db (assoc-in db [:editor :selected-object-state] {:scene-id scene-id :name name :state state})}))

(re-frame/reg-event-fx
  ::edit-object-state
  (fn [{:keys [db]} [_ {:keys [scene-id name state data]}]]
    (let [new-state-id (:state-id data)]
      {:db (-> db
               (update-in [:scenes scene-id :objects (keyword name) :states] dissoc state)
               (update-in [:scenes scene-id :objects (keyword name) :states (keyword new-state-id)] merge data))})))

(re-frame/reg-event-fx
  ::delete-object-state
  (fn [{:keys [db]} [_ scene-id name state]]
    {:db (update-in db [:scenes scene-id :objects (keyword name) :states] dissoc (keyword state))}))

(re-frame/reg-event-fx
  ::set-default-object-state
  (fn [{:keys [db]} [_ scene-id name state]]
    (let [state-data (get-in db [:scenes scene-id :objects (keyword name) :states (keyword state)])]
      {:db (-> db
               (update-in [:scenes scene-id :objects (keyword name)] merge state-data)
               (update-in [:current-scene-data :objects (keyword name)] merge state-data))})))

(re-frame/reg-event-fx
  ::reset-object
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-object)}))

(re-frame/reg-event-fx
  ::select-object
  (fn [{:keys [db]} [_ scene-id name]]
    {:db (assoc-in db [:editor :selected-object] {:scene-id scene-id :name name})}))

(re-frame/reg-event-fx
  ::reset-scene-action
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-scene-action)}))

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

(re-frame/reg-event-fx
  ::show-form
  (fn [{:keys [db]} [_ form]]
    {:db (assoc-in db [:editor :shown-form] form)}))

(re-frame/reg-event-fx
  ::reset-shown-form
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :shown-form)}))

(re-frame/reg-event-fx
  ::select-asset
  (fn [{:keys [db]} [_ scene-id id]]
    {:db (assoc-in db [:editor :selected-asset] {:scene-id scene-id :id id})}))

(re-frame/reg-event-fx
  ::select-new-asset
  (fn [{:keys [db]} _]
    (let [scene-id (:current-scene db)
          new-id (-> db (get-in [:scenes scene-id :assets]) count)]
      {:db (assoc-in db [:editor :selected-asset] {:scene-id scene-id :id new-id})})))

(re-frame/reg-event-fx
  ::edit-asset
  (fn [{:keys [db]} [_ {:keys [scene-id id state]}]]
    (let [asset (-> (get-in db [:scenes scene-id :assets id]) (merge state))]
      {:db (assoc-in db [:scenes scene-id :assets id] asset)
       :reload-asset asset})))

(re-frame/reg-event-fx
  ::reset-asset
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-asset)}))

(re-frame/reg-event-fx
  ::add-to-scene
  (fn [{:keys [db]} [_ {:keys [scene-id name layer]}]]
    (let [layers (get-in db [:scenes scene-id :scene-objects])
          current-layer (get-in db [:scenes scene-id :scene-objects layer] [])
          updated-layer (conj current-layer name)
          updated-layers (reduce (fn [layers idx] (if (= layer idx)
                                                    (assoc layers idx updated-layer)
                                                    (assoc layers idx (get layers idx [])))) layers (range 0 (inc layer)))]
      {:db (update-in db [:scenes scene-id] assoc :scene-objects updated-layers)}
      )))

(re-frame/reg-event-fx
  ::add-to-current-scene
  (fn [{:keys [db]} [_ {:keys [name layer]}]]
    (let [layers (get-in db [:current-scene-data :scene-objects])
          current-layer (get-in db [:current-scene-data :scene-objects layer] [])
          updated-layer (conj current-layer name)
          updated-layers (reduce (fn [layers idx] (if (= layer idx)
                                                    (assoc layers idx updated-layer)
                                                    (assoc layers idx (get layers idx [])))) layers (range 0 (inc layer)))]
      {:db (update-in db [:current-scene-data] assoc :scene-objects updated-layers)}
      )))

(re-frame/reg-event-fx
  ::save-scene
  (fn [{:keys [db]} [_ scene-id scene-data]]
    (let [course-id (:current-course db)]
      {:db (update-in db [:scenes scene-id] merge scene-data)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/scenes/" scene-id)
                    :params          {:scene scene-data}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::save-scene-success]
                    :on-failure      [:api-request-error :save-scene]}})))


(re-frame/reg-event-fx
  ::save-scene-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :save-scene])}))