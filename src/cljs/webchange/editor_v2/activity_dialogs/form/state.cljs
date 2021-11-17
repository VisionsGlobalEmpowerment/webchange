(ns webchange.editor-v2.activity-dialogs.form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.utils :refer [collect-untracked-actions prepare-phrase-actions]]
    [webchange.editor-v2.activity-dialogs.form.state-actions :as state-actions]
    [webchange.editor-v2.state :as parent-state]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-dialog-form]
    [webchange.editor-v2.dialog.utils.dialog-action :as defaults]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.utils.flipbook :as flipbook-utils :refer [flipbook-activity?]]
    [webchange.utils.scene-action-data :as action-data-utils]
    [webchange.utils.scene-data :as scene-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:activity-script])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::translator-form.form/init-state]}))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-selected-action]]}))

;; Object data

(re-frame/reg-sub
  ::object-data
  (fn []
    [(re-frame/subscribe [::translator-form.scene/objects-data])])
  (fn [[objects-data] [_ object-name]]
    (get objects-data (keyword object-name))))

;; Track

(re-frame/reg-sub
  ::untracked-actions
  (fn []
    (re-frame/subscribe [::translator-form.scene/scene-data]))
  (fn [scene-data]
    (collect-untracked-actions scene-data)))

(re-frame/reg-sub
  ::available-tracks
  (fn []
    [(re-frame/subscribe [::translator-form.scene/scene-data])
     (re-frame/subscribe [::untracked-actions])])
  (fn [[scene-data untracked-actions]]
    (->> (cond-> (scene-utils/get-tracks scene-data)
                 (not (empty? untracked-actions)) (conj {:title "Untracked"}))
         (map-indexed (fn [idx {:keys [title]}]
                        {:text title
                         :idx  idx})))))

(def current-track-path (path-to-db [:current-track]))

(re-frame/reg-sub
  ::current-track
  (fn [db]
    (get-in db current-track-path 0)))

(re-frame/reg-event-fx
  ::set-current-track
  (fn [{:keys [db]} [_ track-idx]]
    {:db (assoc-in db current-track-path track-idx)}))

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

;; Dialog data

(re-frame/reg-sub
  ::user-interactions-blocked?
  (fn []
    [(re-frame/subscribe [::translator-form.scene/actions-data])])
  (fn [[actions-data] [_ action-path]]
    (->> (get-in actions-data action-path)
         (:tags)
         (some #{(:user-interactions-blocked action-data-utils/action-tags)})
         (boolean))))

(re-frame/reg-event-fx
  ::set-user-interactions-block
  (fn [{:keys [db]} [_ action-path value]]
    (let [block-interactions-tag (:user-interactions-blocked action-data-utils/action-tags)
          current-dialog-action-data (translator-form.actions/current-dialog-action-data db)
          tags (-> (get-in current-dialog-action-data action-path)
                   (get :tags []))
          new-tags (if value
                     (-> tags (conj block-interactions-tag) (distinct) (vec))
                     (->> tags (remove #(= % block-interactions-tag)) (vec)))]
      {:dispatch [::state-dialog-form/update-action-by-path {:action-path action-path
                                                             :action-type :scene
                                                             :data-patch  {:tags new-tags}}]})))

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

;; Dialogs List

(re-frame/reg-sub
  ::dialogs-to-show
  (fn []
    [(re-frame/subscribe [::current-track])
     (re-frame/subscribe [::translator-form.scene/scene-data])
     (re-frame/subscribe [::untracked-actions])])
  (fn [[current-track-idx scene-data untracked-actions]]
    (let [current-track (scene-utils/get-track-by-index scene-data current-track-idx)]
      (if (some? current-track)
        (->> (:nodes current-track)
             (filter (fn [{:keys [type]}]
                       (some #{type} ["dialog" "prompt"])))
             (map (fn [{:keys [action-id text type]}]
                    (cond-> {:type type}
                            (= type "dialog") (assoc :action-path [(keyword action-id)])
                            (= type "prompt") (assoc :title text)))))
        (map (fn [action-name]
               {:type        "dialog"
                :action-path [(keyword action-name)]})
             untracked-actions)))))

(defn- action-name->page-number
  [action-name pages-data]
  (->> pages-data
       (map-indexed vector)
       (some (fn [[idx {:keys [action]}]]
               (and (= action action-name) idx)))))

(defn- dialog-data->page-number
  [dialog-data pages-data]
  (-> (get dialog-data :action-path)
      (first)
      (clojure.core/name)
      (action-name->page-number pages-data)))

; flipbook-utils

(re-frame/reg-sub
  ::script-data
  (fn []
    [(re-frame/subscribe [::dialogs-to-show])
     (re-frame/subscribe [::translator-form.concepts/current-concept])
     (re-frame/subscribe [::translator-form.scene/scene-data])
     (re-frame/subscribe [::selected-action])])
  (fn [[dialogs-paths current-concept scene-data selected-action]]
    (let [script-data (map (fn [{:keys [action-path title type]}]
                             (case type
                               "dialog" (let [{:keys [available-activities phrase-description]} (get-in scene-data (concat [:actions] action-path))
                                              available-actions (->> (scene-utils/get-available-effects scene-data)
                                                                     (concat available-activities))]
                                          {:type        "dialog"
                                           :title       phrase-description
                                           :action-path action-path
                                           :nodes       (prepare-phrase-actions {:dialog-action-path  action-path
                                                                                 :concept-data        current-concept
                                                                                 :scene-data          scene-data
                                                                                 :available-effects   available-actions
                                                                                 :current-action-path (:path selected-action)})})
                               "prompt" {:type  "prompt"
                                         :title title}))
                           dialogs-paths)]
      (cond
        (flipbook-activity? scene-data) (let [pages-data (flipbook-utils/get-pages-data scene-data)]
                                          (->> script-data
                                               (filter (fn [dialog-data]
                                                         (-> (dialog-data->page-number dialog-data pages-data)
                                                             (some?))))
                                               (sort (fn [dialog-1 dialog-2]
                                                       (< (or (dialog-data->page-number dialog-1 pages-data) ##Inf)
                                                          (or (dialog-data->page-number dialog-2 pages-data) ##Inf))))))
        :else script-data))))

;; Actions

(re-frame/reg-event-fx
  ::handle-drag-n-drop
  (fn [{:keys [_]} [_ {:keys [action] :as data}]]
    (case action
      "add-effect-action" {:dispatch [::add-effect-action data]}
      "set-target-animation" {:dispatch [::set-target-animation data]}
      "remove-target-animation" {:dispatch [::remove-target-animation data]}
      "start-skip-region" {:dispatch [::start-skip-region data]}
      "end-skip-region" {:dispatch [::end-skip-region data]}
      "mute-background-music" {:dispatch [::mute-background-music data]}
      "unmute-background-music" {:dispatch [::unmute-background-music data]}
      "add-movement" {:dispatch [::add-movement data]}
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
          action-data (defaults/get-remove-emotion-action-data {:target target
                                                                :track  track})]
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

(re-frame/reg-event-fx
  ::mute-background-music
  (fn [{:keys [_]} [_ data]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-dialog-node {:type "mute-background-music"})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::unmute-background-music
  (fn [{:keys [_]} [_ data]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-dialog-node {:type "unmute-background-music"})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))

(re-frame/reg-event-fx
  ::add-movement
  (fn [{:keys [_]} [_ {:keys [character movement target] :as data}]]
    (let [position-data (get-action-position-data data)
          action-data (defaults/get-movement-action-data {:action    movement
                                                          :character character
                                                          :target    target})]
      {:dispatch [::state-dialog-form/insert-action (merge {:action-data action-data}
                                                           position-data)]})))
