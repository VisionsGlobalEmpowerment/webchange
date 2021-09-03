(ns webchange.editor-v2.dialog.dialog-text-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.prepare-phrase-actions :refer [prepare-phrase-actions]]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-actions]
    [webchange.editor-v2.dialog.state :as parent-state]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-dialog-form]
    [webchange.editor-v2.dialog.utils.dialog-action :as defaults]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.utils.scene-data :as scene-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:dialog-text-form])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-selected-action]]}))

;; Selected Action

(def selected-action-path (path-to-db [:selected-action]))

(defn get-selected-action
  [db]
  (get-in db selected-action-path))

(re-frame/reg-sub
  ::selected-action
  get-selected-action)

(re-frame/reg-event-fx
  ::set-selected-action
  (fn [{:keys [db]} [_ action-data]]
    {:db (assoc-in db selected-action-path action-data)}))

(re-frame/reg-event-fx
  ::reset-selected-action
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db selected-action-path nil)}))

;; ---

(re-frame/reg-sub
  ::phrase-actions
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-dialog-action-info])
     (re-frame/subscribe [::translator-form.actions/current-dialog-action-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.scene/scene-data])
     (re-frame/subscribe [::selected-action])])
  (fn [[current-dialog-action {:keys [available-activities]} current-concept scene-data selected-action]]
    (let [available-actions (->> (scene-utils/get-available-effects scene-data)
                                 (concat available-activities))]
      (prepare-phrase-actions {:dialog-action-path  (:path current-dialog-action)
                               :concept-data        current-concept
                               :scene-data          scene-data
                               :available-effects   available-actions
                               :current-action-path (:path selected-action)}))))

(re-frame/reg-sub
  ::scene-available-actions
  (fn []
    [(re-frame/subscribe [::translator-form.scene/scene-data])])
  (fn [[scene-data]]
    (scene-utils/get-available-effects scene-data)))

;; Concepts

(re-frame/reg-sub
  ::show-concepts?
  (fn []
    (re-frame/subscribe [::translator-form.concepts/has-concepts?]))
  (fn [has-concepts?]
    has-concepts?))

(re-frame/reg-sub
  ::current-concept
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/current-concept])])
  (fn [[current-concept]]
    (get current-concept :id "")))

(re-frame/reg-sub
  ::concepts-options
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/concepts-list])])
  (fn [[concepts]]
    (map (fn [{:keys [id name]}]
           {:text  name
            :value id})
         concepts)))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [_]} [_ concept-id]]
    {:dispatch [::translator-form.concepts/set-current-concept concept-id]}))

;; Target

(re-frame/reg-sub
  ::available-targets
  (fn [_]
    [(re-frame/subscribe [::translator-form.scene/available-animation-targets])])
  (fn [[targets]]
    (->> targets
         (map (fn [target]
                {:text  target
                 :value target})))))

(def current-target-path :current-target)

(re-frame/reg-sub
  ::current-target
  (fn [db [_ action-path]]
    (get-in db (path-to-db [current-target-path action-path]))))

(re-frame/reg-event-fx
  ::set-current-target
  (fn [{:keys [db]} [_ action-path target]]
    {:db (assoc-in db (path-to-db [current-target-path action-path]) target)}))

(re-frame/reg-event-fx
  ::set-phrase-action-target
  (fn [{:keys [_]} [_ action-path action-type target]]
    {:dispatch-n [[::set-current-target action-path target]
                  [::state-actions/set-phrase-target {:action-path action-path
                                                      :action-type action-type
                                                      :value       target}]]}))

;; Text animation target

(re-frame/reg-sub
  ::available-text-animation-targets
  (fn [_]
    [(re-frame/subscribe [::translator-form.scene/text-objects])])
  (fn [[targets]]
    (->> targets
         (map (fn [[object-name {:keys [text]}]]
                {:text  (str text " (" (clojure.core/name object-name) ")")
                 :value (clojure.core/name object-name)})))))

(def current-text-animation-target-path :current-target)

(re-frame/reg-sub
  ::current-text-animation-target
  (fn [db [_ action-path]]
    (get-in db (path-to-db [current-text-animation-target-path action-path]))))

(re-frame/reg-event-fx
  ::set-current-text-animation-target
  (fn [{:keys [db]} [_ action-path target]]
    {:db (assoc-in db (path-to-db [current-text-animation-target-path action-path]) target)}))

(re-frame/reg-event-fx
  ::set-text-animation-action-target
  (fn [{:keys [_]} [_ action-path action-type target]]
    {:dispatch-n [[::set-current-text-animation-target action-path target]
                  [::state-actions/set-phrase-target {:action-path action-path
                                                      :action-type action-type
                                                      :value       target}]]}))

;; Actions

(re-frame/reg-event-fx
  ::handle-drag-n-drop
  (fn [{:keys [_]} [_ {:keys [action] :as data}]]
    (case action
      "add-effect-action" {:dispatch [::add-effect-action data]}
      "set-target-animation" {:dispatch [::set-target-animation data]}
      "remove-target-animation" {:dispatch [::remove-target-animation data]}
      "start-skip-region"  {:dispatch [::start-skip-region data]}
      "end-skip-region"  {:dispatch [::end-skip-region data]}
      {})))

(defn- get-action-position-data
  [{:keys [target-path relative-position]}]
  (let [target-parent-action-path (drop-last 2 target-path)
        target-position (last target-path)]
    {:position          target-position
     :parent-path       target-parent-action-path
     :relative-position relative-position}))

(re-frame/reg-event-fx
  ::add-effect-action
  (fn [{:keys [_]} [_ {:keys [id] :as data}]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-effect-action-data {:action-name id})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::set-target-animation
  (fn [{:keys [_]} [_ {:keys [animation target track] :as data}]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-emotion-action-data {:animation animation
                                                         :target    target
                                                         :track     track})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::remove-target-animation
  (fn [{:keys [_]} [_ {:keys [target track] :as data}]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-remove-emotion-action-data {:target    target
                                                                :track     track})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::start-skip-region
  (fn [{:keys [_]} [_ data]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-dialog-node {:type "start-skip-region"})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::end-skip-region
  (fn [{:keys [_]} [_ data]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-dialog-node {:type "end-skip-region"})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))
