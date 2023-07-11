(ns webchange.lesson-builder.tools.background-image.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage]
    [webchange.lesson-builder.state :as lesson-builder-state]
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
    {:db       (-> db
                   (update-current-background-type type)
                   (set-form-content {:component type}))
     :dispatch [::update-background]}))

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

(defn- ->background-object-data
  [current-background]
  (let [{:keys [type data]} current-background]
    (case type
      "background" (let [{:keys [single-background]} data]
                     (cond-> {:type "background"}
                             (some? single-background) (assoc :src single-background)))
      "layered-background" (let [{:keys [background surface decoration]} data]
                             (cond-> {:type "layered-background"}
                                     (some? background) (assoc-in [:background :src] background)
                                     (some? surface) (assoc-in [:surface :src] surface)
                                     (some? decoration) (assoc-in [:decoration :src] decoration))))))

(re-frame/reg-event-fx
  ::select-background-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [type image]}]]
    (let [current-background (get-current-background db)]
      {:db       (-> db
                     (update-current-background-image (keyword type) (:path image))
                     (set-form-content {:component (:type current-background)}))
       :dispatch [::update-background]})))

(re-frame/reg-event-fx
  ::update-background
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [current-background (get-current-background db)
          background-object-data (->background-object-data current-background)]
      {:dispatch-n [[::stage/change-background background-object-data]]})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::lesson-builder-state/save-activity
                {:on-success [:layout/reset]}]}))

(re-frame/reg-event-fx
  ::cancel
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [initial-background (get-initial-background db)
          background-object-data (->background-object-data initial-background)]
      {:dispatch-n [[::stage/change-background background-object-data]
                    [:layout/reset]]})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))
