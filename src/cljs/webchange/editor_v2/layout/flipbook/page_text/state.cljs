(ns webchange.editor-v2.layout.flipbook.page-text.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.layout.flipbook.state :as db]
    [webchange.utils.text :refer [parts->chunks]]
    [webchange.editor-v2.layout.components.activity-stage.screenshots :as screenshots]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.logger.index :as logger]
    [webchange.state.state :as state]))

(defn path-to-db
  ([id]
   (path-to-db id []))
  ([id relative-path]
   (->> relative-path
        (concat [:page-text-control id])
        (db/path-to-db))))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id {:keys [data dialog-action-name phrase-action-path text-object-name]}]]
    {:db       (-> db
                   (assoc-in (path-to-db id [:text-object-name]) text-object-name)
                   (assoc-in (path-to-db id [:dialog-action-name]) dialog-action-name)
                   (assoc-in (path-to-db id [:phrase-action-path]) phrase-action-path)
                   (assoc-in (path-to-db id [:current-data]) data))
     :dispatch [::set-initial-data id data]}))

(defn- get-text-object-name
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [:text-object-name])))

(defn- get-dialog-action-name
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [:dialog-action-name])))

(defn- get-phrase-action-path
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [:phrase-action-path])))

(defn- get-initial-data
  [db id]
  (get-in db (path-to-db id [:initial-data]) {}))

(re-frame/reg-event-fx
  ::set-initial-data
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db (path-to-db id [:initial-data]) data)}))

(defn- get-current-data
  [db id]
  (get-in db (path-to-db id [:current-data]) {}))

(re-frame/reg-sub
  ::current-data
  (fn [db [_ id]]
    {:pre [(some? id)]}
    (get-current-data db id)))

(re-frame/reg-sub
  ::current-text
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])])
  (fn [[current-data]]
    (get current-data :text "")))

(re-frame/reg-event-fx
  ::set-current-text
  (fn [{:keys [db]} [_ id text]]
    (let [text-object-name (get-text-object-name db id)
          parts (clojure.string/split text #" ")
          chunks (parts->chunks text parts)]
      {:db       (-> db
                     (assoc-in (path-to-db id [:current-data :text]) text)
                     (assoc-in (path-to-db id [:current-data :chunks]) chunks))
       :dispatch [::scene/set-scene-object-state text-object-name {:text text}]})))

(re-frame/reg-sub
  ::current-font-size
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])])
  (fn [[current-data]]
    (get current-data :font-size 18)))

(re-frame/reg-sub
  ::current-font-family
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])])
  (fn [[current-data]]
    (get current-data :font-family)))

(re-frame/reg-event-fx
  ::set-current-font-size
  (fn [{:keys [db]} [_ id font-size]]
    (let [text-object-name (get-text-object-name db id)]
      {:db       (assoc-in db (path-to-db id [:current-data :font-size]) font-size)
       :dispatch [::scene/set-scene-object-state text-object-name {:font-size font-size}]})))

(re-frame/reg-event-fx
  ::set-current-font-family
  (fn [{:keys [db]} [_ id font-family]]
    (let [text-object-name (get-text-object-name db id)]
      {:db       (assoc-in db (path-to-db id [:current-data :font-family]) font-family)
       :dispatch [::scene/set-scene-object-state text-object-name {:font-family font-family}]})))

(re-frame/reg-event-fx
  ::open-dialog-window
  (fn [{:keys [db]} [_ id window-options]]
    (let [dialog-action-name (get-dialog-action-name db id)
          phrase-action-path (get-phrase-action-path db id)
          action-node {:path [dialog-action-name]}
          phrase-node {:path phrase-action-path}]

      (logger/group-folded "Open voice-over window")
      (logger/trace "dialog-action-name" dialog-action-name)
      (logger/trace "phrase-action-path" phrase-action-path)
      (logger/trace "action-node" action-node)
      (logger/trace "phrase-node" phrase-node)
      (logger/group-end "Open voice-over window")

      {:dispatch-n [[::translator-form.actions/set-current-dialog-action action-node]
                    [::translator-form.actions/set-current-phrase-action phrase-node]
                    [::dialog.window/open window-options]]})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_ id]]
    (let [initial-data (get-initial-data db id)
          text-object-name (get-text-object-name db id)]
      {:db       (-> db
                     (update-in (path-to-db id []) dissoc :text-object-name)
                     (update-in (path-to-db id []) dissoc :dialog-action-name)
                     (update-in (path-to-db id []) dissoc :phrase-action-path)
                     (update-in (path-to-db id []) dissoc :current-data))
       :dispatch [::scene/set-scene-object-state text-object-name initial-data]})))

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

(re-frame/reg-sub
  ::has-changes?
  (fn [db [_ id]]
    (let [current-data (get-current-data db id)
          initial-data (get-initial-data db id)]
      (not= current-data initial-data))))

(re-frame/reg-event-fx
  ::set-loading-status
  (fn [{:keys [db]} [_ id status]]
    {:db (assoc-in db (path-to-db id [:loading-status]) status)}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ id]]
    (let [text-object-name (get-text-object-name db id)
          changed-data (get-current-data db id)]
      {:dispatch-n [[::set-loading-status id :loading]
                    [::state/update-scene-object {:object-name       text-object-name
                                                  :object-data-patch changed-data}
                     {:on-success [::save-success id changed-data]}]]})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [_]} [_ id data]]
    {:dispatch-n [[::set-initial-data id data]
                  [::set-loading-status id :done]
                  [::screenshots/generate-stages-screenshots]]}))
