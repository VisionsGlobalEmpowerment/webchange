(ns webchange.editor-v2.dialog.dialog-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as au]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(def empty-action-position 0)
(def inner-action-position 1)

(def default-action {:type "sequence-data"
                     :data [{:type "empty" :duration 0}
                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]})

(def text-animation-action {:type "sequence-data"
                            :data [{:type "empty" :duration 0}
                                   {:type "text-animation", :phrase-text "New text animation", :audio nil}]})

(defn get-empty-action
  [action]
  (get-in action [:data empty-action-position]))

(defn get-inner-action
  [action]
  (get-in action [:data inner-action-position]))

(defn get-action [name]
  {:type "sequence-data"
   :data [{:type     "empty"
           :duration 0}
          {:type "action" :id name}]})

;; Subs

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

          concept-action {:type "sequence-data",
                          :data [default-action]}

          concept-schema {:name     field-name
                          :type     "action"
                          :template concept-action}

          list-to-path (if (actions/node-parallel? parent-action)
                         {:type "parallel",
                          :data (vec (concat (:data parent-action) [empty-action]))}
                         {:type "parallel", :data [(:data node) empty-action]})

          data-patch (-> base-action
                         (actions/replace-child list-to-path target-position)
                         (select-keys [:data]))]
      {:dispatch-n (list
                     [::dialog-form.concepts/add-concepts-schema-fields concept-schema]
                     [::translator-form.concepts/update-current-concept [(keyword field-name)] concept-action]
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
  (fn [{:keys [db]} [_ action relative-position node]]
    (let [{:keys [base-path base-action target-position]} (actions/get-dialog-node-data node)
          data-patch (-> base-action
                         (au/insert-child-action action target-position relative-position)
                         (select-keys [:data]))]
      {:dispatch-n (list [::update-scene-action base-path data-patch])})))

(re-frame/reg-event-fx
  ::add-new-phrase-concept-action
  (fn [{:keys [db]} [_ relative-position node]]
    (let [{:keys [base-path base-action target-position]} (actions/get-dialog-node-data node)
          concept-var (:concept-var base-action)
          field-name (actions/unique-var-name)
          empty-action {:type "action", :from-var [{:var-name concept-var, :var-property field-name}]}
          concept-action {:type "sequence-data",
                          :data [default-action]},
          concept-schema {:name     field-name
                          :type     "action"
                          :template concept-action}

          data-patch (-> base-action
                         (au/insert-child-action empty-action target-position relative-position)
                         (select-keys [:data]))]
      {:dispatch-n (list
                     [::dialog-form.concepts/add-concepts-schema-fields concept-schema]
                     [::translator-form.concepts/update-current-concept [(keyword field-name)] concept-action]
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
    {:dispatch-n (list [::update-inner-action {:duration (int offset)} 0])}))

(re-frame/reg-event-fx
  ::set-phrase-action-volume
  (fn [{:keys [_]} [_ volume]]
    {:dispatch-n (list [::update-inner-action {:volume (int volume)} 1])}))

(re-frame/reg-event-fx
  ::update-dialog-audio-action
  (fn [{:keys [db]} [_ target-action data-patch]]
    (let [{:keys [path type]} (cond
                                (= target-action :dialog) (translator-form.actions/current-dialog-action-info db)
                                (= target-action :phrase) (translator-form.actions/current-phrase-action-info db))
          action-path (concat (au/node-path->action-path path) [:data 1])]
      (cond
        (= type :concept-action) {:dispatch-n (list [::translator-form.concepts/update-current-concept action-path data-patch])}
        (= type :scene-action) {:dispatch-n (list [::translator-form.scene/update-action action-path data-patch])}))))

(re-frame/reg-event-fx
  ::set-phrase-dialog-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list [::update-dialog-audio-action :phrase {:audio audio-url}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio-region
  (fn [{:keys [_]} [_ audio-url start duration]]
    (let [region-data {:audio    audio-url
                       :start    start
                       :duration duration}]
      {:dispatch-n (list [::update-dialog-audio-action :phrase region-data]
                         [::load-lip-sync-data audio-url start duration])})))


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
                       [::update-dialog-audio-action :phrase {:data lip-sync-data}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list [:api-request-error :load-lip-sync-data]
                         [::update-dialog-audio-action :phrase {:data default-lip-sync-data}])})))

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
    {:dispatch-n (list [::update-inner-action {:target target} 1])}))
