(ns webchange.editor-v2.activity-form.common.object-form.animation-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.editor-v2.components.character-form.data :refer [animation->character-data]]
    [webchange.interpreter.renderer.scene.components.animation.animation-params :refer [animations-params]]
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
    (let [animation-data (merge {:scale {:x 1 :y 1}}
                                (select-keys objects-data [:name :skin :skin-names :scale]))]
      {:dispatch-n [[::state/init id {:data  animation-data
                                      :names objects-names}]
                    [::warehouse-animations/load-available-animation]]})))

;; Skeleton

(re-frame/reg-sub
  ::current-character-data
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (animation->character-data current-data)))

(defn- get-default-skeleton-params
  [skeleton-name]
  (let [default-params (->> (keyword skeleton-name)
                            (get animations-params))]
    (select-keys default-params [:scale :speed])))

(re-frame/reg-event-fx
  ::set-skeleton
  (fn [{:keys [db]} [_ id skeleton-name skin-params]]
    (let [{:keys [default-skin default-skins skin-type]} (->> (warehouse-animations/get-available-animations db)
                                                              (some (fn [{:keys [name] :as skeleton}]
                                                                      (and (= name skeleton-name) skeleton))))
          default-skin-params (merge (case skin-type
                                       :combined {:skin-names (merge default-skins
                                                                     skin-params)
                                                  :skin       nil}
                                       :single {:skin       (or skin-params default-skin)
                                                :skin-names nil})
                                     (get-default-skeleton-params skeleton-name))]
      {:dispatch [::state/update-current-data id (merge {:name skeleton-name} default-skin-params)]})))

;; Skin

(re-frame/reg-event-fx
  ::set-current-skin-names
  (fn [{:keys [_]} [_ id skin-names]]
    (logger/trace "set skin names" id skin-names)
    {:dispatch [::state/update-current-data id {:skin-names skin-names}]}))
