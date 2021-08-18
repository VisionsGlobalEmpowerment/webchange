(ns webchange.editor-v2.activity-form.common.object-form.animation-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.logger.index :as logger]
    [webchange.state.warehouse-animations :as warehouse-animations]))

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
                    [::warehouse-animations/load-available-animation]]})))

;; Available skins

(defn- check-skin-option
  [value type]
  (-> value
      (clojure.string/split #"/")
      first
      (clojure.string/lower-case)
      keyword
      (= type)))

;; Skeleton

(re-frame/reg-sub
  ::current-skeleton
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :name "")))

(defn- filter-extra-skeletons
  [skeletons]
  (let [skeletons-to-avoid ["book" "boxes" "pinata" "vera-go" "vera-45" "vera-90"]]
    (->> skeletons
         (filter (fn [{:keys [name]}]
                   (-> #{name} (some skeletons-to-avoid) not))))))

(re-frame/reg-sub
  ::skeletons-options
  (fn []
    (re-frame/subscribe [::warehouse-animations/available-animations]))
  (fn [available-animations]
    (->> (filter-extra-skeletons available-animations)
         (map (fn [{:keys [name preview]}]
                {:text      name
                 :value     name
                 :thumbnail preview})))))

(re-frame/reg-event-fx
  ::set-skeleton
  (fn [{:keys [db]} [_ id skeleton-name]]
    (let [{:keys [default-skin default-skins skin-type]} (->> (warehouse-animations/get-available-animations db)
                                                              (some (fn [{:keys [name] :as skeleton}]
                                                                      (and (= name skeleton-name) skeleton))))
          default-skin-params (case skin-type
                                :combined {:skin-names default-skins}
                                :single {:skin default-skin})]
      {:dispatch [::state/update-current-data id (merge {:name skeleton-name} default-skin-params)]})))

;; Skin

(re-frame/reg-sub
  ::skin-options
  (fn [[_ id]]
    [(re-frame/subscribe [::current-skeleton id])
     (re-frame/subscribe [::warehouse-animations/available-animations])])
  (fn [[skeleton-name available-skeletons]]
    (->> available-skeletons
         (some (fn [{:keys [name skins]}]
                 (and (= name skeleton-name)
                      skins)))
         (map (fn [{:keys [name preview]}]
                {:value     name
                 :thumbnail preview}))
         (sort-by :thumbnail)
         (reverse))))

(re-frame/reg-sub
  ::skin-body-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option (:value %) :body) skin-options)))

(re-frame/reg-sub
  ::skin-clothes-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option (:value %) :clothes) skin-options)))

(re-frame/reg-sub
  ::skin-head-options
  (fn [[_ id]]
    [(re-frame/subscribe [::skin-options id])])
  (fn [[skin-options]]
    (filter #(check-skin-option (:value %) :head) skin-options)))

(re-frame/reg-sub
  ::combined-skins?
  (fn [[_ id]]
    [(re-frame/subscribe [::current-skeleton id])
     (re-frame/subscribe [::warehouse-animations/available-animations])])
  (fn [[skeleton-name available-skeletons]]
    (->> available-skeletons
         (some (fn [{:keys [name skin-type]}]
                 (and (= name skeleton-name)
                      skin-type)))
         (= :combined))))

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

;; Scale

(re-frame/reg-sub
  ::current-scale
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :scale)))

(re-frame/reg-event-fx
  ::set-scale
  (fn [{:keys [_]} [_ id scale-key scale-value]]
    {:dispatch [::state/update-current-data id {:scale {scale-key scale-value}}]}))

;; Flip

(defn- get-scale-x
  [db id]
  (-> (state/get-current-data db id)
      (get-in [:scale :x])))

(re-frame/reg-event-fx
  ::flip-animation
  (fn [{:keys [db]} [_ id]]
    (let [scale-x (get-scale-x db id)]
      {:dispatch [::set-scale id :x (- scale-x)]})))
