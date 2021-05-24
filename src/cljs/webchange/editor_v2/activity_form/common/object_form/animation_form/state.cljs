(ns webchange.editor-v2.activity-form.common.object-form.animation-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:animation-form])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [animation-data (select-keys objects-data [:skin])
          animation-name (get objects-data :name)]
      {:dispatch-n [[::state/init id {:data  animation-data
                                      :names objects-names}]
                    [::set-animation-name id animation-name]
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

(re-frame/reg-sub
  ::available-skins
  (fn [db [_ id]]
    (get-in db (path-to-db id [available-skins-path]))))

;; Animation Name

(def animation-name-path :animation-name)

(re-frame/reg-event-fx
  ::set-animation-name
  (fn [{:keys [db]} [_ id animation-name]]
    {:db (assoc-in db (path-to-db id [animation-name-path]) animation-name)}))

(re-frame/reg-sub
  ::animation-name
  (fn [db [_ id]]
    {:pre [(some? id)]}
    (get-in db (path-to-db id [animation-name-path]))))

;; Skin

(re-frame/reg-sub
  ::skin-options
  (fn [[_ id]]
    [(re-frame/subscribe [::animation-name id])
     (re-frame/subscribe [::available-skins id])])
  (fn [[animation-name available-skins]]
    (->> available-skins
         (some (fn [{:keys [name skins]}]
                 (and (= name animation-name)
                      skins)))
         (map (fn [{:keys [name preview]}]
                {:value     name
                 :thumbnail preview}))
         (sort-by :thumbnail)
         (reverse))))

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
    {:dispatch [::state/update-current-data id {:skin skin}]}))
