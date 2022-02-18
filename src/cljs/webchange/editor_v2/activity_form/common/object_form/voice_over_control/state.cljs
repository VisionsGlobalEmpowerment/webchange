(ns webchange.editor-v2.activity-form.common.object-form.voice-over-control.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state :as activity-dialogs-state]
    [webchange.editor-v2.activity-dialogs.form.utils :refer [prepare-phrase-actions]]
    [webchange.editor-v2.activity-dialogs.menu.state :as menu-state]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.voice-over-control.utils :refer [get-actions-data]]
    [webchange.editor-v2.layout.state :as layout-state]
    [webchange.subs :as subs]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:voice-over])
       (state/path-to-db id)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id object-name]]
    (let [actions-data (get-actions-data db object-name)]
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
    (let [{:keys [dialog-action-name phrase-action-path]} (get-action-data db id)
          action-path (->> phrase-action-path
                           (map (fn [item]
                                  (if (number? item) [:data item] item)))
                           (flatten)
                           (vec))
          scene-data (subs/current-scene-data db)
          action-data nil #_(-> (prepare-phrase-actions {:dialog-action-path  [dialog-action-name]
                                                         :current-action-path action-path
                                                         :scene-data          scene-data})
                                first)]
      {:dispatch-n [[::layout-state/set-activity-dialogs]
                    [::activity-dialogs-state/set-selected-action action-data]
                    [::menu-state/set-current-section :voice-over]]})))
