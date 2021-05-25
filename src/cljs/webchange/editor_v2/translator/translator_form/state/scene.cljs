(ns webchange.editor-v2.translator.translator-form.state.scene
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.history.state :as history]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.scene-utils :refer [add-if-not-exist
                                                                              remove-by-key
                                                                              update-by-key]]
    [webchange.editor-v2.translator.translator-form.state.window-confirmation :as confirm]
    [webchange.subs :as subs]))

;; Subs

(defn scene-data
  [db]
  (get-in db (path-to-db [:scene :data]) {}))

(re-frame/reg-sub
  ::scene-data
  scene-data)

(re-frame/reg-sub
  ::has-changes
  (fn scene-data
    [db]
    (get-in db (path-to-db [:scene :changed]) false)))

(defn scene-id
  [db]
  (get-in db (path-to-db [:scene :id]) nil))

(re-frame/reg-sub
  ::scene-id
  scene-id)

(defn actions-data
  [db]
  (-> db scene-data :actions))

(re-frame/reg-sub
  ::actions-data
  (fn []
    [(re-frame/subscribe [::scene-data])])
  (fn [[scene-data]]
    (:actions scene-data)))

(defn assets-data
  [db]
  (-> db scene-data :assets))

(re-frame/reg-sub
  ::assets-data
  (fn []
    [(re-frame/subscribe [::scene-data])])
  (fn [[scene-data]]
    (:assets scene-data)))

(re-frame/reg-sub
  ::audio-assets
  (fn []
    [(re-frame/subscribe [::assets-data])])
  (fn [[assets-data]]
    (filter #(= (:type %) "audio") assets-data)))

(defn objects-data
  [db]
  (-> db scene-data :objects))

(defn metadata-data
  [db]
  (-> db scene-data :metadata))

(re-frame/reg-sub
  ::objects-data
  (fn []
    [(re-frame/subscribe [::scene-data])])
  (fn [[scene-data]]
    (:objects scene-data)))

(re-frame/reg-sub
  ::text-objects
  (fn []
    [(re-frame/subscribe [::objects-data])])
  (fn [[objects-data]]
    (filter (fn [[_ {:keys [type]}]]
              (= type "text"))
            objects-data)))

(re-frame/reg-sub
  ::available-animation-targets
  (fn []
    [(re-frame/subscribe [::objects-data])])
  (fn [[scene-objects]]
    (->> scene-objects
         (filter (fn [[_ {:keys [type]}]]
                   (= type "animation")))
         (map first)
         (map name))))

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [current-scene-id (subs/current-scene db)
          current-scene-data (subs/current-scene-data db)]
      {:db (-> db
               (assoc-in (path-to-db [:scene :data]) current-scene-data)
               (assoc-in (path-to-db [:scene :id]) current-scene-id))})))

(re-frame/reg-event-fx
  ::set-changes
  (fn [{:keys [db]} [_ asset-data]]
    {:db               (assoc-in db (path-to-db [:scene :changed]) true)
     :set-before-leave confirm/unsaved-changes-message}))

(re-frame/reg-event-fx
  ::add-asset
  (fn [{:keys [db]} [_ asset-data]]
    {:db         (update-in db (path-to-db [:scene :data :assets]) conj asset-data)
     :dispatch-n (list [::set-changes])}))

(re-frame/reg-event-fx
  ::add-asset-if-not-exist
  (fn [{:keys [db]} [_ asset-url asset-data]]
    (let [assets (assets-data db)]
      {:db         (assoc-in db (path-to-db [:scene :data :assets]) (add-if-not-exist assets :url asset-url asset-data))
       :dispatch-n (list [::set-changes])})))

(re-frame/reg-event-fx
  ::update-asset
  (fn [{:keys [db]} [_ asset-url data-patch]]
    (let [assets (assets-data db)]
      {:db         (assoc-in db (path-to-db [:scene :data :assets]) (update-by-key assets :url asset-url data-patch))
       :dispatch-n (list [::set-changes])})))

(re-frame/reg-event-fx
  ::update-asset-date
  (fn [_ [_ asset-url date]]
    {:dispatch-n (list [::update-asset asset-url {:date date}])}))

(re-frame/reg-event-fx
  ::delete-asset
  (fn [{:keys [db]} [_ asset-url]]
    (let [assets (assets-data db)]
      {:db         (assoc-in db (path-to-db [:scene :data :assets]) (remove-by-key assets :url asset-url))
       :dispatch-n (list [::set-changes])})))

(re-frame/reg-event-fx
  ::update-action
  (fn [{:keys [db]} [_ action-path data-patch {:keys [suppress-history?]}]]
    (let [actions-data (actions-data db)
          action-data (get-in actions-data action-path)
          updated-data (merge action-data data-patch)]
      {:db         (assoc-in db (path-to-db (concat [:scene :data :actions] action-path)) updated-data)
       :dispatch-n (->> (list [::set-changes]
                              (when-not suppress-history?
                                [::history/add-history-event {:type :scene-action
                                                              :path action-path
                                                              :from (->> data-patch (keys) (select-keys action-data))
                                                              :to   data-patch}]))
                        (remove nil?))})))

(re-frame/reg-event-fx
  ::update-object
  (fn [{:keys [db]} [_ object-path data-patch]]
    (let [objects-data (objects-data db)
          object-data (get-in objects-data object-path)
          updated-data (merge object-data data-patch)]
      {:db         (assoc-in db (path-to-db (concat [:scene :data :objects] object-path)) updated-data)
       :dispatch-n (list [::set-changes])})))
