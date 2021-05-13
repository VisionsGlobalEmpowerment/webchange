(ns webchange.editor-v2.activity-form.flipbook.asset-form.text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.flipbook.asset-form.state :refer [path-to-db] :as state]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.logger.index :as logger]
    [webchange.state.state-fonts :as fonts]
    [webchange.utils.text :refer [text->chunks]]))

;; Dialog action name

(def dialog-action-name-path :dialog-action-name)

(defn- set-dialog-action-name
  [db id value]
  {:pre [(some? id)]}
  (assoc-in db (path-to-db id [dialog-action-name-path]) value))

(defn- get-dialog-action-name
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [dialog-action-name-path])))

;; Phrase action name

(def phrase-action-path :phrase-action-path)

(defn- set-phrase-action-path
  [db id value]
  {:pre [(some? id)]}
  (assoc-in db (path-to-db id [phrase-action-path]) value))

(defn- get-phrase-action-path
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id [phrase-action-path])))

;; Init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id object-data]]
    (let [text-name (get-in object-data [:text :name])
          text-data (-> object-data
                        (get-in [:text :data])
                        (select-keys [:text :chunks :font-size :font-family]))
          dialog-action-name (-> object-data :action keyword)
          phrase-action-path (-> object-data :phrase-action-path)]
      {:db       (-> db
                     (set-dialog-action-name id dialog-action-name)
                     (set-phrase-action-path id phrase-action-path))
       :dispatch [::state/init id {:assets-names [text-name]
                                   :data         text-data}]})))


;; Current Text

(re-frame/reg-sub
  ::current-text
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :text "")))

(re-frame/reg-event-fx
  ::set-current-text
  (fn [{:keys [_]} [_ id text]]
    (let [chunks (text->chunks text)]
      {:dispatch [::state/update-current-data id {:text   text
                                                  :chunks chunks}]})))

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

;; Text Animation Action

(re-frame/reg-event-fx
  ::open-dialog-window
  (fn [{:keys [db]} [_ id]]
    (let [dialog-action-name (get-dialog-action-name db id)
          phrase-action-path (get-phrase-action-path db id)
          action-node {:path [dialog-action-name]}
          phrase-node {:path phrase-action-path}

          window-options {:components     {:description  {:hide? true}
                                           :node-options {:hide? true}
                                           :target       {:hide? true}
                                           :phrase       {:hide? true}
                                           :play-phrase  {:hide? true}
                                           :diagram      {:hide? true}}
                          :single-phrase? true}]

      (logger/group-folded "Open voice-over window")
      (logger/trace "dialog-action-name" dialog-action-name)
      (logger/trace "phrase-action-path" phrase-action-path)
      (logger/trace "action-node" action-node)
      (logger/trace "phrase-node" phrase-node)
      (logger/group-end "Open voice-over window")

      {:dispatch-n [[::translator-form.actions/set-current-dialog-action action-node]
                    [::translator-form.actions/set-current-phrase-action phrase-node]
                    [::dialog.window/open window-options]]})))
