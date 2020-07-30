(ns webchange.editor-v2.scene.state.background
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.scene.state.db :refer [path-to-db]]
    [webchange.subs :as subs]
    [webchange.editor.events :as edit-scene]))

(def default-dimension {:width  1920
                        :height 1080})

(defn- src->image-data
  [src]
  {:src   src
   :thumb src})

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [{:keys [type] :as background} (subs/current-scene-background db)
          background-events (case type
                              "background" (list [::set-type :single]
                                                 [::set-layer :single-background (src->image-data (:src background))])
                              "layered-background" (list [::set-type :layered]
                                                         [::set-layer :background (src->image-data (get-in background [:background :src]))]
                                                         [::set-layer :decoration (src->image-data (get-in background [:decoration :src]))]
                                                         [::set-layer :surface (src->image-data (get-in background [:surface :src]))]))]
      {:dispatch-n (concat background-events
                           (list [::set-dimension default-dimension]
                                 [::load-available-background]))})))

;; Background window

(re-frame/reg-sub
  ::window-open?
  (fn [db]
    (get-in db (path-to-db [:window-open?]) false)))

(re-frame/reg-event-fx
  ::open-window
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db (path-to-db [:window-open?]) true)
     :dispatch [::init-state]}))

(re-frame/reg-event-fx
  ::close-window
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:window-open?]) false)}))

;; Gallery

(re-frame/reg-sub
  ::gallery-window-open?
  (fn [db]
    (get-in db (path-to-db [:gallery :open?]) false)))

(re-frame/reg-event-fx
  ::open-gallery-window
  (fn [{:keys [db]} [_ source]]
    {:db (-> db
             (assoc-in (path-to-db [:gallery :open?]) true)
             (assoc-in (path-to-db [:current-layer]) source))}))

(re-frame/reg-event-fx
  ::close-gallery-window
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:gallery :open?]) false)}))

(re-frame/reg-event-fx
  ::load-available-background
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :load-available-background] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/courses/editor/assets")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-available-background-success]
                  :on-failure      [:api-request-error :load-available-background]}}))

(re-frame/reg-event-fx
  ::load-available-background-success
  (fn [{:keys [db]} [_ result]]
    (let [->item (fn [asset] {:src (:path asset) :thumb (:thumbnail-path asset) :type (-> asset :type keyword)})
          gallery (->> result
                      (map ->item)
                      (group-by :type))]
      {:db         (assoc-in db (path-to-db [:gallery]) gallery)
       :dispatch-n (list [:complete-request :load-available-background])})))

(re-frame/reg-sub
  ::gallery
  (fn [db]
    (get-in db (path-to-db [:gallery]) {})))

(defn current-layer [db] (get-in db (path-to-db [:current-layer]) false))
(re-frame/reg-sub ::current-layer current-layer)

(re-frame/reg-sub
  ::current-source-gallery
  (fn []
    [(re-frame/subscribe [::gallery])
     (re-frame/subscribe [::current-layer])])
  (fn [[gallery current-layer]]
    (-> gallery
        (get current-layer [])
        (distinct))))

;; Background state

(re-frame/reg-sub
  ::dimension
  (fn [db]
    (get-in db (path-to-db [:dimension]) default-dimension)))

(re-frame/reg-event-fx
  ::set-dimension
  (fn [{:keys [db]} [_ dimension]]
    {:db (assoc-in db (path-to-db [:dimension]) dimension)}))

(defn background-type
  [db]
  (get-in db (path-to-db [:background :type]) :single))

(re-frame/reg-sub
  ::background-type
  background-type)

(re-frame/reg-event-fx
  ::set-type
  (fn [{:keys [db]} [_ type]]
    {:db (assoc-in db (path-to-db [:background :type]) type)}))

(defn background-layers
  [db]
  (get-in db (path-to-db [:background]) {}))

(re-frame/reg-sub
  ::background-layers
  background-layers)

(re-frame/reg-event-fx
  ::set-layer
  (fn [{:keys [db]} [_ layer image]]
    {:db (assoc-in db (path-to-db [:background layer]) image)}))

(re-frame/reg-event-fx
  ::set-current-layer
  (fn [{:keys [db]} [_ image]]
    (let [current-layer (current-layer db)]
      {:dispatch [::set-layer current-layer image]})))

(re-frame/reg-sub
  ::layer-image
  (fn [db [_ layer]]
    (get-in db (path-to-db [:background layer]) "")))

;; Save

(defn- clean-background-state
  [state]
  (-> state
      (dissoc :name)
      (dissoc :type)
      (dissoc :src)
      (dissoc :background)
      (dissoc :decoration)
      (dissoc :surface)))

(defn- state->single-params
  [editor-state current-background]
  (merge (clean-background-state current-background)
         {:type "background"
          :src  (get-in editor-state [:single-background :src])}))

(defn- state->layered-params
  [editor-state current-background]
  (merge (clean-background-state current-background)
         (reduce (fn [result key]
                   (let [value (get-in editor-state [key :src])]
                     (if-not (empty? value)
                       (assoc-in result [key :src] value)
                       result)))
                 {:type "layered-background"}
                 [:background :decoration :surface])))

(re-frame/reg-event-fx
  ::save-changes
  (fn [{:keys [db]} [_]]
    (let [current-type (background-type db)
          current-layers (background-layers db)
          current-scene (subs/current-scene db)
          background (subs/current-scene-background db)
          background-state (case current-type
                             :single (state->single-params current-layers background)
                             :layered (state->layered-params current-layers background))]
      {:dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                     :target   (:name background)
                                                     :state    background-state}]
                         [::edit-scene/update-current-scene-object {:target (:name background)
                                                                   :state  background-state}]
                         [::edit-scene/save-current-scene current-scene])})))
