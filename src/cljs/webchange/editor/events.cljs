(ns webchange.editor.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]
    [webchange.editor-v2.concepts.subs :as concepts-subs]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.state.warehouse :as warehouse]
    [webchange.state.state :as state-core]))

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
  ::reset-shown-form
  (fn [{:keys [db]} [_]]
    {:db (update-in db [:editor] dissoc :shown-form)}))

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
    {:db       (update-in db [:scenes scene-id] merge data)
     :dispatch [::warehouse/save-activity-version
                {:activity-id   scene-id
                 :activity-data scene-data}
                {:on-success [::save-scene-success]}]}))

(re-frame/reg-event-fx
  ::save-current-scene
  (fn [{:keys [db]} [_ scene-id]]
    (let [course-id (:current-course db)
          scene-data (get-in db [:scenes scene-id])]
      (save-scene db course-id scene-id scene-data))))

(re-frame/reg-event-fx
  ::save-scene-success
  (fn [{:keys [_]} [_ {:keys [scene-id data]}]]
    {:dispatch-n (list [::ie/set-scene scene-id data]
                       [::ie/store-scene scene-id data]
                       [::translator-form.scene/init-state]
                       [::state-core/update-last-saved]
                       [:complete-request :save-scene])}))

(re-frame/reg-event-fx
  ::edit-dataset
  (fn [{:keys [db]} [_ dataset-id data attempt]]
    (let [{version :version {fields :fields} :scheme} (editor-subs/dataset-concept db)
          fields (->> fields
                      (concat (:add data))
                      (remove (fn [field] (some #{field} (:remove data)))))
          attempt (or attempt 0)]
      {:db         (assoc-in db [:loading :edit-dataset] true)
       :http-xhrio {:method          :put
                    :uri             (str "/api/datasets/" dataset-id)
                    :params          {:scheme {:fields fields} :version version}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::edit-dataset-success]
                    :on-failure      [::edit-dataset-error dataset-id data attempt]}})))

(re-frame/reg-event-fx
  ::edit-dataset-success
  (fn [{:keys [db]} [_ response]]
    {:db         (assoc-in db [:editor :course-datasets] [response])
     :dispatch-n (list [:complete-request :edit-dataset]
                       [::load-datasets]
                       [::translator-form.concepts/reset-current-dataset response])}))

(re-frame/reg-event-fx
  ::edit-dataset-error
  (fn [{:keys [db]} [_ id data attempt {:keys [status response] :as result}]]
    (let [attempts-left (< attempt 5)
          conflict (= 409 status)]
      (if (and attempts-left conflict)
        {:db       (assoc-in db [:editor :course-datasets] [response])
         :dispatch [::edit-dataset id data (inc attempt)]}
        {:dispatch [:api-request-error :edit-dataset]}))))

(re-frame/reg-event-fx
  ::update-dataset-item
  (fn [{:keys [db]} [_ id data-patch attempt]]
    (let [{:keys [name data version]} (get-in db [:dataset-items id])
          new-data (merge data data-patch)
          attempt (or attempt 0)]
      {:db         (assoc-in db [:loading :update-dataset-item] true)
       :http-xhrio {:method          :put
                    :uri             (str "/api/dataset-items/" id)
                    :params          {:data new-data :name name :version version}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::update-dataset-item-success]
                    :on-failure      [::update-dataset-item-error id data-patch attempt]}})))

(re-frame/reg-event-fx
  ::update-dataset-item-success
  (fn [{:keys [db]} [_ {:keys [id data] :as response}]]
    {:db         (assoc-in db [:dataset-items id] response)
     :dispatch-n (list [:complete-request :update-dataset-item]
                       [::update-course-dataset-item-data id data]
                       [::translator-form.concepts/reset-concept id response])}))

(re-frame/reg-event-fx
  ::update-dataset-item-error
  (fn [{:keys [db]} [_ id data-patch attempt {:keys [status response] :as result}]]
    (let [attempts-left (< attempt 5)
          conflict (= 409 status)]
      (if (and attempts-left conflict)
        {:db         (assoc-in db [:dataset-items id] response)
         :dispatch-n (list [::update-dataset-item id data-patch (inc attempt)])}
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
