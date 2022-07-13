(ns webchange.lesson-builder.tools.background-image.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.blocks.state :as layout-state]
    [webchange.lesson-builder.tools.stage-actions :as stage]
    [webchange.utils.scene-data :refer [get-scene-background]]))

(def path-to-db :lesson-builder/background-image)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; form content

(def form-content-key :form-content)

(defn- set-form-content
  [db value]
  (assoc db form-content-key value))

(re-frame/reg-sub
  ::form-content
  :<- [path-to-db]
  #(get % form-content-key))

(re-frame/reg-event-fx
  ::show-image-library
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db (set-form-content db {:component "image-library"
                               :type      type})}))



;; initial background

(def initial-background-key :initial-background)

(defn- get-initial-background
  [db]
  (get db initial-background-key))

(defn- set-initial-background
  [db value]
  (assoc db initial-background-key value))

(re-frame/reg-sub
  ::initial-background
  :<- [path-to-db]
  get-initial-background)

;; current background

(def current-background-key :current-background)

(defn- get-current-background
  [db]
  (get db current-background-key))

(defn- set-current-background
  [db value]
  (assoc db current-background-key value))

(defn- update-current-background-image
  [db type value]
  (assoc-in db [current-background-key :data type] value))

(defn- update-current-background-type
  [db value]
  (assoc-in db [current-background-key :type] value))

(re-frame/reg-sub
  ::current-background
  :<- [path-to-db]
  get-current-background)

(re-frame/reg-sub
  ::current-background-data
  :<- [::current-background]
  #(get % :data))

(re-frame/reg-sub
  ::current-background-type
  :<- [::current-background]
  #(get % :type))

(re-frame/reg-event-fx
  ::set-background-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db (-> db
             (update-current-background-type type)
             (set-form-content {:component type}))}))

;; events

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_]]
    (let [[name {:keys [type] :as data}] (get-scene-background activity-data)
          background-data {:name name
                           :type type
                           :data {:single-background (get data :src)
                                  :background        (get-in data [:background :src])
                                  :decoration        (get-in data [:decoration :src])
                                  :surface           (get-in data [:surface :src])}}]
      {:db (-> db
               (set-initial-background background-data)
               (set-current-background background-data)
               (set-form-content {:component type}))})))

(re-frame/reg-event-fx
  ::select-background-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [type image]}]]
    (let [{current-background-type :type} (get-current-background db)]
      {:db (-> db
               (update-current-background-image (keyword type) (:path image))
               (set-form-content {:component current-background-type}))})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [type data]} (get-current-background db)
          background-object-data (case type
                                   "background" (let [{:keys [single-background]} data]
                                                  (cond-> {:type "background"}
                                                          (some? single-background) (assoc :src single-background)))
                                   "layered-background" (let [{:keys [background surface decoration]} data]
                                                          (cond-> {:type "layered-background"}
                                                                  (some? background) (assoc-in [:background :src] background)
                                                                  (some? surface) (assoc-in [:surface :src] surface)
                                                                  (some? decoration) (assoc-in [:decoration :src] decoration))))]
      {:dispatch-n [[::stage/change-background background-object-data]
                    [::layout-state/reset-state]]})))

(re-frame/reg-event-fx
  ::cancel
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::layout-state/reset-state]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))
