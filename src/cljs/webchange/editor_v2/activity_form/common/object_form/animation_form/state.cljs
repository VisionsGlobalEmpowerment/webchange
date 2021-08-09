(ns webchange.editor-v2.activity-form.common.object-form.animation-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.logger.index :as logger]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:animation-form])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [animation-data (select-keys objects-data [:name :skin :skin-names])]
      {:dispatch-n [[::state/init id {:data  animation-data
                                      :names objects-names}]
                    [::load-available-skins id]]})))

;; Available skins

(def available-skins-path :available-skins)

(re-frame/reg-event-fx
  ::load-available-skins
  (fn [{:keys [_]} [_ id]]
    {:dispatch [::warehouse/load-animation-skins {:on-success [::set-available-skins id]}]}))

(re-frame/reg-event-fx
  ::set-available-skins
  (fn [{:keys [db]} [_ id skins]]
    {:db (assoc-in db (path-to-db id [available-skins-path]) skins)}))

(defn- get-available-skeletons
  [db id]
  (get-in db (path-to-db id [available-skins-path])))

(re-frame/reg-sub
  ::available-skeletons
  (fn [db [_ id]]
    (get-available-skeletons db id)))

;; Skeleton

(re-frame/reg-sub
  ::current-skeleton
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :name "")))


(re-frame/reg-sub
  ::skeletons-options
  (fn [[_ id]]
    (re-frame/subscribe [::available-skeletons id]))
  (fn [available-skins]
    (->> available-skins
         (map (fn [{:keys [name]}]
                {:text  name
                 :value name})))))

(re-frame/reg-event-fx
  ::set-skeleton
  (fn [{:keys [db]} [_ id skeleton-name]]
    (let [skeleton (->> (get-available-skeletons db id)
                        (some (fn [{:keys [name] :as skeleton}]
                                (and (= name skeleton-name) skeleton))))]
      {:dispatch [::state/update-current-data id {:name skeleton-name
                                                  :skin (:default-skin skeleton)}]})))

;; Skin

(re-frame/reg-sub
  ::skin-options
  (fn [[_ id]]
    [(re-frame/subscribe [::current-skeleton id])
     (re-frame/subscribe [::available-skeletons id])])
  (fn [[animation-name available-skeletons]]
    (->> available-skeletons
         (some (fn [{:keys [name skins]}]
                 (and (= name animation-name)
                      skins)))
         (map (fn [{:keys [name preview]}]
                {:value     name
                 :thumbnail preview}))
         (sort-by :thumbnail)
         (reverse))))

(defn- check-skin-option
  [option type]
  (-> option
      :value
      (clojure.string/split #"/")
      first
      (clojure.string/lower-case)
      keyword
      (= type)))

(re-frame/reg-sub
  ::skin-body-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option % :body) skin-options)))

(re-frame/reg-sub
  ::skin-clothes-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option % :clothes) skin-options)))

(re-frame/reg-sub
  ::skin-head-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option % :head) skin-options)))

(re-frame/reg-sub
  ::combined-skins?
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (-> skin-options
        first
        :value
        (clojure.string/split #"/")
        count
        (> 1))))

(re-frame/reg-sub
  ::current-skin
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :skin "")))

(re-frame/reg-event-fx
  ::set-current-skin
  (fn [{:keys [_]} [_ id skin]]
    (logger/trace "set current-skin" id skin)
    {:dispatch [::state/update-current-data id {:skin skin}]}))

(re-frame/reg-sub
  ::current-skin-names
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (logger/trace "current data" current-data)
    (get current-data :skin-names {:body nil})))

(re-frame/reg-event-fx
  ::set-current-skin-names
  (fn [{:keys [_]} [_ id skin-names]]
    (logger/trace "set skin names" id skin-names)
    {:dispatch [::state/update-current-data id {:skin-names skin-names}]}))
