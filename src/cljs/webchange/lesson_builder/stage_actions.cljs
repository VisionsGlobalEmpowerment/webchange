(ns webchange.lesson-builder.stage-actions
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions-spec :as spec]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.list :as list-utils]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as utils]))

;; actions

(defn- remove-action
  [activity-data {:keys [action-path]}]
  (let [parent-path (concat [:actions] (butlast action-path))
        removed-action-idx (last action-path)
        updated-activity-data (cond
                                (number? removed-action-idx) (update-in activity-data parent-path list-utils/remove-at-position removed-action-idx)
                                (keyword? removed-action-idx) (update-in activity-data parent-path dissoc removed-action-idx))

        ;; try to simplify parent action
        container-action-path (butlast parent-path)
        container-action-data (get-in updated-activity-data container-action-path)
        last-action-in-parallel? (and (= "parallel" (:type container-action-data)) (> 2 (-> container-action-data :data count)))
        updated-activity-data (if last-action-in-parallel?
                                (let [child-action (-> container-action-data :data last)
                                      container-parent-path (butlast container-action-path)
                                      container-idx (last container-action-path)]
                                  (if (some? child-action)
                                    (update-in activity-data container-parent-path list-utils/replace-at-position child-action container-idx)
                                    (update-in activity-data container-parent-path list-utils/remove-at-position container-idx)))
                                updated-activity-data)]
    updated-activity-data))

(defn- split-position
  [path]
  (let [path (vec path)
        position (last path)
        path (butlast path)]
    [path position]))

(defn- conj-position
  [path position]
  (-> (vec path)
      (conj position)))

(defn- insert-action
  [activity-data {:keys [action-data parent-data-path position]}]
  (let [update-path (concat [:actions] parent-data-path)
        updated-activity-data (update-in activity-data update-path list-utils/insert-at-position action-data position)]
    updated-activity-data))

(re-frame/reg-event-fx
  ::remove-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path] :as props}]]
    {:pre [(s/valid? ::spec/action-path action-path)]}
    (let [updated-activity-data (remove-action activity-data props)]
      {:dispatch-n [[::state/set-activity-data updated-activity-data]
                    [::script-state/set-selected-action nil]]})))

(re-frame/reg-event-fx
  ::insert-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-data parent-data-path position] :as props}]]
    {:pre [(s/valid? ::spec/action-data action-data)
           (s/valid? ::spec/action-path parent-data-path)
           (s/valid? ::spec/position position)]}
    {:dispatch [::state/set-activity-data (insert-action activity-data props)]}))

(re-frame/reg-event-fx
  ::move-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [source-action-path target-action-path]}]]
    {:pre [(s/valid? ::spec/action-path source-action-path)
           (s/valid? ::spec/action-path target-action-path)]}
    (let [source-action-data (utils/get-action activity-data source-action-path)
          [target-action-path target-action-position] (split-position target-action-path)
          [source-action-path source-action-position] (split-position source-action-path)
          source-action-position (if (and (= target-action-path source-action-path)
                                          (< target-action-position source-action-position))
                                   (inc source-action-position)
                                   source-action-position)
          updated-activity-data (-> activity-data
                                    (insert-action {:action-data      source-action-data
                                                    :parent-data-path target-action-path
                                                    :position         target-action-position})
                                    (remove-action {:action-path (conj-position source-action-path source-action-position)}))]
      {:dispatch-n [[::state/set-activity-data updated-activity-data]
                    [::script-state/set-selected-action nil]]})))

(re-frame/reg-event-fx
  ::update-empty-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path data-patch]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/data data-patch)]}
    (let [update-path (concat [:actions] action-path action-utils/empty-action-path)
          updated-activity-data (update-in activity-data update-path merge data-patch)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::update-inner-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path data-patch]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/data data-patch)]}
    (let [update-path (concat [:actions] action-path action-utils/inner-action-path)
          updated-activity-data (update-in activity-data update-path merge data-patch)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::set-action-target
  (fn [_ [_ {:keys [action-path target]}]]
    {:pre [(s/valid? ::spec/action-target target)]}
    {:dispatch [::update-inner-action {:action-path action-path
                                       :data-patch  {:target target}}]}))

(re-frame/reg-event-fx
  ::set-action-phrase-text
  (fn [_ [_ {:keys [action-path phrase-text]}]]
    {:pre [(s/valid? ::spec/text phrase-text)]}
    {:dispatch [::update-inner-action {:action-path action-path
                                       :data-patch  {:phrase-text phrase-text}}]}))

(re-frame/reg-event-fx
  ::set-action-phrase-audio
  (fn [_ [_ {:keys [action-path audio-url]}]]
    {:pre [(s/valid? ::spec/url audio-url)]}
    {:dispatch [::update-inner-action {:action-path action-path
                                       :data-patch  {:audio audio-url}}]}))

(re-frame/reg-event-fx
  ::set-action-phrase-region
  (fn [_ [_ {:keys [action-path region]}]]
    {:dispatch [::update-inner-action {:action-path action-path
                                       :data-patch  region}]}))

(re-frame/reg-event-fx
  ::set-action-phrase-volume
  (fn [_ [_ {:keys [action-path volume]}]]
    {:pre [(s/valid? ::spec/number volume)]}
    {:dispatch [::update-inner-action {:action-path action-path
                                       :data-patch  {:volume volume}}]}))

(re-frame/reg-event-fx
  ::toggle-action-tag
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path tag]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/action-tag tag)]}
    (let [action-data (utils/get-action activity-data action-path)
          action-tags (get action-data :tags [])
          new-tags (if (some #{tag} action-tags)
                     (->> action-tags (remove #(= % tag)) (vec))
                     (-> action-tags (conj tag) (distinct) (vec)))
          update-path (concat [:actions] action-path [:tags])
          updated-activity-data (assoc-in activity-data update-path new-tags)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

;; assets

(re-frame/reg-event-fx
  ::add-asset
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ asset-data]]
    {:pre [(s/valid? ::spec/data asset-data)]}
    (let [updated-activity-data (update activity-data :assets conj asset-data)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::remove-asset
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [asset-url]}]]
    {:pre [(s/valid? ::spec/url asset-url)]}
    (let [updated-activity-data (update activity-data :assets list-utils/remove-by-predicate #(= (:url %) asset-url) merge)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))


(re-frame/reg-event-fx
  ::update-asset
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [asset-url data-patch]}]]
    {:pre [(s/valid? ::spec/url asset-url)
           (s/valid? ::spec/data data-patch)]}
    (let [updated-activity-data (update activity-data :assets list-utils/update-by-predicate #(= (:url %) asset-url) merge data-patch)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

;; objects

(re-frame/reg-event-fx
  ::change-background
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ background-data]]
    {:pre [(s/valid? ::spec/background-data background-data)]}
    (let [[name] (utils/get-scene-background activity-data)
          updated-activity-data (update-in activity-data [:objects name] merge background-data)]
      ;; ToDo: update stage: change background
      ;; {:dispatch [::state-renderer/set-scene-object-state name background-data]}
      {:dispatch-n [[::state/set-activity-data updated-activity-data]
                    [::stage-state/reset]]})))

(re-frame/reg-event-fx
  ::set-object-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [object-name text chunks]}]]
    {:pre [(s/valid? ::spec/object-name object-name)
           (s/valid? ::spec/text text)
           (s/valid? ::spec/chunks chunks)]}
    (let [update-path [:objects (keyword object-name)]
          updated-activity-data (update-in activity-data update-path merge {:text text
                                                                            :chunks chunks})]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::toggle-object-visibility
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ object-name]]
    (let [object-key (keyword object-name)
          visible (-> (get-in activity-data [:objects object-key :visible] true) not)
          updated-activity-data (assoc-in activity-data [:objects object-key :visible] visible)]
      {:dispatch-n [[::state/set-activity-data updated-activity-data]
                    [::state-renderer/set-scene-object-state object-name {:visible visible}]]})))

(re-frame/reg-event-fx
  ::update-objects
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [objects assets]}]]
    (let [updated-activity-data (-> activity-data
                                    (update :objects merge objects)
                                    (update :assets concat assets))]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

;; Apply Template Options

(re-frame/reg-event-fx
  ::apply-template-options
  [(re-frame/inject-cofx :activity-info)]
  (fn [{:keys [activity-info]} [_ template-options {:keys [on-success on-failure]}]]
    (let [{:keys [id]} activity-info]
      {:dispatch [::warehouse/apply-activity-template-options
                  {:activity-id id
                   :data        template-options}
                  {:on-success [::apply-template-options-success on-success]
                   :on-failure [::apply-template-options-failure on-failure]}]})))

(re-frame/reg-event-fx
  ::apply-template-options-success
  (fn [{:keys [_]} [_ on-success {:keys [data]}]]
    {:dispatch-n (cond-> [[::state/reset-activity-data data]
                          [::stage-state/reset]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::apply-template-options-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))

;; call activity action

(re-frame/reg-event-fx
  ::call-activity-action
  [(re-frame/inject-cofx :activity-info)]
  (fn [{:keys [activity-info]} [_ {:keys [action data common-action?]
                                   :or   {common-action? false}}
                                {:keys [on-success on-failure]}]]
    (let [{:keys [id]} activity-info]
      {:dispatch [::warehouse/activity-template-action
                  {:activity-id id
                   :common-action? common-action?
                   :action         action
                   :data           data}
                  {:on-success [::call-activity-action-success on-success]
                   :on-failure [::call-activity-action-failure on-failure]}]})))

(re-frame/reg-event-fx
  ::call-activity-action-success
  (fn [{:keys [_]} [_ on-success {:keys [data]}]]
    {:dispatch-n (cond-> [[::state/reset-activity-data data]
                          [::stage-state/reset]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::call-activity-action-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))

;; Activity settings

(re-frame/reg-event-fx
  ::update-activity-settings
  [(re-frame/inject-cofx :activity-info)]
  (fn [{:keys [activity-info db]} [_ {:keys [data]} {:keys [on-success on-failure]}]]
    (let [{:keys [id]} activity-info]
      {:db       db
       :dispatch [::warehouse/activity-settings
                  {:activity-id id
                   :data        data}
                  {:on-success [::update-activity-settings-success on-success]
                   :on-failure [::update-activity-settings-failure on-failure]}]})))

(re-frame/reg-event-fx
  ::update-activity-settings-success
  (fn [{:keys [_]} [_ on-success {:keys [info data]}]]
    {:dispatch-n (cond-> [[::state/reset-activity-data data]
                          [::state/set-activity-info info]
                          [::stage-state/reset]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::update-activity-settings-failure
  (fn [{:keys [_]} [_ on-failure]]
    {:dispatch-n (cond-> []
                         (some? on-failure) (conj on-failure))}))
