(ns webchange.editor-v2.layout.components.sync-status.history.state
  (:require
   [ajax.core :refer [json-request-format json-response-format]]
   [re-frame.core :as re-frame]
   [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]
   [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
   [webchange.interpreter.events :as interpreter.events]
   [webchange.state.core :as core]
   [webchange.state.warehouse :as warehouse]
   [webchange.subs :as subs]))

(def modal-versions-state-path [:editor-v2 :sandbox :restore-versions-state])
(def modal-template-state-path [:editor-v2 :sandbox :restore-template-state])


(re-frame/reg-sub
  ::versions
  (fn [db]
    (->> (concat modal-versions-state-path [:versions])
         (get-in db )
         (sort-by :created-at)
         (reverse))))

(re-frame/reg-sub
  ::last-update
  (fn []
    [(re-frame/subscribe [::versions])])
  (fn [[versions]]
    (-> versions first :created-at)))

(re-frame/reg-sub
  ::update-available
  (fn [db]
    (let [activity-template-version (-> (subs/current-scene-data db) :metadata :template-version)
          template-version (get-in db (concat modal-template-state-path [:version]) 0)]
      (< activity-template-version template-version))))

(re-frame/reg-event-fx
  ::reload-scene
  (fn [{:keys [_]} [_ name data]]
    {:pre [(string? name) (map? data)]}
    {:dispatch-n [[::core/set-scene-data {:scene-id   name
                                          :scene-data data}]
                  [::interpreter.events/set-scene name data]
                  [::interpreter.events/store-scene name data]
                  [::translator-form.scene/init-state]
                  [::state-stage/reset-stage]]}))

(re-frame/reg-event-fx
  ::load-versions
  (fn [{:keys [_]} [_ course-slug scene-slug]]
    (if (and (some? course-slug)
             (some? scene-slug))
      {:dispatch [::warehouse/load-versions
                  {:course-slug course-slug
                   :scene-slug  scene-slug}
                  {:on-success [::load-versions-success]}]})))

(re-frame/reg-event-fx
  ::load-versions-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db modal-versions-state-path result)}))

(re-frame/reg-event-fx
  ::restore-version
  (fn [{:keys [_]} [_ scene-version-id]]
    {:dispatch [::warehouse/restore-version
                {:scene-version-id scene-version-id}
                {:on-success [::restore-version-success]}]}))

(re-frame/reg-event-fx
  ::restore-version-success
  (fn [_ [_ {:keys [name data]}]]
    {:dispatch-n (list [::reload-scene name data]
                       [::close])}))

(re-frame/reg-event-fx
  ::load-template
  (fn [{:keys [_]} [_ template-id]]
    (when template-id
      {:dispatch [::warehouse/load-template
                  {:template-id template-id}
                  {:on-success [::load-template-success]}]})))

(re-frame/reg-event-fx
  ::load-template-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db modal-template-state-path result)}))

(re-frame/reg-event-fx
  ::update-template
  (fn [{:keys [db]} _]
    (let [course-slug (:current-course db)
          scene-slug (:current-scene db)]
      {:db         (assoc-in db [:loading :update-template] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-slug "/update-template/" scene-slug)
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::update-template-success]
                    :on-failure      [:api-request-error :update-template]}})))

(re-frame/reg-event-fx
  ::update-template-success
  (fn [{:keys [_]} [_ {:keys [name data]}]]
    {:dispatch-n (list [:complete-request :update-template]
                       [::reload-scene name data]
                       [::close])}))

;; Window

(def modal-state-path [:editor-v2 :sandbox :restore-modal-state])

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> (get-in db modal-state-path)
        (boolean))))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    (let [template-id (-> (subs/current-scene-data db) :metadata :template-id)
          course-slug (core/current-course-id db)
          scene-slug (core/current-scene-id db)]
      {:db         (-> db
                       (assoc-in modal-state-path true)
                       (assoc-in modal-versions-state-path {})
                       (assoc-in modal-template-state-path {}))
       :dispatch-n (list [::load-versions course-slug scene-slug]
                         [::load-template template-id])})))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))
