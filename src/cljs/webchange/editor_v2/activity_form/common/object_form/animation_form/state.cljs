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
    (let [animation-data (select-keys objects-data [:name :skin :skin-names :scale])]
      {:dispatch-n [[::state/init id {:data  animation-data
                                      :names objects-names}]
                    [::load-available-skeletons id]]})))

;; Available skins

(def available-skeletons-path :available-skeletons)

(re-frame/reg-event-fx
  ::load-available-skeletons
  (fn [{:keys [_]} [_ id]]
    {:dispatch [::warehouse/load-animation-skins {:on-success [::set-available-skeletons id]}]}))

(defn- set-default-skin
  [skeletons]
  (->> skeletons
       (map (fn [{:keys [skins] :as skeleton-data}]
              (let [first-skin (-> skins first :name)
                    ;; Take first not 'default' skin because 'default' skin is broken in most old animations
                    first-not-default-skin (some (fn [{:keys [name]}] (and (not= name "default") name)) skins)]
                (->> (or first-not-default-skin first-skin)
                     (assoc skeleton-data :default-skin)))))))

(defn- filter-extra-skeletons
  [skeletons]
  (let [skeletons-to-avoid ["book" "boxes" "pinata" "vera-go" "vera-45" "vera-90"]]
    (->> skeletons
         (filter (fn [{:keys [name]}]
                   (-> #{name} (some skeletons-to-avoid) not))))))

(re-frame/reg-event-fx
  ::set-available-skeletons
  (fn [{:keys [db]} [_ id skeletons]]
    (let [skeletons-data (->> skeletons set-default-skin filter-extra-skeletons)]
      {:db (assoc-in db (path-to-db id [available-skeletons-path]) skeletons-data)})))

(defn- get-available-skeletons
  [db id]
  (get-in db (path-to-db id [available-skeletons-path])))

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
         (map (fn [{:keys [name preview]}]
                {:text      name
                 :value     name
                 :thumbnail preview})))))

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

;; Flip

(defn- get-scale-x
  [db id]
  (-> (state/get-current-data db id)
      (get-in [:scale :x])))

(re-frame/reg-event-fx
  ::flip-animation
  (fn [{:keys [db]} [_ id]]
    (let [scale-x (get-scale-x db id)]
      {:dispatch [::state/update-current-data id {:scale {:x (- scale-x)}}]})))
