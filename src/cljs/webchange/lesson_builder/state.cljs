(ns webchange.lesson-builder.state
  (:require
    [clojure.data :as d]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.utils.flipbook :refer [flipbook-activity?]]
    [webchange.utils.scene-action-data :refer [dialog-sequence-action?]]
    [webchange.utils.scene-data :refer [update-action]]
    [webchange.utils.uid :refer [get-uid]]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.module-router :refer [set-before-leave! reset-before-leave!]]))

(def path-to-db :lesson-builder/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def activity-data-key :activity-data)

;; undo/redo history

(defn- push-undo
  [db activity-data]
  (-> db
      (update :undo-history conj activity-data)))

(defn- peek-undo
  [db]
  (-> db :undo-history first))

(defn- pop-undo
  [db]
  (-> db
      (update :undo-history rest)))

(defn- push-redo
  [db activity-data]
  (-> db
      (update :redo-history conj activity-data)))

(defn- peek-redo
  [db]
  (-> db :redo-history first))

(defn- pop-redo
  [db]
  (-> db
      (update :redo-history rest)))

(defn- reset-redo
  [db]
  (assoc db :redo-history '()))

(defn- undo
  [db]
  (-> db
      (assoc :has-activity-data-changes? true)
      (push-redo (get db activity-data-key))
      (assoc activity-data-key (peek-undo db))
      (pop-undo)))

(defn- redo
  [db]
  (-> db
      (assoc :has-activity-data-changes? true)
      (push-undo (get db activity-data-key))
      (assoc activity-data-key (peek-redo db))
      (pop-redo)))

(defn undo-available?
  [db]
  (->> (get-in db [path-to-db :undo-history])
       (remove nil?)
       (seq)))

(defn- diff-for-undo
  [db]
  (let [current-data (-> (get db activity-data-key) :objects)
        next-data (-> (peek-undo db) :objects)]
    (d/diff current-data next-data)))

(comment
  (let [db (get @re-frame.db/app-db path-to-db)
        current-data (-> (get db activity-data-key) :objects)
        next-data (-> (peek-undo db) :objects)]
    (d/diff current-data next-data)

    )
  (-> @re-frame.db/app-db
      (diff-for-undo)))
;; activity data

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key true))

(defn get-activity-data
  ([db]
   (get db activity-data-key))
  ([db path-to-db]
   (get-in db [path-to-db activity-data-key])))

(defn- set-activity-data
  [db value]
  (set-before-leave!)
  (-> db
      (push-undo (get db activity-data-key))
      (reset-redo)
      (assoc activity-data-key value)
      (assoc :has-activity-data-changes? true)))

(defn- reset-activity-data
  [db value]
  (reset-before-leave!)
  (-> db
      (push-undo (get db activity-data-key))
      (reset-redo)
      (assoc activity-data-key value)
      (assoc :has-activity-data-changes? false)))

(re-frame/reg-sub
  ::activity-data
  :<- [path-to-db]
  #(get-activity-data %))

(comment
  (-> @(re-frame/subscribe [::activity-data])
      :metadata
      (select-keys [:next-page-id :next-spread-id :flipbook-pages])))

(re-frame/reg-cofx
 :activity-data
 (fn [{:keys [db] :as co-effects}]
   (->> (get-activity-data db path-to-db)
        (assoc co-effects :activity-data))))

(re-frame/reg-event-fx
  ::reset-activity-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-data]]
    {:db (-> db
             (reset-activity-data activity-data))}))

(re-frame/reg-event-fx
  ::set-activity-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-data]]
    {:db (-> db
             (set-activity-data activity-data))}))

(re-frame/reg-sub
  ::flipbook?
  :<- [::activity-data]
  flipbook-activity?)

;; activity info

(def activity-info-loading-key :activity-info-loading?)

(defn- set-activity-info-loading
  [db value]
  (assoc db activity-info-loading-key value))

(re-frame/reg-sub
  ::activity-info-loading?
  :<- [path-to-db]
  #(get % activity-info-loading-key true))

(def activity-info-key :activity-info)

(defn get-activity-info
  ([db]
   (get db activity-info-key))
  ([db path-to-db]
   (get-in db [path-to-db activity-info-key])))

(defn- set-activity-info
  [db value]
  (assoc db activity-info-key value))

(re-frame/reg-sub
  ::activity-info
  :<- [path-to-db]
  #(get-activity-info %))

(re-frame/reg-cofx
  :activity-info
  (fn [{:keys [db] :as co-effects}]
    (->> (get-activity-info db path-to-db)
         (assoc co-effects :activity-info))))

(re-frame/reg-event-fx
  ::set-activity-info
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-info]]
    {:db (-> db (set-activity-info activity-info))}))

;; activity versions

(def activity-versions-loading-key :activity-versions-loading?)

(defn- set-activity-versions-loading
  [db value]
  (assoc db activity-versions-loading-key value))

(re-frame/reg-sub
  ::activity-versions-loading?
  :<- [path-to-db]
  #(get % activity-versions-loading-key false))

(def activity-versions-key :activity-versions)

(defn- set-activity-versions
  [db value]
  (assoc db activity-versions-key value))

(re-frame/reg-sub
  ::activity-versions
  :<- [path-to-db]
  #(get % activity-versions-key []))

(re-frame/reg-event-fx
  ::load-versions
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity-info db)]
      {:db       (-> db (set-activity-versions-loading true))
       :dispatch [::warehouse/load-activity-versions
                  {:activity-id id}
                  {:on-success [::load-versions-success]
                   :on-failure [::load-versions-failure]}]})))

(re-frame/reg-event-fx
  ::load-versions-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ versions]]
    {:db (-> db
             (set-activity-versions versions)
             (set-activity-versions-loading false))}))

(re-frame/reg-event-fx
  ::load-versions-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-versions-loading false))}))

;; general

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id]}]]
    {:db         (-> db
                     (set-activity-loading true)
                     (set-activity-info-loading true))
     :dispatch-n [[::warehouse/load-activity-current-version
                   {:activity-id activity-id}
                   {:on-success [::load-activity-success]
                    :on-failure [::load-activity-failure]}]
                  [::warehouse/load-activity
                   {:activity-id activity-id}
                   {:on-success [::load-activity-info-success]
                    :on-failure [::load-activity-info-failure]}]]}))

(defn- set-actions-uid
  [activity-data]
  (update-action activity-data
                 (fn [{:keys [data]}]
                   (dialog-sequence-action? data))
                 (fn [action-data]
                   (assoc action-data :uid (get-uid)))))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-data]]
    {:db (-> db
             (set-activity-loading false)
             (reset-activity-data (set-actions-uid activity-data)))}))

(re-frame/reg-event-fx
  ::load-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))

(re-frame/reg-event-fx
  ::load-activity-info-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-info]]
    {:db       (-> db
                   (set-activity-info-loading false)
                   (set-activity-info activity-info))
     :dispatch [::load-versions]}))

(re-frame/reg-event-fx
  ::load-activity-info-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-info-loading false))}))

;; save

(def activity-saving-key :activity-saving?)

(defn- set-activity-saving
  [db value]
  (assoc db activity-saving-key value))

(re-frame/reg-sub
  ::activity-saving?
  :<- [path-to-db]
  #(get % activity-saving-key false))

(re-frame/reg-event-fx
  ::save-activity
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_ {:keys [on-success]}]]
    {:dispatch [::deferred-save-activity {:on-success on-success}]}))

(re-frame/reg-event-fx
  ::deferred-save-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    (let [{:keys [id]} (get-activity-info db)
          data (get-activity-data db)]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/save-activity-version
                  {:activity-id   id
                   :activity-data data}
                  {:on-success [::save-activity-success on-success]
                   :on-failure [::save-activity-failure]}]})))

(re-frame/reg-event-fx
  ::save-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success]]
    (reset-before-leave!)
    {:db       (-> db
                   (set-activity-saving false)
                   (set-activity-versions-loading true)
                   (assoc :has-activity-data-changes? false))
     :dispatch-n [[::load-versions]
                  on-success]}))

(re-frame/reg-event-fx
  ::save-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

;; Update Template

(re-frame/reg-event-fx
  ::update-template
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity-info db)]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/update-template
                  {:activity-id id}
                  {:on-success [::update-template-success]
                   :on-failure [::update-template-failure]}]})))

(re-frame/reg-event-fx
  ::update-template-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data]}]]
    {:db (-> db
             (set-activity-saving false)
             (reset-activity-data data))}))

(re-frame/reg-event-fx
  ::update-template-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

;; Add Image
(re-frame/reg-event-fx
  ::add-image
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [on-success common-action?] :or {common-action? true}}]]
    (let [{:keys [id]} (get-activity-info db)]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/activity-template-action
                  {:activity-id id
                   :action      "add-image"
                   :data        data
                   :common-action? common-action?}
                  {:on-success [::add-image-success on-success]
                   :on-failure [::add-image-failure]}]})))

(re-frame/reg-event-fx
  ::add-image-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success {:keys [data]}]]
    {:db         (-> db
                     (set-activity-saving false)
                     (reset-activity-data data))
     :dispatch-n (cond-> [[:stage/reset]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::add-image-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

;; Add Character
(re-frame/reg-event-fx
  ::add-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [{:keys [id]} (get-activity-info db)]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/activity-template-action
                  {:activity-id id
                   :action      "add-character"
                   :data        data}
                  {:on-success [::add-character-success]
                   :on-failure [::add-character-failure]}]})))

(re-frame/reg-event-fx
  ::add-character-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data]}]]
    {:db       (-> db
                   (set-activity-saving false)
                   (reset-activity-data data))
     :dispatch [:stage/reset]}))

(re-frame/reg-event-fx
  ::add-character-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

;; Remove
(re-frame/reg-event-fx
  ::remove-object
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [object-type object-name on-success]}]]
    (let [{:keys [id]} (get-activity-info db)
          action (case object-type
                   :uploaded-image :remove-image
                   :added-character :remove-character
                   :animation :remove-character
                   :question :remove-question
                   :anchor :remove-anchor
                   :text :remove-text)
          common-action? (not (= :text object-type))]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/activity-template-action
                  {:activity-id id
                   :action      action
                   :common-action? common-action?
                   :data        {:name object-name}}
                  {:on-success [::remove-object-success on-success]
                   :on-failure [::remove-object-failure]}]})))

(re-frame/reg-event-fx
  ::remove-object-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success {:keys [data]}]]
    {:db       (-> db
                   (set-activity-saving false)
                   (reset-activity-data data))
     :dispatch on-success}))

(re-frame/reg-event-fx
  ::remove-object-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

;; Background music
(re-frame/reg-event-fx
  ::update-background-music
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [src volume on-success]}]]
    (let [{:keys [id]} (get-activity-info db)]
      (if src
        {:db       (-> db (set-activity-saving true))
         :dispatch [::warehouse/activity-template-action
                    {:activity-id id
                     :action      :background-music
                     :data        {:background-music {:src    src
                                                      :volume volume}}}
                    {:on-success [::update-background-music-success on-success]
                     :on-failure [::update-background-music-failure]}]}
        {:db       (-> db (set-activity-saving true))
         :dispatch [::warehouse/activity-template-action
                    {:activity-id id
                     :action      :background-music-remove}
                    {:on-success [::update-background-music-success on-success]
                     :on-failure [::update-background-music-failure]}]}))))

(re-frame/reg-event-fx
  ::update-background-music-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success {:keys [data]}]]
    {:db       (-> db
                   (set-activity-saving false)
                   (reset-activity-data data))
     :dispatch on-success}))

(re-frame/reg-event-fx
  ::update-background-music-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))

(re-frame/reg-event-fx
  ::call-activity-action
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [action data common-action?] :or {common-action? true}} {:keys [on-success on-failure]}]]
    (let [{:keys [id]} (get-activity-info db)]
      {:db       (set-activity-saving db true)
       :dispatch [::warehouse/activity-template-action
                  {:activity-id    id
                   :action         action
                   :data           data
                   :common-action? common-action?}
                  {:on-success [::call-activity-action-success on-success]
                   :on-failure [::call-activity-action-failure on-failure]}]})))

(re-frame/reg-event-fx
  ::call-activity-action-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success response]]
    (cond-> {:db (set-activity-saving db false)}
            (some? on-success) (assoc :dispatch (conj on-success response)))))

(re-frame/reg-event-fx
  ::call-activity-action-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-failure response]]
    (cond-> {:db (set-activity-saving db false)}
            (some? on-failure) (assoc :dispatch (conj on-failure response)))))

(re-frame/reg-event-fx
  ::undo
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (undo db)}))
