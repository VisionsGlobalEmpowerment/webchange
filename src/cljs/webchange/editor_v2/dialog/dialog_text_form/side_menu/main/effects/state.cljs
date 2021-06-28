(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state-dialog]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:effects])
       (parent-state/path-to-db)))

;; Selected Effect

(def selected-effect-path (path-to-db [:selected-effect]))

(defn- get-selected-effect
  [db]
  (get-in db selected-effect-path))

(re-frame/reg-sub
  ::selected-effect
  get-selected-effect)

(re-frame/reg-event-fx
  ::set-selected-effect
  (fn [{:keys [db]} [_ effect-id]]
    {:db (assoc-in db selected-effect-path effect-id)}))

(re-frame/reg-sub
  ::available-effects
  (fn []
    [(re-frame/subscribe [::parent-state/available-effects])
     (re-frame/subscribe [::selected-effect])])
  (fn [[available-effects selected-effect]]
    (map (fn [effect]
           {:text      (clojure.string/replace effect "-" " ")
            :value     effect
            :selected? (= effect selected-effect)})
         available-effects)))

;; Actions

(re-frame/reg-event-fx
  ::add-current-effect-action
  (fn [{:keys [db]} [_ relative-position]]
    (let [effect (get-selected-effect db)
          {:keys [node-data]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/add-effect-action {:node-data         node-data
                                                     :effect            effect
                                                     :relative-position relative-position}]})))
