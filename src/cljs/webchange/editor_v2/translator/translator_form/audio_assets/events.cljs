(ns webchange.editor-v2.translator.translator-form.audio-assets.events
  (:require
    [ajax.core :refer [json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as es]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.events :as add-audio-events]
    [webchange.editor-v2.translator.translator-form.audio-assets.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.audio-assets.utils :refer [get-action-audio-data
                                                                               get-concept-actions
                                                                               get-concept-scene-actions]]
    [webchange.subs :as ws]))

(defn- list->map
  [list]
  (->> list
       (reduce (fn [result {:keys [url] :as asset}]
                 (assoc result url asset))
               {})))

(defn- get-scene-audio-assets-map
  [scene-assets scene-audio]
  (let [scene-audio-map (reduce (fn [result [key value]]
                                  (assoc result value (name key)))
                                {} scene-audio)]
    (->> scene-assets
         (filter (fn [{:keys [type]}]
                   (= type "audio")))
         (map (fn [{:keys [url] :as asset}]
                (if (contains? scene-audio-map url)
                  (->> url
                       (get scene-audio-map)
                       (assoc asset :id))
                  asset)))
         (list->map))))

(defn- get-concepts-audio-assets-map
  [concepts-actions]
  (->> concepts-actions
       (map get-action-audio-data)
       (flatten)
       (list->map)))

(defn- fix-assets-data
  [assets-map]
  (let [assoc-when-nil (fn [data key value]
                         (if (nil? (get data key))
                           (assoc data key value)
                           data))]
    (->> assets-map
         (map (fn [[asset-key asset-data]]
                [asset-key (-> asset-data
                               (assoc-when-nil :alias "alias not defied")
                               (assoc-when-nil :type "audio"))]))
         (into {}))))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::init-assets-data]
                       [::add-audio-events/init-state])}))

(re-frame/reg-event-fx
  ::init-assets-data
  (fn [{:keys [db]} [_]]
    (let [scene-id (ws/current-scene db)
          concepts (es/course-dataset-items db)
          concepts-actions (->> (get-concept-scene-actions (es/course-concept db)
                                                           (ws/current-scene db))
                                (get-concept-actions (vals concepts)))
          concepts-audio-assets-map (get-concepts-audio-assets-map concepts-actions)
          scene-assets (ws/scene-assets db scene-id)
          scene-audio (ws/scene-audio db scene-id)
          scene-audio-assets-map (get-scene-audio-assets-map scene-assets scene-audio)]
      {:db (assoc-in db (path-to-db [:data]) (-> (merge concepts-audio-assets-map
                                                        scene-audio-assets-map)
                                                 (fix-assets-data)))})))

(re-frame/reg-event-fx
  ::add-asset
  (fn [{:keys [db]} [_ asset-key asset-data]]
    {:db           (assoc-in db (path-to-db [:data asset-key]) asset-data)
     :reload-asset asset-data}))

(re-frame/reg-event-fx
  ::patch-asset
  (fn [{:keys [db]} [_ asset-key data-patch]]
    {:db (update-in db (path-to-db [:data asset-key]) merge data-patch)}))

(re-frame/reg-event-fx
  ::delete-asset
  (fn [{:keys [db]} [_ asset-key]]
    {:db (update-in db (path-to-db [:data]) dissoc asset-key)}))

(defn- get-form-data
  [form-params]
  (reduce (fn [form-data [key value]]
            (.append form-data key value)
            form-data)
          (js/FormData.)
          form-params))

(re-frame/reg-event-fx
  ::upload-audio
  (fn [{:keys [db]} [_ js-file-value audio-props form-params]]
    (let [form-data (get-form-data (concat [["file" js-file-value]] (or form-params [])))
          asset-data {:date (.now js/Date)}]
      {:db         (assoc-in db [:loading :upload-audio] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/assets/")
                    :body            form-data
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::upload-audio-success (merge audio-props asset-data)]
                    :on-failure      [:api-request-error :upload-audio]}})))

(re-frame/reg-event-fx
  ::upload-audio-success
  (fn [_ [_ audio-props data]]
    (let [asset-data (merge audio-props data)]
      {:dispatch-n (list [:complete-request :upload-audio]
                         [::add-asset (:url data) asset-data])})))

