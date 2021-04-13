(ns webchange.book-creator.image-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.state :as db]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.state.state :as state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.state.utils :refer [get-scene-object]]))

(defn path-to-db
  ([id]
   (path-to-db id []))
  ([id relative-path]
   (->> relative-path
        (concat [:page-image-control id])
        (db/path-to-db))))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id {:keys [name data spread-image]}]]
    (let [[second-image-name] (if (some? spread-image)
                                (get-scene-object db (fn [object-name {:keys [spread-image-name]}]
                                                       (and (= spread-image-name spread-image)
                                                            (not= object-name name))))
                                [])]
      {:db       (-> db
                     (assoc-in (path-to-db id [:current-data]) data)
                     (assoc-in (path-to-db id [:second-image-name]) second-image-name)
                     (assoc-in (path-to-db id [:image-object-names]) (cond-> [name]
                                                                             (some? second-image-name) (conj second-image-name))))
       :dispatch [::set-initial-data id data]})))

(defn- get-current-data
  [db id]
  (get-in db (path-to-db id [:current-data]) {}))

(defn- get-initial-data
  [db id]
  (get-in db (path-to-db id [:initial-data]) {}))

(re-frame/reg-event-fx
  ::set-initial-data
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db (path-to-db id [:initial-data]) data)}))

(defn- get-image-object-names
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [:image-object-names])))

(re-frame/reg-event-fx
  ::set-image-src
  (fn [{:keys [db]} [_ id image-src]]
    (let [image-names (get-image-object-names db id)]
      {:db         (assoc-in db (path-to-db id [:current-data :src]) image-src)
       :dispatch-n (map (fn [image-name]
                          [::scene/set-scene-object-state image-name {:src image-src}])
                        image-names)})))

(re-frame/reg-sub
  ::loading-status
  (fn [db [_ id]]
    (get-in db (path-to-db id [:loading-status]) :done)))

(re-frame/reg-sub
  ::loading?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::loading-status id])])
  (fn [[loading-status]]
    (= loading-status :loading)))

(re-frame/reg-event-fx
  ::set-loading-status
  (fn [{:keys [db]} [_ id status]]
    {:db (assoc-in db (path-to-db id [:loading-status]) status)}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_ id]]
    (let [initial-data (get-initial-data db id)
          image-names (get-image-object-names db id)]
      {:db         (-> db
                       (update-in (path-to-db id []) dissoc :image-object-name))
       :dispatch-n (map (fn [image-name]
                          [::scene/set-scene-object-state image-name initial-data])
                        image-names)})))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ id]]
    (let [image-names (get-image-object-names db id)
          changed-data (get-current-data db id)]
      {:dispatch-n [[::set-loading-status id :loading]
                    [::state/update-scene-objects {:patches-list (map (fn [image-name]
                                                                        {:object-name       image-name
                                                                         :object-data-patch changed-data})
                                                                      image-names)}
                     {:on-success [::save-success id changed-data]}]]})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [_]} [_ id data]]
    {:dispatch-n [[::set-initial-data id data]
                  [::set-loading-status id :done]
                  [::state-flipbook/generate-stages-screenshots]]}))