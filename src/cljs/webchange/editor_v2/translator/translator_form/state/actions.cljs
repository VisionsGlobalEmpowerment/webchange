(ns webchange.editor-v2.translator.translator-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer :as audio-analyzer]
    [webchange.utils.text :refer [text->chunks]]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.actions-shared :as actions-shared]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.subs :as subs]))

(defn- get-object-by-name
  [db object-name]
  (-> (translator-form.scene/objects-data db)
      (get (keyword object-name))))

;; Subs

(def current-dialog-action-info actions-shared/current-dialog-action-info)

(re-frame/reg-sub
  ::current-dialog-action-info
  actions-shared/current-dialog-action-info)

(re-frame/reg-sub
  ::current-dialog-action-data
  (fn []
    [(re-frame/subscribe [::current-dialog-action-info])
     (re-frame/subscribe [::subs/current-scene-actions])])
  (fn [[{:keys [path]} actions]]
    (get-in actions path)))

(re-frame/reg-sub
  ::current-dialog-action
  (fn []
    [(re-frame/subscribe [::current-dialog-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])])
  (fn [[{:keys [path]} actions]]
    (get-in actions path)))

(re-frame/reg-sub
  ::current-dialog-action-name
  (fn []
    [(re-frame/subscribe [::current-dialog-action-info])])
  (fn [[{:keys [path]}]]
    (first path)))

(defn current-phrase-action-info
  [db]
  (get-in db (path-to-db [:current-phrase-action])))

(re-frame/reg-sub
  ::current-phrase-action-info
  current-phrase-action-info)

(defn current-phrase-action
  ([db]
   (let [phrase-action-info (current-phrase-action-info db)
         actions-data (translator-form.scene/actions-data db)
         current-concept (translator-form.concepts/current-concept-data db)]
     (current-phrase-action phrase-action-info actions-data current-concept)))
  ([{:keys [path type]} actions-data current-concept]
   (let [action-path (actions/node-path->action-path path)]
     (when-not (nil? action-path)
       (cond
         (= type :concept-action) (get-in current-concept action-path)
         (= type :scene-action) (get-in actions-data action-path)
         :else nil)))))

(re-frame/reg-sub
  ::current-phrase-action
  (fn []
    [(re-frame/subscribe [::current-phrase-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept-data])])
  (fn [[phrase-action-info actions-data current-concept]]
    (current-phrase-action phrase-action-info actions-data current-concept)))

(re-frame/reg-sub
  ::phrase-node-available-actions
  (fn []
    [(re-frame/subscribe [::translator-form.concepts/current-concept])])
  (fn [[current-concept] [_ node]]
    (if (actions/available-to-edit-actions? node current-concept)
      [:insert-before :insert-after :delete]
      [])))

;; Events


(defn get-action-path-data
  ([db target-action]
   (get-action-path-data db target-action nil))
  ([db target-action sub-path]
   (let [{:keys [path type]} (cond
                               (= target-action :dialog) (current-dialog-action-info db)
                               (= target-action :phrase) (current-phrase-action-info db))
         action-path (cond-> (actions/node-path->action-path path)
                             (some? sub-path) (concat sub-path))]
     [action-path type])))

(defn get-action-data
  [db path-data]
  (let [[action-path type] path-data]
    (cond
      (= type :concept-action) (get-in (translator-form.concepts/current-concept db) (concat [:data] action-path))
      (= type :scene-action) (get-in (translator-form.scene/actions-data db) action-path))))

(defn text-animation?
  [action]
  (= (:type action) "text-animation"))

(defn question-audio-data?
  [action]
  (not (:type action)))

(defn update-text-animation-data?
  [action]
  (or (question-audio-data? action) (text-animation? action)))

(defn animation-sequence?
  [action]
  (= (:type action) "animation-sequence"))

(defn update-talk-animation-data?
  [action]
  (animation-sequence? action))

(defn current-phrase-action-animation-sequence?
  ([db target-action]
   (current-phrase-action-animation-sequence? db target-action nil))
  ([db target-action sub-path]
   (let [action-path-data (get-action-path-data db target-action sub-path)
         action (get-action-data db action-path-data)]
     (animation-sequence? action))))

(defn get-phrase-text
  [db action action-path-data]
  (cond
    (question-audio-data? action) (:text (get-action-data db (update action-path-data 0 drop-last)))
    (text-animation? action) (:text (get-object-by-name db (:target action)))
    (:phrase-text-translated action) (:phrase-text-translated action)
    (:phrase-text action) (:phrase-text action)
    :else nil))

(re-frame/reg-event-fx
  ::update-phrase-region-data
  (fn [{:keys [db]} [_ audio-url sub-path force]]
    (let [action-path-data (get-action-path-data db :phrase sub-path)
          action (get-action-data db action-path-data)]
      (if (or force (and (= (:audio action) audio-url) (not (and (:start action) (:duration action)))))
        (let [phrase-text (get-phrase-text db action action-path-data)
              region-data (audio-analyzer/get-region-data-if-possible
                            phrase-text
                            audio-url)]
          (if (contains? region-data :end)
            {:dispatch-n (list [::update-action :phrase
                                (cond-> {:audio    audio-url
                                         :start    (:start region-data)
                                         :duration (- (:end region-data) (:start region-data))
                                         :end      (:end region-data)}
                                        (update-text-animation-data? action)
                                        (assoc :data
                                               (audio-analyzer/get-chunks-data-if-possible
                                                 phrase-text audio-url region-data))

                                        (update-talk-animation-data? action)
                                        (assoc :data
                                               (audio-analyzer/get-talk-data-if-possible
                                                 phrase-text audio-url region-data))
                                        ) sub-path])}))))))

(re-frame/reg-event-fx
  ::update-action
  (fn [{:keys [db]} [_ target-action data-patch sub-path]]
    (let [[action-path type] (get-action-path-data db target-action sub-path)]
      (cond
        (= type :concept-action) {:dispatch-n (list [::translator-form.concepts/update-current-concept action-path data-patch])}
        (= type :scene-action) {:dispatch-n (list [::translator-form.scene/update-action action-path data-patch])}))))

(re-frame/reg-event-fx
  ::update-metadata
  (fn [{:keys [db]} [_ key data]]
    {:db         (assoc-in db (path-to-db (concat [:scene :data :metadata] [key])) data)
     :dispatch-n (list [::translator-form.scene/set-changes])}))

;; Dialog Action

(re-frame/reg-event-fx
  ::set-current-dialog-action
  (fn [{:keys [db]} [_ action-node]]
    (let [action-info (if-not (nil? action-node)
                        (actions/node->info action-node)
                        nil)]
      {:db (assoc-in db (path-to-db [:current-dialog-action]) action-info)})))

(re-frame/reg-event-fx
  ::set-dialog-action-description-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-action :dialog {:phrase-description-translated text}])}))

;; Phrase Action

(re-frame/reg-event-fx
  ::set-current-phrase-action
  (fn [{:keys [db]} [_ action-node]]
    (let [action-info (if-not (nil? action-node)
                        (actions/node->info action-node)
                        nil)]
      {:db (assoc-in db (path-to-db [:current-phrase-action]) action-info)})))

(re-frame/reg-event-fx
  ::init-current-phrase-action
  (fn [{:keys [db]} [_ action-node]]
    (let [phrase-action-info (current-phrase-action-info db)]
      (when (nil? phrase-action-info)
        {:dispatch-n (list [::set-current-phrase-action action-node])}))))

(re-frame/reg-event-fx
  ::set-text-animation-target
  (fn [{:keys [db]} [_ object-name]]
    (let [target-data (get-object-by-name db object-name)]
      (if (contains? target-data :chunks)
        {:dispatch [::update-action :phrase {:target object-name}]}
        (let [chunks (text->chunks (:text target-data))]
          {:dispatch-n (list [::update-action :phrase {:target object-name}]
                             [::translator-form.scene/update-object [(keyword object-name)] {:chunks chunks}])})))))

(re-frame/reg-event-fx
  ::set-phrase-action-phrase
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-action :phrase {:phrase-text text}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-phrase-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-action :phrase {:phrase-text-translated text}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-target
  (fn [{:keys [_]} [_ target]]
    {:dispatch-n (list [::update-action :phrase {:target target}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list
                   [::update-phrase-region-data audio-url nil true]
                   [::update-action :phrase {:audio audio-url}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio-region
  (fn [{:keys [db]} [_ audio-url start duration]]
    (let [region-data {:audio    audio-url
                       :start    start
                       :duration duration}]
      {:dispatch-n (list [::update-action :phrase region-data]
                         (if (current-phrase-action-animation-sequence? db :phrase)
                         [::load-lip-sync-data audio-url start duration]
                         ))})))

(re-frame/reg-event-fx
  ::load-lip-sync-data
  (fn [{:keys [db]} [_ audio-url start duration]]
    {:db         (assoc-in db [:loading :load-lip-sync-data] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/actions/get-talk-animations")
                  :url-params      {:file     audio-url
                                    :start    start
                                    :duration duration}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-lip-sync-data-success]
                  :on-failure      [::load-lip-sync-data-failure start duration]}}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-success
  (fn [{:keys [_]} [_ lip-sync-data]]
    {:dispatch-n (list [:complete-request :load-lip-sync-data]
                       [::update-action :phrase {:data lip-sync-data}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list
                     [:api-request-error :load-lip-sync-data]
                     [::update-metadata :lip-not-sync true]
                     [::update-action :phrase {:data default-lip-sync-data}])})))

(def empty-action {:type        "animation-sequence"
                   :phrase-text "New action"
                   :audio       nil})

(re-frame/reg-event-fx
  ::add-new-phrase-action
  (fn [{:keys [db]} [_ node relative-position]]
    (let [current-concept (translator-form.concepts/current-concept db)
          {:keys [concept-action? parent-action parent-path target-position]} (actions/get-node-data node current-concept)
          data-patch (-> parent-action
                         (actions/insert-child-action empty-action target-position relative-position)
                         (select-keys [:data]))]
      (if concept-action?
        {:dispatch-n (list [::translator-form.concepts/update-current-concept parent-path data-patch])}))))

(re-frame/reg-event-fx
  ::delete-phrase-action
  (fn [{:keys [db]} [_ node]]
    (let [current-concept (translator-form.concepts/current-concept db)
          {:keys [concept-action? parent-action parent-path target-position]} (actions/get-node-data node current-concept)
          data-patch (-> parent-action
                         (actions/delete-child-action target-position)
                         (select-keys [:data]))]
      (if concept-action?
        {:dispatch-n (list [::translator-form.concepts/update-current-concept parent-path data-patch])}))))
