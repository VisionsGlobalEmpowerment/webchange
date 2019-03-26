(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]))

(re-frame/reg-event-fx
  ::init-editor
  (fn [_ [_ course-id]]
    {:dispatch-n (list [::ie/start-course course-id]
                       [::load-datasets]
                       )}))

(re-frame/reg-event-fx
  ::load-datasets
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:db (-> db
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
    {:db (assoc-in db [:editor :course-datasets] (:datasets result))
     :dispatch-n (list [:complete-request :datasets])}))

(re-frame/reg-event-fx
  ::edit-object
  (fn [{:keys [db]} [_ {:keys [scene-id target state]}]]
    {:db (update-in db [:scenes scene-id :objects (keyword target)] merge state)}))

(re-frame/reg-event-fx
  ::edit-current-scene-object
  (fn [{:keys [db]} [_ {:keys [target state]}]]
    {:db (update-in db [:current-scene-data :objects (keyword target)] merge state)}))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:editor :current-main-content] screen)}))

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
  ::show-upload-asset-form
  (fn [_ _]
    {:dispatch [::set-main-content :upload-asset-form]}))

(re-frame/reg-event-fx
  ::add-asset
  (fn [{:keys [db]} [_ {:keys [scene-id state]}]]
    (let [id (-> (get-in db [:scenes scene-id :assets]) count)
          assets (-> db
                     (get-in [:scenes scene-id :assets] [])
                     (assoc id state))]
      {:db (assoc-in db [:scenes scene-id :assets] assets)
       :reload-asset state})))

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
    (let [layers (get-in db [:scenes scene-id :scene-objects] [])
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
    (let [layers (get-in db [:current-scene-data :scene-objects] [])
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
      {:db (-> db
               (assoc-in [:loading :save-scene] true)
               (update-in [:scenes scene-id] merge scene-data))
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

(re-frame/reg-event-fx
  ::create-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [course-id (:current-course db)]
      {:db (assoc-in db [:loading :create-scene] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/scenes/" scene-id)
                    :params          {:scene {}}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::create-scene-success]
                    :on-failure      [:api-request-error :create-scene]}})))


(re-frame/reg-event-fx
  ::create-scene-success
  (fn [{:keys [db]} [_ {scene-name :name}]]
    (let [course-id (:current-course db)
          course-data (-> (:course-data db)
                          (update-in [:scenes] conj scene-name))]
    {:dispatch-n (list [:complete-request :create-scene]
                       [::save-course course-id course-data]
                       [::set-main-content :editor])})))


(re-frame/reg-event-fx
  ::save-course
  (fn [{:keys [db]} [_ course-id data]]
    {:db (assoc db :course-data data)
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses/" course-id)
                  :params          {:course data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::save-course-success]
                  :on-failure      [:api-request-error :save-course]}}))


(re-frame/reg-event-fx
  ::save-course-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :save-course])}))

(re-frame/reg-event-fx
  ::open-current-course-versions
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:db (-> db
               (assoc-in [:loading :course-versions] true)
               (assoc-in [:editor :current-main-content] :course-versions))
       :http-xhrio {:method          :get
                    :uri             (str "/api/courses/" course-id "/versions")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::open-current-course-versions-success]
                    :on-failure      [:api-request-error :course-versions]}})))


(re-frame/reg-event-fx
  ::open-current-course-versions-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:editor :course-versions] (:versions result))
     :dispatch-n (list [:complete-request :course-versions])}))

(re-frame/reg-event-fx
  ::restore-course-version
  (fn [{:keys [db]} [_ version-id]]
    {:db (assoc-in db [:loading :restore-course-version] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/course-versions/" version-id "/restore")
                  :params          {:id version-id}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::restore-course-version-success]
                  :on-failure      [:api-request-error :restore-course-version]}}))


(re-frame/reg-event-fx
  ::restore-course-version-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :restore-course-version]
                       [::open-current-course-versions])}))

(re-frame/reg-event-fx
  ::open-current-scene-versions
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)
          scene-id (:current-scene db)]
      {:db (-> db
               (assoc-in [:loading :scene-versions] true)
               (assoc-in [:editor :current-main-content] :scene-versions))
       :http-xhrio {:method          :get
                    :uri             (str "/api/courses/" course-id "/scenes/" scene-id "/versions")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::open-current-scene-versions-success]
                    :on-failure      [:api-request-error :scene-versions]}})))


(re-frame/reg-event-fx
  ::open-current-scene-versions-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:editor :scene-versions] (:versions result))
     :dispatch-n (list [:complete-request :scene-versions])}))

(re-frame/reg-event-fx
  ::restore-scene-version
  (fn [{:keys [db]} [_ version-id]]
    {:db (assoc-in db [:loading :restore-scene-version] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/scene-versions/" version-id "/restore")
                  :params          {:id version-id}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::restore-scene-version-success]
                  :on-failure      [:api-request-error :restore-scene-version]}}))


(re-frame/reg-event-fx
  ::restore-scene-version-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :restore-scene-version]
                       [::open-current-scene-versions])}))

(re-frame/reg-event-fx
  ::add-dataset
  (fn [{:keys [db]} [_ {:keys [name fields]}]]
    (let [course-id (:current-course db)]
      {:db (assoc-in db [:loading :add-dataset] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/datasets")
                    :params          {:course-id course-id :name name :scheme {:fields fields}}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::add-dataset-success]
                    :on-failure      [:api-request-error :add-dataset]}})))


(re-frame/reg-event-fx
  ::add-dataset-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :add-dataset]
                       [::load-datasets])}))

(re-frame/reg-event-fx
  ::edit-dataset
  (fn [{:keys [db]} [_ dataset-id {:keys [fields]}]]
    {:db (assoc-in db [:loading :edit-dataset] true)
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
  ::show-edit-dataset-form
  (fn [{:keys [db]} [_ dataset-id]]
    {:db (assoc-in db [:editor :current-dataset-id] dataset-id)
     :dispatch [::set-main-content :edit-dataset-form]}))

(re-frame/reg-event-fx
  ::show-dataset
  (fn [{:keys [db]} [_ dataset-id]]
    {:db (assoc-in db [:editor :current-dataset-id] dataset-id)
     :dispatch-n (list [::load-current-dataset-items]
                       [::load-current-dataset-lessons]
                       [::set-main-content :dataset-info])}))

(re-frame/reg-event-fx
  ::load-current-dataset-items
  (fn [{:keys [db]} _]
    (let [dataset-id (get-in db [:editor :current-dataset-id])]
      {:db (-> db
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
    {:db (assoc-in db [:editor :current-dataset-items] (:items result))
     :dispatch-n (list [:complete-request :dataset-items])}))

(re-frame/reg-event-fx
  ::show-add-dataset-item-form
  (fn [_ _]
    {:dispatch [::set-main-content :add-dataset-item-form]}))

(re-frame/reg-event-fx
  ::add-dataset-item
  (fn [{:keys [db]} [_ {:keys [name data]}]]
    (let [dataset-id (get-in db [:editor :current-dataset-id])]
      {:db (assoc-in db [:loading :add-dataset-item] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/dataset-items")
                    :params          {:dataset-id dataset-id :name name :data data}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::add-dataset-item-success]
                    :on-failure      [:api-request-error :add-dataset-item]}})))


(re-frame/reg-event-fx
  ::add-dataset-item-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :add-dataset-item]
                       [::load-current-dataset-items]
                       [::set-main-content :dataset-info])}))

(re-frame/reg-event-fx
  ::show-edit-dataset-item-form
  (fn [{:keys [db]} [_ item-id]]
    {:db (assoc-in db [:editor :current-dataset-item-id] item-id)
     :dispatch [::set-main-content :edit-dataset-item-form]}))

(re-frame/reg-event-fx
  ::edit-dataset-item
  (fn [{:keys [db]} [_ item-id {:keys [data]}]]
    {:db (assoc-in db [:loading :edit-dataset-item] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/dataset-items/" item-id)
                  :params          {:data data}
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
  ::delete-dataset-item
  (fn [{:keys [db]} [_ item-id]]
    {:db (assoc-in db [:loading :delete-dataset-item] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/dataset-items/" item-id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-dataset-item-success]
                  :on-failure      [:api-request-error :delete-dataset-item]}}))


(re-frame/reg-event-fx
  ::delete-dataset-item-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :delete-dataset-item]
                       [::load-current-dataset-items])}))

(re-frame/reg-event-fx
  ::load-current-dataset-lessons
  (fn [{:keys [db]} _]
    (let [dataset-id (get-in db [:editor :current-dataset-id])]
      {:db (-> db
               (assoc-in [:loading :dataset-lessons] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/datasets/" dataset-id "/lesson-sets")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-current-dataset-lessons-success]
                    :on-failure      [:api-request-error :dataset-lessons]}})))


(re-frame/reg-event-fx
  ::load-current-dataset-lessons-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:editor :current-dataset-lessons] (:lesson-sets result))
     :dispatch-n (list [:complete-request :dataset-lessons])}))

(re-frame/reg-event-fx
  ::show-add-dataset-lesson-form
  (fn [_ _]
    {:dispatch [::set-main-content :add-dataset-lesson-form]}))

(re-frame/reg-event-fx
  ::add-dataset-lesson
  (fn [{:keys [db]} [_ {:keys [name data]}]]
    (let [dataset-id (get-in db [:editor :current-dataset-id])]
      {:db (assoc-in db [:loading :add-dataset-lesson] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/lesson-sets")
                    :params          {:dataset-id dataset-id :name name :data data}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::add-dataset-lesson-success]
                    :on-failure      [:api-request-error :add-dataset-lesson]}})))


(re-frame/reg-event-fx
  ::add-dataset-lesson-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :add-dataset-lesson]
                       [::load-current-dataset-lessons]
                       [::set-main-content :dataset-info])}))

(re-frame/reg-event-fx
  ::show-edit-dataset-lesson-form
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:editor :current-dataset-lesson-id] id)
     :dispatch [::set-main-content :edit-dataset-lesson-form]}))

(re-frame/reg-event-fx
  ::edit-dataset-lesson
  (fn [{:keys [db]} [_ id {{items :items} :data}]]
    {:db (assoc-in db [:loading :edit-dataset-lesson] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/lesson-sets/" id)
                  :params          {:data {:items items}}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-dataset-lesson-success]
                  :on-failure      [:api-request-error :edit-dataset-lesson]}}))


(re-frame/reg-event-fx
  ::edit-dataset-lesson-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :edit-dataset-lesson]
                       [::load-current-dataset-lessons]
                       [::set-main-content :dataset-info])}))

(re-frame/reg-event-fx
  ::delete-dataset-lesson
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:loading :delete-dataset-lesson] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/lesson-sets/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-dataset-lesson-success]
                  :on-failure      [:api-request-error :delete-dataset-lesson]}}))


(re-frame/reg-event-fx
  ::delete-dataset-lesson-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :delete-dataset-lesson]
                       [::load-current-dataset-lessons])}))

(re-frame/reg-event-fx
  ::upload-asset
  (fn [{:keys [db]} [_ scene-id js-file-value]]
    (let [form-data (doto
                      (js/FormData.)
                      (.append "file" js-file-value))]
      {:db (assoc-in db [:loading :upload-asset] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/assets/")
                    :body            form-data
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::upload-asset-success scene-id]
                    :on-failure      [:api-request-error :upload-asset]}})))


(re-frame/reg-event-fx
  ::upload-asset-success
  (fn [{:keys [db]} [_ scene-id result]]
      {:dispatch-n (list [:complete-request :upload-asset]
                         [::add-asset {:scene-id scene-id :state result}]
                         [::set-main-content :editor])}))

(re-frame/reg-event-fx
  ::add-object-to-current-scene
  (fn [{:keys [db]} [_ asset-id]]
    (let [scene-id (:current-scene db)
          asset (get-in db [:scenes scene-id :assets asset-id])
          state {:type :image :scene-layer 5 :scene-name "image"
                 :src (:url asset)
                 :width (:width asset)
                 :height (:height asset)}]
      (if (= "image" (:type asset))
        {:db (assoc-in db [:editor :new-object-defaults] state)
         :dispatch-n (list [::show-form :add-object])}))))