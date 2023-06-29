(ns webchange.admin.pages.activities.state
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activities)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Activities Loading
(re-frame/reg-sub
  ::activities-loading?
  :<- [path-to-db]
  (fn [db]
    (or (:visible-activities-loading db)
        (:my-activities-loading db))))

;; Activities Data

(re-frame/reg-sub
  ::show-only-assessments?
  :<- [path-to-db]
  (fn [db]
    (get db :show-only-assessments? false)))

(re-frame/reg-sub
  ::show-my-global?
  :<- [path-to-db]
  (fn [db]
    (get db :show-my-global? true)))

(re-frame/reg-sub
  ::selected-type
  :<- [path-to-db]
  (fn [db]
    (get db :selected-type :visible)))

(defn- with-library-type
  [{:keys [status owner-id] :as activity} user-id]
  (let [is-global? (= "visible" status)
        is-owner? (= owner-id user-id)]
    (cond
      (and is-global? is-owner?) (assoc activity :library-type "my")
      is-global? (assoc activity :library-type "global")
      :else activity)))

(defn- filter-by-search
  [search-string activities]
  (let [search-query (-> search-string (s/lower-case) (s/split #" "))]
    (reduce (fn [activities search-word]
              (filter #(s/includes? (:keywords %) search-word) activities))
            activities
            search-query)))

(re-frame/reg-sub
  ::selected-group
  :<- [path-to-db]
  #(get % :selected-group))

(re-frame/reg-sub
  ::activities-list
  :<- [path-to-db]
  :<- [::selected-type]
  :<- [::show-my-global?]
  :<- [::show-only-assessments?]
  :<- [::search-string]
  :<- [::current-language]
  (fn [[db selected-type show-my-global show-only-assessments search-string current-language]]
    (let [current-user-id (get-in db [:current-user :id])
          activities (if (= selected-type :visible)
                       (:visible-activities db)
                       (:my-activities db))]
      (cond->> activities
               :always (map #(with-library-type % current-user-id))
               :always (filter (fn [{:keys [lang]}]
                                 (= lang current-language)))
               (not show-my-global) (remove #(and
                                              (= (:status %) "visible")
                                              (= (:owner-id %) current-user-id)))
               show-only-assessments (filter #(:assessment %))
               (-> search-string seq) (filter-by-search search-string)
               :always (map #(assoc % :template-id (-> % :metadata :template-id)))
               :always (sort-by :name)))))

(re-frame/reg-sub
  ::activities-counter
  :<- [path-to-db]
  (fn [db]
    {:my      (-> db :my-activities count)
     :visible (-> db :visible-activities count)}))

(re-frame/reg-sub
  ::groups
  :<- [path-to-db]
  :<- [::activities-list]
  (fn [[db activities-list]]
    (let [templates (->> (:templates db)
                         (map (juxt :id identity))
                         (into {}))]
      (->> activities-list
           (group-by :template-id)
           (map #(-> % second first))
           (map #(assoc % :id (:template-id %)))
           (map #(assoc % :name (get-in templates [(:id %) :name])))
           (filter #(seq (:name %)))))))

(re-frame/reg-sub
  ::activities
  :<- [path-to-db]
  :<- [::activities-list]
  :<- [::selected-group]
  (fn [[db activities-list selected-group]]
    (->> activities-list
         (filter (fn [{:keys [template-id]}]
                   (= template-id selected-group)))
                                        ;       (sort-by :updated-at)
         )))

(re-frame/reg-event-fx
  ::select-group
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (assoc :selected-group value))}))

(re-frame/reg-event-fx
  ::reset-group
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (dissoc :selected-group))}))
;;

(def default-language "english")

(re-frame/reg-sub
  ::current-language
  :<- [path-to-db]
  #(get % :current-language default-language))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [current-language (get db :current-language default-language)]
      {:db         (-> db
                       (assoc :visible-activities-loading true)
                       (assoc :my-activities-loading true))
       :dispatch-n [[::warehouse/load-visible-activities
                     {:lang current-language}
                     {:on-success [::load-visible-activities-success]}]
                    [::warehouse/load-my-activities
                     {:lang current-language}
                     {:on-success [::load-my-activities-success]}]
                    [::warehouse/load-templates
                     {:on-success [::load-templates-success]}]
                    [::warehouse/load-current-user
                     {:on-success [::load-account-success]}]]})))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :current-user data))}))

(re-frame/reg-event-fx
  ::load-templates-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :templates data))}))

(comment
  (-> @re-frame.db/app-db
      (get-in [path-to-db])
      :templates))

(defn- with-keywords
  [activities]
  (map (fn [{name :name {:keys [description attributions skills]} :metadata :as a}]
         (assoc a :keywords (s/lower-case (str name " " description " " attributions " " skills))))
       activities))

(re-frame/reg-event-fx
  ::load-visible-activities-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :visible-activities-loading false)
             (assoc :visible-activities (with-keywords data)))}))

(re-frame/reg-event-fx
  ::load-my-activities-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (assoc :my-activities-loading false)
             (assoc :my-activities (with-keywords data)))}))

(re-frame/reg-event-fx
  ::select-language
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ language]]
    {:db         (-> db
                     (assoc :visible-activities-loading true)
                     (assoc :my-activities-loading true)
                     (assoc :current-language language))
     :dispatch-n [[::warehouse/load-visible-activities
                   {:lang language}
                   {:on-success [::load-visible-activities-success]}]
                  [::warehouse/load-my-activities
                   {:lang language}
                   {:on-success [::load-my-activities-success]}]]}))

(re-frame/reg-event-fx
  ::select-type
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db (-> db
             (assoc :selected-type type)
             (dissoc :selected-group))}))

(re-frame/reg-event-fx
  ::set-show-global
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-my-global? value)}))

(re-frame/reg-event-fx
  ::set-show-only-assessments
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-only-assessments? value)}))

(re-frame/reg-event-fx
  ::open-activity
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ activity-id]]
    {:dispatch [::routes/redirect :activity-edit :activity-id activity-id]}))

(re-frame/reg-event-fx
  ::edit-activity
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ activity-id]]
    {:dispatch [::routes/redirect :activity-edit :activity-id activity-id]}))

;; search string

(def search-string-key :search-string)

(defn- get-search-string
  [db]
  (get db search-string-key ""))

(defn- set-search-string
  [db value]
  (assoc db search-string-key value))

(re-frame/reg-sub
  ::search-string
  :<- [path-to-db]
  get-search-string)

(re-frame/reg-event-fx
  ::set-search-string
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-search-string db value)}))
