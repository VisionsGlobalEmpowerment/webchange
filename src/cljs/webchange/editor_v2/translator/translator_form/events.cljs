(ns webchange.editor-v2.translator.translator-form.events
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-graph]]
    [webchange.editor-v2.translator.translator-form.utils-update-action :refer [get-action-update-data
                                                                                get-current-action-data]]
    [webchange.editor-v2.translator.translator-form.audio-assets.events :as audio-assets-events]
    [webchange.subs :as subs]))

(defn- get-current-concept
  [db]
  (let [concepts (editor-subs/course-dataset-items db)]
    (->> concepts (vals) (sort-by :name) (first))))

(defn- get-graph-data
  [db current-concept]
  (let [scene-data (subs/current-scene-data db)
        selected-phrase-node (editor-subs/current-action db)
        phrase-action-name (or (:origin-name selected-phrase-node)
                               (keyword (:name selected-phrase-node)))]
    (get-graph scene-data phrase-action-name {:current-concept current-concept})))

(defn- get-current-action
  [graph]
  (->> graph get-root-nodes first (get graph)))

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [current-concept (get-current-concept db)]
      {:dispatch-n (list [::audio-assets-events/init-state]
                         [::set-current-concept current-concept]
                         [::init-graph current-concept])})))

(re-frame/reg-event-fx
  ::set-graph
  (fn [{:keys [db]} [_ graph]]
    {:db (assoc-in db (path-to-db [:graph]) graph)}))

(re-frame/reg-event-fx
  ::set-concept-required
  (fn [{:keys [db]} [_ concept-required?]]
    {:db (assoc-in db (path-to-db [:concept-required?]) concept-required?)}))

(re-frame/reg-event-fx
  ::init-graph
  (fn [{:keys [db]} [_ current-concept]]
    (let [{:keys [graph has-concepts?]} (get-graph-data db current-concept)
          current-action (get-current-action graph)]
      {:dispatch-n (list [::set-graph graph]
                         [::set-concept-required has-concepts?]
                         [::set-current-selected-action current-action])})))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db (path-to-db [:selected-action]) action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys []} [_]]
    {:dispatch-n (list [::set-current-selected-action nil])}))

(re-frame/reg-event-fx
  ::reset-edited-data
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:edited-data]) {})}))

(re-frame/reg-event-fx
  ::set-action-edited-data
  (fn [{:keys [db]} [_ action-name action-id data]]
    {:db (assoc-in db (path-to-db [:edited-data :actions [action-name action-id]]) data)}))

(re-frame/reg-event-fx
  ::patch-dialog-action-edited-data
  (fn [{:keys [db]} [_ data-patch]]
    (let [data-store (translator-form-subs/edited-actions-data db)
          selected-phrase-node (editor-subs/current-action db)
          {:keys [id name type data]} (get-current-action-data selected-phrase-node nil data-store)]
      {:dispatch-n (list [::set-action-edited-data name id {:type type :data (merge data data-patch)}])})))

(re-frame/reg-event-fx
  ::set-dialog-action-description-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::patch-dialog-action-edited-data {:phrase-description-translated text}])}))

(re-frame/reg-event-fx
  ::patch-current-action-edited-data
  (fn [{:keys [db]} [_ data-patch]]
    (let [data-store (translator-form-subs/edited-actions-data db)
          current-concept (translator-form-subs/current-concept db)
          selected-action-node (translator-form-subs/selected-action db)
          scene-data (subs/current-scene-data db)
          original-action-name (-> selected-action-node :path first)
          {:keys [id name type data]} (get-action-update-data {:scene-data           scene-data
                                                               :data-store           data-store
                                                               :current-concept      current-concept
                                                               :selected-action-node selected-action-node
                                                               :original-action-name original-action-name
                                                               :data-patch           data-patch})]
      {:dispatch-n (list [::set-action-edited-data name id {:type type :data data}])})))

(re-frame/reg-event-fx
  ::set-current-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list [::patch-current-action-edited-data {:audio audio-url}])}))

(re-frame/reg-event-fx
  ::set-current-action-audio-region
  (fn [{:keys [_]} [_ audio-url start duration]]
    (let [region-data {:start    start
                       :duration duration}]
      {:dispatch-n (list [::patch-current-action-edited-data region-data]
                         [::load-lip-sync-data audio-url start duration])})))

(re-frame/reg-event-fx
  ::set-current-action-phrase-translated-text
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::patch-current-action-edited-data {:phrase-text-translated text}])}))

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
                       [::patch-current-action-edited-data {:data lip-sync-data}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list [:api-request-error :load-lip-sync-data]
                         [::patch-current-action-edited-data {:data default-lip-sync-data}])})))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ concept]]
    {:db (assoc-in db (path-to-db [:current-concept]) concept)
     :dispatch-n (list [::init-graph concept])}))

(re-frame/reg-event-fx
  ::reset-current-concept
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::set-current-concept nil])}))
