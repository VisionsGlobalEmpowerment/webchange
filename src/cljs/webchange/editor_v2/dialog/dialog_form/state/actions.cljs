(ns webchange.editor-v2.dialog.dialog-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer :as audio-analyzer]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :as defaults]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.logger.index :as logger]))

(def dialog-sub-path [:data 1])

;; Evenst
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
  ::add-new-phrase-action
  (fn [{:keys [_]} [_ action relative-position node]]
    (let [{:keys [base-path base-action target-position]} (actions/get-dialog-node-data node)
          data-patch (-> base-action
                         (au/insert-child-action action target-position relative-position)
                         (select-keys [:data]))]
      {:dispatch-n (list [::update-scene-action base-path data-patch])})))

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
  ::update-inner-action
  (fn [{:keys [db]} [_ data-patch position]]
    (let [{:keys [path type]} (translator-form.actions/current-phrase-action-info db)
          action-path (concat (au/node-path->action-path path) [:data position])]
      (cond
        (= type :concept-action) {:dispatch-n (list [::translator-form.concepts/update-current-concept action-path data-patch])}
        (= type :scene-action) {:dispatch-n (list [::translator-form.scene/update-action action-path data-patch])}))))

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
  (fn [{:keys [db]} [_ audio-url]]
    {:dispatch-n (list
                   [::update-dialog-audio-action :phrase {:audio audio-url}]
                   [::translator-form.actions/update-phrase-region-data audio-url dialog-sub-path true]
                   )}))

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
