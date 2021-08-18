(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.emotions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.state :as parent-state]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state-dialog]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-translator]
    [webchange.state.warehouse-animations :as warehouse-animations]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:emotions])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse-animations/load-available-animation]}))

;; Target

(re-frame/reg-sub
  ::available-characters-options
  (fn []
    [(re-frame/subscribe [::state-translator/available-animation-targets])])
  (fn [[available-animation-targets]]
    (->> available-animation-targets
         (map (fn [target]
                {:text  target
                 :value target})))))

(def current-target-path (path-to-db [:current-target]))

(re-frame/reg-sub
  ::current-target
  (fn [db]
    (get-in db current-target-path)))

(re-frame/reg-event-fx
  ::set-current-target
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-target-path value)}))

(re-frame/reg-sub
  ::current-target-data
  (fn []
    [(re-frame/subscribe [::current-target])
     (re-frame/subscribe [::state-translator/objects-data])])
  (fn [[current-target objects-data]]
    (->> (keyword current-target)
         (get objects-data))))

;; Emotions

(re-frame/reg-sub
  ::show-emotions?
  (fn []
    [(re-frame/subscribe [::current-target])])
  (fn [[current-target-name]]
    (some? current-target-name)))

(re-frame/reg-sub
  ::available-emotions
  (fn []
    [(re-frame/subscribe [::current-target])
     (re-frame/subscribe [::current-target-data])
     (re-frame/subscribe [::warehouse-animations/available-animations])])
  (fn [[current-target-name current-target-data available-animations]]
    (let [animation-name (:name current-target-data)
          animation-data (some (fn [{:keys [name] :as data}]
                                 (and (= name animation-name) data))
                               available-animations)]
      (-> (map (fn [{:keys [animation emotion]}]
                 {:text      emotion
                  :target    current-target-name
                  :animation animation})
               (get animation-data :emotions []))
          (vec)
          (conj {:text      "Reset emotions"
                 :target    current-target-name
                 :animation "reset"})))))
