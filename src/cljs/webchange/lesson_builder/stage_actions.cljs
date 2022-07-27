(ns webchange.lesson-builder.stage-actions
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions-spec :as spec]
    [webchange.lesson-builder.tools.script.state :as script-state]
    [webchange.utils.list :as list-utils]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as utils]))

(re-frame/reg-event-fx
  ::change-background
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ background-data]]
    {:pre [(s/valid? ::spec/background-data background-data)]}
    (let [[name] (utils/get-scene-background activity-data)
          updated-activity-data (update-in activity-data [:objects name] merge background-data)]
      ;; ToDo: update stage: change background
      ;; {:dispatch [::state-renderer/set-scene-object-state name background-data]}
      {:dispatch [::state/set-activity-data updated-activity-data]})))

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

(re-frame/reg-event-fx
  ::remove-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path] :as props}]]
    {:pre [(s/valid? ::spec/action-path action-path)]}
    (let [updated-activity-data (remove-action activity-data props)]
      {:dispatch-n [[::state/set-activity-data updated-activity-data]
                    [::script-state/set-selected-action nil]]})))

(re-frame/reg-event-fx
  ::set-action-target
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path target]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/action-target target)]}
    (let [update-path (concat [:actions] action-path action-utils/inner-action-path [:target])
          updated-activity-data (assoc-in activity-data update-path target)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::set-action-phrase-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-path phrase-text]}]]
    {:pre [(s/valid? ::spec/action-path action-path)
           (s/valid? ::spec/action-target phrase-text)]}
    (let [update-path (concat [:actions] action-path action-utils/inner-action-path [:phrase-text])
          updated-activity-data (assoc-in activity-data update-path phrase-text)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

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

(defn- insert-action
  [activity-data {:keys [action-data parent-data-path position]}]
  (let [update-path (concat [:actions] parent-data-path)
        updated-activity-data (update-in activity-data update-path list-utils/insert-at-position action-data position)]
    updated-activity-data))

(re-frame/reg-event-fx
  ::insert-action
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [action-data parent-data-path position] :as props}]]
    {:pre [(s/valid? ::spec/action-data action-data)
           (s/valid? ::spec/action-path parent-data-path)
           (s/valid? ::spec/position position)]}
    {:dispatch [::state/set-activity-data (insert-action activity-data props)]}))

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
  ::set-object-text
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [object-name text]}]]
    {:pre [(s/valid? ::spec/object-name object-name)
           (s/valid? ::spec/text text)]}
    (let [update-path [:objects (keyword object-name) :text]
          updated-activity-data (assoc-in activity-data update-path text)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))

(re-frame/reg-event-fx
  ::update-asset
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ {:keys [asset-url data-patch]}]]
    {:pre [(s/valid? ::spec/url asset-url)
           (s/valid? ::spec/data data-patch)]}
    (let [updated-activity-data (update activity-data :assets list-utils/update-by-predicate #(= (:url %) asset-url) merge data-patch)]
      {:dispatch [::state/set-activity-data updated-activity-data]})))
