(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.phrase.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as parent-state]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state-dialog]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:phrase])
       (parent-state/path-to-db)))

;; Current Destination

(def current-destination-path (path-to-db [:current-destination]))

(defn- get-current-destination
  [db]
  (get-in db current-destination-path :scene))

(re-frame/reg-sub
  ::current-destination
  get-current-destination)

(re-frame/reg-event-fx
  ::set-current-destination
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-destination-path value)}))

;; Available Destinations

(re-frame/reg-sub
  ::available-destinations
  (fn []
    [(re-frame/subscribe [::current-destination])
     (re-frame/subscribe [::state-dialog/show-concepts?])])
  (fn [[current-destination show-concepts?]]
    (->> (cond-> [{:text  "main activity"
                   :value :scene}]
                 show-concepts? (conj {:text  "concept"
                                       :value :concept}))
         (map (fn [{:keys [value] :as option}]
                (->> (= value current-destination)
                     (assoc option :selected?)))))))

;; Actions

(re-frame/reg-sub
  ::available-actions
  (fn []
    (re-frame/subscribe [::current-destination]))
  (fn [current-destination]
    (cond-> [:before :after]
            (= current-destination :scene) (conj :parallel))))

(re-frame/reg-event-fx
  ::add-phrase-action
  (fn [{:keys [db]} [_ relative-position]]
    (let [destination (get-current-destination db)
          {:keys [node-data]} (state-dialog/get-selected-action db)
          props {:node-data         node-data
                 :relative-position relative-position}]
      (case destination
        :scene {:dispatch [::state-actions/add-new-empty-phrase-action props]}
        :concept {:dispatch [::state-actions/add-new-empty-phrase-concept-action props]}))))
