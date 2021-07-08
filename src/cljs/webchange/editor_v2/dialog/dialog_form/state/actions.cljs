(ns webchange.editor-v2.dialog.dialog-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.dialog.utils.dialog-action :as defaults]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.logger.index :as logger]))

(def dialog-sub-path [:data 1])

;; Events
(re-frame/reg-event-fx
  ::update-scene-action
  (fn [{:keys [db]} [_ action-path data-patch]]
    {:db (assoc-in db (path-to-db (concat [:scene :data] [:actions] action-path [:data])) (:data data-patch))}))

(re-frame/reg-event-fx
  ::add-new-phrase-parallel-concept-action
  (fn [{:keys [db]} [_ node]]
    (let [{:keys [parent-action base-path base-action target-position]} (actions/get-node-data node)
          concept-var (:concept-var base-action)
          field-name (actions/unique-var-name)
          empty-action {:type "action", :from-var [{:var-name concept-var, :var-property field-name}]}
          concept-schema {:name     field-name
                          :type     "action"
                          :template defaults/default-concept-action}

          list-to-path (if (actions/node-parallel? parent-action)
                         {:type "parallel",
                          :data (vec (concat (:data parent-action) [empty-action]))}
                         {:type "parallel", :data [(:data node) empty-action]})

          data-patch (-> base-action
                         (actions/replace-child list-to-path target-position)
                         (select-keys [:data]))]
      {:dispatch-n (list
                     [::dialog-form.concepts/add-concepts-schema-fields concept-schema]
                     [::translator-form.concepts/update-current-concept [(keyword field-name)] defaults/default-concept-action]
                     [::update-scene-action base-path data-patch])})))

(re-frame/reg-event-fx
  ::add-new-phrase-parallel-action
  (fn [{:keys [db]} [_ action node]]
    (let [{:keys [target-node parent-action base-path base-action target-position]} (actions/get-dialog-node-data node)
          list-to-path (if (actions/node-parallel? parent-action)
                         {:type "parallel",
                          :data (vec (concat (:data parent-action) [action]))}
                         {:type "parallel", :data [target-node action]})
          data-patch (-> base-action
                         (actions/replace-child list-to-path target-position)
                         (select-keys [:data]))]
      {:dispatch-n (list [::update-scene-action (actions/complete-path base-path) data-patch])})))

(re-frame/reg-event-fx
  ::add-new-empty-phrase-parallel-action
  (fn [{:keys [_]} [_ {:keys [node-data]}]]
    (let [current-target (-> (:data node-data) (defaults/get-inner-action) (get :target))
          new-action-data (cond-> defaults/default-action
                                  (some? current-target) (defaults/update-inner-action {:target current-target}))]
      {:dispatch [::add-new-phrase-parallel-action new-action-data node-data]})))

(re-frame/reg-event-fx
  ::add-new-scene-action
  (fn [{:keys [_]} [_ action relative-position node]]
    (let [{:keys [base-path base-action target-position]} (actions/get-dialog-node-data node)
          data-patch (-> base-action
                         (au/insert-child-action action target-position relative-position)
                         (select-keys [:data]))]
      {:dispatch-n (list [::update-scene-action base-path data-patch])})))

(re-frame/reg-event-fx
  ::insert-child-action
  (fn [{:keys [db]} [_ child-action position]]
    (let [dialog-action-info (translator-form.actions/current-dialog-action-info db)
          dialog-action-data (translator-form.actions/current-dialog-action-data db)

          dialog-action-path (:path dialog-action-info)
          data-patch (-> (au/insert-child-action-at-index dialog-action-data child-action position)
                         (select-keys [:data]))]

      {:dispatch [::update-scene-action dialog-action-path data-patch]})))

(re-frame/reg-event-fx
  ::append-child-action
  (fn [{:keys [_]} [_ child-action]]
    {:dispatch [::insert-child-action child-action :last]}))

(re-frame/reg-event-fx
  ::append-empty-phrase-action
  (fn [{:keys [_]} [_]]
    {:dispatch [::append-child-action defaults/default-action]}))

(re-frame/reg-event-fx
  ::append-empty-text-animation-action
  (fn [{:keys [_]} [_]]
    {:dispatch [::append-child-action defaults/text-animation-action]}))

(re-frame/reg-event-fx
  ::add-new-empty-phrase-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position] :or {relative-position :after}}]]
    (let [current-target (-> (:data node-data) (defaults/get-inner-action) (get :target))
          new-action-data (cond-> defaults/default-action
                                  (some? current-target) (defaults/update-inner-action {:target current-target}))]
      (if (= relative-position :parallel)
        {:dispatch [::add-new-phrase-parallel-action new-action-data node-data]}
        {:dispatch [::add-new-scene-action new-action-data relative-position node-data]}))))

(re-frame/reg-event-fx
  ::add-new-empty-text-animation-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position] :or {relative-position :after}}]]
    (let [new-action-data defaults/text-animation-action]
      {:dispatch [::add-new-scene-action new-action-data relative-position node-data]})))

(defn- get-exact-position
  [position relative-position]
  (case relative-position
    :before position
    :after (inc position)
    position))

(re-frame/reg-event-fx
  ::insert-effect-action
  (fn [{:keys [_]} [_ {:keys [effect-id position relative-position] :or {relative-position :exact}}]]
    (let [effect-action-data (defaults/get-effect-action-data {:action-name effect-id})
          exact-position (get-exact-position position relative-position)]
      {:dispatch [::insert-child-action effect-action-data exact-position]})))

(re-frame/reg-event-fx
  ::add-effect-action
  (fn [{:keys [_]} [_ {:keys [effect node-data relative-position] :or {relative-position :after}}]]
    (let [effect-action-data (defaults/get-effect-action-data {:action-name effect})]
      {:dispatch (if (= relative-position :parallel)
                   [::add-new-phrase-parallel-action effect-action-data node-data]
                   [::add-new-scene-action effect-action-data relative-position node-data])})))

(re-frame/reg-event-fx
  ::add-new-phrase-concept-action
  (fn [{:keys [_]} [_ relative-position node action-data]]
    (let [{:keys [base-path base-action target-position]} (actions/get-dialog-node-data node)
          concept-var (:concept-var base-action)
          field-name (actions/unique-var-name)
          empty-action {:type "action", :from-var [{:var-name concept-var, :var-property field-name}]}
          concept-schema {:name     field-name
                          :type     "action"
                          :template defaults/default-concept-action}
          action (cond-> defaults/default-concept-action
                         (some? action-data) (defaults/update-inner-concept-action action-data))

          data-patch (-> base-action
                         (au/insert-child-action empty-action target-position relative-position)
                         (select-keys [:data]))]
      {:dispatch-n (list
                     [::dialog-form.concepts/add-concepts-schema-fields concept-schema]
                     [::translator-form.concepts/update-current-concept [(keyword field-name)] action]
                     [::update-scene-action base-path data-patch])})))

(re-frame/reg-event-fx
  ::add-new-empty-phrase-concept-action
  (fn [{:keys [_]} [_ {:keys [node-data relative-position] :or {relative-position :after}}]]
    (let [current-target (-> (:data node-data) (defaults/get-inner-action) (get :target))
          new-action-data (cond-> {}
                                  (some? current-target) (assoc :target current-target))]
      {:dispatch [::add-new-phrase-concept-action relative-position node-data new-action-data]})))

(re-frame/reg-event-fx
  ::delete-phrase-action
  (fn [{:keys [db]} [_ node]]
    (let [{:keys [concept-action? base-path parent-action base-action target-position item-position]} (actions/get-dialog-node-data node)]
      (if (and (actions/node-parallel? parent-action) (not= 0 item-position))
        (let [parent-action (assoc parent-action :data (vec (:data parent-action)))
              parallel-data (-> parent-action
                                (au/delete-child-action item-position))
              parallel-data (if (= (count (:data parallel-data)) 1) (first (:data parallel-data)) parallel-data)
              data-patch (-> base-action
                             (actions/replace-child parallel-data target-position)
                             (select-keys [:data]))]
          {:dispatch-n (list [::update-scene-action base-path data-patch])})
        (let [data-patch (-> base-action
                             (au/delete-child-action target-position)
                             (select-keys [:data]))]
          (if concept-action?
            (let [var-name (get-in node [:action-node-data :data :from-var 0 :var-property])]
              {:dispatch-n (list
                             [::dialog-form.concepts/remove-concepts-schema-field var-name]
                             [::dialog-form.concepts/remove-var-from-concepts var-name]
                             [::update-scene-action base-path data-patch])})
            {:dispatch-n (list [::update-scene-action base-path data-patch])}))))))

(re-frame/reg-event-fx
  ::update-action-by-path
  (fn [{:keys [_]} [_ {:keys [action-path action-type data-patch]}]]
    {:dispatch [(case action-type
                  :concept ::translator-form.concepts/update-current-concept
                  :scene ::translator-form.scene/update-action)
                action-path
                data-patch]}))

(re-frame/reg-event-fx
  ::update-empty-action-by-path
  (fn [{:keys [_]} [_ {:keys [action-path action-type data-patch]}]]
    {:dispatch [::update-action-by-path {:action-path (concat action-path defaults/empty-action-path)
                                         :action-type action-type
                                         :data-patch  data-patch}]}))

(re-frame/reg-event-fx
  ::update-inner-action-by-path
  (fn [{:keys [_]} [_ {:keys [action-path action-type data-patch]}]]
    {:dispatch [::update-action-by-path {:action-path (concat action-path defaults/inner-action-path)
                                         :action-type action-type
                                         :data-patch  data-patch}]}))

(re-frame/reg-event-fx
  ::update-inner-action
  (fn [{:keys [db]} [_ data-patch position]]
    (let [{:keys [path type]} (translator-form.actions/current-phrase-action-info db)
          action-path (concat (au/node-path->action-path path) [:data position])]
      {:dispatch [::update-action-by-path {:action-path action-path
                                           :action-type (cond
                                                          (= type :concept-action) :concept
                                                          (= type :scene-action) :scene
                                                          :else type)
                                           :data-patch  data-patch}]})))

(re-frame/reg-event-fx
  ::set-phrase-action-offset
  (fn [{:keys [_]} [_ offset]]
    {:dispatch-n (list [::update-inner-action {:duration (float offset)} 0])}))

(re-frame/reg-event-fx
  ::set-phrase-action-volume
  (fn [{:keys [_]} [_ volume]]
    {:dispatch-n (list [::update-inner-action {:volume (float volume)} 1])}))

(defn get-action-path-data
  ([db target-action]
   (get-action-path-data db target-action dialog-sub-path))
  ([db target-action sub-path]
   (translator-form.actions/get-action-path-data db target-action sub-path)))

(re-frame/reg-event-fx
  ::update-dialog-audio-action
  (fn [{:keys [db]} [_ target-action data-patch]]
    (logger/trace "update dialog audio" target-action data-patch)
    (let [[action-path type] (get-action-path-data db target-action)]
      (cond
        (= type :concept-action) {:dispatch-n (list [::translator-form.concepts/update-current-concept action-path data-patch])}
        (= type :scene-action) {:dispatch-n (list [::translator-form.scene/update-action action-path data-patch])}))))

(re-frame/reg-event-fx
  ::set-phrase-dialog-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list
                   [::update-dialog-audio-action :phrase {:audio audio-url}]
                   [::translator-form.actions/update-phrase-region-data {:audio-url audio-url
                                                                         :sub-path  dialog-sub-path
                                                                         :force?    true}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio-region
  (fn [{:keys [db]} [_ audio-url start duration]]
    (let [region-data {:audio    audio-url
                       :start    start
                       :duration duration}]
      {:dispatch
       (if (translator-form.actions/current-phrase-action-animation-sequence? db :phrase dialog-sub-path)
         [::load-lip-sync-data audio-url start duration region-data]
         [::update-dialog-audio-action :phrase region-data]
         )})))

(re-frame/reg-event-fx
  ::load-lip-sync-data
  (fn [{:keys [db]} [_ audio-url start duration rest-data]]
    {:db         (assoc-in db [:loading :load-lip-sync-data] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/actions/get-talk-animations")
                  :url-params      {:file     audio-url
                                    :start    start
                                    :duration duration}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-lip-sync-data-success rest-data]
                  :on-failure      [::load-lip-sync-data-failure rest-data start duration]}}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-success
  (fn [{:keys [_]} [_ rest-data lip-sync-data]]
    {:dispatch-n (list [:complete-request :load-lip-sync-data]
                       [::update-dialog-audio-action :phrase (merge rest-data {:data lip-sync-data})])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ rest-data start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list [:api-request-error :load-lip-sync-data]
                         [::translator-form.actions/update-metadata :lip-not-sync true]
                         [::update-dialog-audio-action :phrase (merge rest-data {:data default-lip-sync-data})])})))

(re-frame/reg-event-fx
  ::set-phrase-action-phrase
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-inner-action {:phrase-text text} 1])}))

(re-frame/reg-event-fx
  ::set-phrase-action-phrase-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-inner-action {:phrase-text-translated text} 1])}))

(re-frame/reg-event-fx
  ::set-phrase-action-target
  (fn [{:keys [_]} [_ target]]
    {:dispatch-n (list [::update-inner-action {:target target} 1]
                       [::translator-form.audios/set-current-target target])}))
