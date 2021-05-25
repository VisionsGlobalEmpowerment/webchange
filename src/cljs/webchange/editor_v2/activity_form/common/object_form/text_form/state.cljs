(ns webchange.editor-v2.activity-form.common.object-form.text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.state.state-fonts :as fonts]
    [webchange.utils.text :refer [text->chunks]]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
    (let [text-data (select-keys objects-data [:text :chunks :font-family :font-size])]
      {:dispatch [::state/init id {:data  text-data
                                   :names objects-names}]})))

;; Text

(re-frame/reg-sub
  ::current-text
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :text "")))

(re-frame/reg-event-fx
  ::set-current-text
  (fn [{:keys [db]} [_ id text]]
    (let [has-chunks? (-> (state/get-current-data db id) (get :chunks) (some?))]
      {:dispatch [::state/update-current-data id (cond-> {:text text}
                                                         has-chunks? (assoc :chunks (text->chunks text)))]})))

;; Font Family

(re-frame/reg-sub
  ::font-family-options
  (fn []
    [(re-frame/subscribe [::fonts/font-family-options])])
  (fn [[options]]
    options))

(re-frame/reg-sub
  ::current-font-family
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :font-family "")))

(re-frame/reg-event-fx
  ::set-current-font-family
  (fn [{:keys [_]} [_ id font-family]]
    {:dispatch [::state/update-current-data id {:font-family font-family}]}))

;; Font Size

(re-frame/reg-sub
  ::font-size-options
  (fn [[_ id]]
    [(re-frame/subscribe [::fonts/font-size-options])
     (re-frame/subscribe [::current-font-size id])])
  (fn [[options current-font-size]]
    (->> current-font-size
         (conj (map :value options))
         (remove #(= % ""))
         (distinct)
         (sort)
         (map (fn [size] {:text  size
                          :value size})))))

(re-frame/reg-sub
  ::current-font-size
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :font-size "")))

(re-frame/reg-event-fx
  ::set-current-font-size
  (fn [{:keys [_]} [_ id font-size]]
    {:dispatch [::state/update-current-data id {:font-size font-size}]}))
