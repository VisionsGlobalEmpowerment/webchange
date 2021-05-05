(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]
    [webchange.editor-v2.concepts.subs :as concepts-subs]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]))

(re-frame/reg-event-fx
  ::init-editor
  (fn [_ [_ course-id scene-id]]
    {:dispatch-n (list [::ie/start-course course-id scene-id]
                       [::load-datasets])}))

(re-frame/reg-event-fx
  ::load-datasets
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:db         (-> db
                       (assoc-in [:loading :datasets] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/courses/" course-id "/datasets")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-datasets-success]
                    :on-failure      [:api-request-error :datasets]}})))

(re-frame/reg-event-fx
  ::load-datasets-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db [:editor :course-datasets] (:datasets result))
     :dispatch-n (list [:complete-request :datasets])}))

(re-frame/reg-event-fx
  ::update-object
  (fn [{:keys [db]} [_ {:keys [scene-id target state]}]]
    {:db (assoc-in db [:scenes scene-id :objects (keyword target)] state)}))

(re-frame/reg-event-fx
  ::update-current-scene-object
  (fn [{:keys [db]} [_ {:keys [target state]}]]
    {:db (assoc-in db [:current-scene-data :objects (keyword target)] state)}))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:editor :current-main-content] screen)}))

(re-frame/reg-event-fx
  ::reset-object
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-object)}))

(re-frame/reg-event-fx
  ::reset-scene-action
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-scene-action)}))

(re-frame/reg-event-fx
  ::select-scene-action
  (fn [{:keys [db]} [_ action scene-id]]
    (when-not action (throw (js/Error. "Action is not defined")))
    (when-not scene-id (throw (js/Error. "Scene id is not defined")))
    (let [action-data (get-in db [:scenes scene-id :actions (keyword action)])]
      {:db       (assoc-in db [:editor :selected-scene-action] {:scene-id scene-id :action action})
       :dispatch [::set-form-data action-data]})))

(re-frame/reg-event-fx
  ::set-form-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:editor :action-form] {:data data :path [] :breadcrumb []})}))

(re-frame/reg-event-fx
  ::show-form
  (fn [{:keys [db]} [_ form]]
    {:db (assoc-in db [:editor :shown-form] form)}))

(re-frame/reg-event-fx
  ::reset-shown-form
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :shown-form)}))

(re-frame/reg-event-fx
  ::add-asset
  (fn [{:keys [db]} [_ {:keys [scene-id state]}]]
    (let [asset (assoc state :date (.getTime (js/Date.)))
          assets (-> db
                     (get-in [:scenes scene-id :assets] [])
                     (conj asset))]
      {:db (assoc-in db [:scenes scene-id :assets] assets)})))

(re-frame/reg-event-fx
  ::reset-scene-assets
  (fn [{:keys [db]} [_ scene-id assets]]
    {:db (assoc-in db [:scenes scene-id :assets] assets)}))

(re-frame/reg-event-fx
  ::reset-scene-actions
  (fn [{:keys [db]} [_ scene-id actions]]
    {:db (assoc-in db [:scenes scene-id :actions] actions)}))

(re-frame/reg-event-fx
  ::reset-scene-objects
  (fn [{:keys [db]} [_ scene-id objects]]
    {:db (assoc-in db [:scenes scene-id :objects] objects)}))

(re-frame/reg-event-fx
  ::reset-scene-metadata
  (fn [{:keys [db]} [_ scene-id metadata]]
    {:db (assoc-in db [:scenes scene-id :metadata] metadata)}))

(re-frame/reg-event-fx
  ::reset-scene-skills
  (fn [{:keys [db]} [_ scene-id skills]]
    {:db (assoc-in db [:scene-skills scene-id] skills)}))

(re-frame/reg-event-fx
  ::reset-asset
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :selected-asset)}))

(defn save-scene
  [db course-id scene-id scene-data]
  (let [data (dissoc scene-data :animations)]
    {:db         (-> db
                     (assoc-in [:loading :save-scene] true)
                     (update-in [:scenes scene-id] merge data))
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses/" course-id "/scenes/" scene-id)
                  :params          {:scene data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::save-scene-success]

                  :on-failure      [:api-request-error :save-scene]}}))

(re-frame/reg-event-fx
  ::save-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [course-id (:current-course db)
          scene-data (get-in db [:scenes scene-id])]
      (save-scene db course-id scene-id scene-data))))

(re-frame/reg-event-fx
  ::save-scene-success
  (fn [{:keys [db]} [_ {:keys [scene-id data]}]]
    (re-frame/dispatch [::ie/set-scene scene-id data])
    (re-frame/dispatch [::ie/store-scene scene-id data])
    (re-frame/dispatch [::translator-form.scene/init-state])
    {:dispatch-n (list [:complete-request :save-scene])}))

(defn update-scene
  [db course-id scene-id scene-data-patch]
  (let [data (dissoc scene-data-patch :animations)]
    {:db         (-> db
                     (assoc-in [:loading :update-scene] true)
                     (update-in [:scenes scene-id] merge data))
     :http-xhrio {:method          :put
                  :uri             (str "/api/courses/" course-id "/scenes/" scene-id)
                  :params          {:scene data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::update-scene-success]
                  :on-failure      [:api-request-error :update-scene]}}))

(re-frame/reg-event-fx
  ::update-scene
  (fn [{:keys [db]} [_ scene-id scene-data-patch]]
    (let [course-id (:current-course db)]
      (update-scene db course-id scene-id scene-data-patch))))

(re-frame/reg-event-fx
  ::update-scene-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :update-scene])}))

(re-frame/reg-event-fx
  ::edit-dataset
  (fn [{:keys [db]} [_ dataset-id {:keys [fields]}]]
    {:db         (assoc-in db [:loading :edit-dataset] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/datasets/" dataset-id)
                  :params          {:scheme {:fields fields}}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-dataset-success]
                  :on-failure      [:api-request-error :edit-dataset]}}))

(re-frame/reg-event-fx
  ::edit-dataset-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :edit-dataset]
                       [::load-datasets])}))

(re-frame/reg-event-fx
  ::load-current-dataset-items
  (fn [{:keys [db]} _]
    (let [dataset-id (get-in db [:editor :current-dataset-id])]
      {:db         (-> db
                       (assoc-in [:loading :dataset-items] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/datasets/" dataset-id "/items")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-current-dataset-items-success]
                    :on-failure      [:api-request-error :dataset-items]}})))

(re-frame/reg-event-fx
  ::load-current-dataset-items-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db [:editor :current-dataset-items] (:items result))
     :dispatch-n (list [:complete-request :dataset-items])}))

(re-frame/reg-event-fx
  ::show-add-dataset-item-form
  (fn [_ _]
    {:dispatch [::set-main-content :add-dataset-item-form]}))

(re-frame/reg-event-fx
  ::edit-dataset-item
  (fn [{:keys [db]} [_ item-id {:keys [data name]}]]
    {:db         (assoc-in db [:loading :edit-dataset-item] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/dataset-items/" item-id)
                  :params          {:data data :name name}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-dataset-item-success]
                  :on-failure      [:api-request-error :edit-dataset-item]}}))

(re-frame/reg-event-fx
  ::edit-dataset-item-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :edit-dataset-item]
                       [::load-current-dataset-items]
                       [::set-main-content :dataset-info])}))

(re-frame/reg-event-fx
  ::update-dataset-item
  (fn [{:keys [db]} [_ id data-patch attempt]]
    (let [{:keys [name data dataset-id version]} (get-in db [:dataset-items id])
          new-data (merge data data-patch)
          attempt (or attempt 0)]
      {:db         (assoc-in db [:loading :update-dataset-item] true)
       :http-xhrio {:method          :put
                    :uri             (str "/api/dataset-items/" id)
                    :params          {:data new-data :name name :version version}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::update-dataset-item-success dataset-id]
                    :on-failure      [::update-dataset-item-error id data-patch dataset-id attempt]}})))

(re-frame/reg-event-fx
  ::update-dataset-item-success
  (fn [{:keys [db]} [_ dataset-id {:keys [id data]}]]
    (let [prepared-data (assoc data :dataset-id dataset-id)]
      {:db         (assoc-in db [:dataset-items id] prepared-data)
       :dispatch-n (list [:complete-request :update-dataset-item]
                         [::update-course-dataset-item-data id data]
                         [::translator-form.concepts/reset-concept-patch id])})))

(re-frame/reg-event-fx
  ::update-dataset-item-error
  (fn [{:keys [db]} [_ id data-patch dataset-id attempt {:keys [status response] :as result}]]
    (js/console.log "error" result "data-patch" data-patch)
    (let [attempts-left (< attempt 5)
          conflict (= 409 status)]
      (if (and attempts-left conflict)
        (let [data (-> response :data)
              prepared-data (assoc data :dataset-id dataset-id)]
          {:db         (assoc-in db [:dataset-items id] prepared-data)
           :dispatch-n (list [::update-dataset-item id data-patch (inc attempt)])})
        {:dispatch [:api-request-error :update-dataset-item]}))))

(re-frame/reg-event-fx
  ::update-course-dataset-item-data
  (fn [{:keys [db]} [_ id data]]
    (let [dataset-items (concepts-subs/dataset-items db)
          item-index (->> dataset-items
                          (map-indexed vector)
                          (some (fn [[idx dataset-item]]
                                  (and (= (:id dataset-item) id)
                                       idx))))]
      {:db (update-in db [:editor :course-dataset-items item-index] merge data)})))

(re-frame/reg-event-fx
  ::select-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (if-not (nil? scene-id)
      {:dispatch-n (list
                     [::ie/set-current-scene scene-id]
                     [::reset-asset]
                     [::reset-object]
                     [::reset-scene-action]
                     [::reset-shown-form]
                     [::set-main-content :editor])}
      {})))
