(ns webchange.editor-v2.text-animation-editor.chunks-editor.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :translator :text])))

(def modal-state-path (path-to-db [:configuration-modal-state]))
(def current-text-info-path (path-to-db [:current-text-info]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::translator-form/init-state]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path false)
     :dispatch [::translator-form/reset-state]}))

(re-frame/reg-event-fx
  ::set-current-dialog-text
  (fn [{:keys [db]} [_ text-object-info]]
    {:db (assoc-in db current-text-info-path text-object-info)}))

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::current-dialog-text-info
  (fn [db]
    (get-in db current-text-info-path)))

(re-frame/reg-sub
  ::current-dialog-text
  (fn []
    [(re-frame/subscribe [::current-dialog-text-info])
     (re-frame/subscribe [::translator-form.scene/objects-data])])
  (fn [[{:keys [path]} objects]]
    (get-in objects path)))

(re-frame/reg-event-fx
  ::update-text-data
  (fn [{:keys [db]} [_ text-data-patch]]
    (let [{path :path} (get-in db current-text-info-path)]
      {:dispatch [::translator-form.scene/update-object path text-data-patch]})))

(re-frame/reg-event-fx
  ::save
  (fn [_ _]
    {:dispatch-n (list [::translator-form/save-changes] [::close])}))
