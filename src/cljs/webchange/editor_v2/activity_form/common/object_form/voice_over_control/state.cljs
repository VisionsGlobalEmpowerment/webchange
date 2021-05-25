(ns webchange.editor-v2.activity-form.common.object-form.voice-over-control.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.voice-over-control.utils :refer [get-actions-data]]
    [webchange.editor-v2.dialog.state.window :as dialog.window]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.logger.index :as logger]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:voice-over])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id object-name]]
    (let [actions-data (get-actions-data db object-name)]
      (print "---  ::init actions-data" actions-data)
      {:dispatch [::set-action-data id actions-data]})))

;; Action Data

(def action-data-path :action-data)

(re-frame/reg-event-fx
  ::set-action-data
  (fn [{:keys [db]} [_ id action-data]]
    {:db (assoc-in db (path-to-db id [action-data-path]) action-data)}))

(defn- get-action-data
  [db id]
  (get-in db (path-to-db id [action-data-path])))

(re-frame/reg-sub
  ::action-data
  (fn [db [_ id]]
    (get-action-data db id)))

;; Button

(re-frame/reg-sub
  ::visible?
  (fn [[_ id]]
    [(re-frame/subscribe [::action-data id])])
  (fn [[action-data]]
    (some? action-data)))

;; Dialog

(re-frame/reg-event-fx
  ::open-dialog-window
  (fn [{:keys [db]} [_ id]]
    (let [{:keys [dialog-action-name phrase-action-path ]} (get-action-data db id)
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
