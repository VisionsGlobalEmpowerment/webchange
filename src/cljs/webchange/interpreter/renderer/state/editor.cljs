(ns webchange.interpreter.renderer.state.editor
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]))

(def editor-objects (atom {}))
(defonce select-object-handlers (atom {}))
(defonce update-object-handlers (atom {}))

(defn register-select-object-handler
  [stage-id key handler]
  (swap! select-object-handlers assoc key {:stage-id stage-id
                                           :handler  handler}))

(defn register-update-object-handler
  [key handler]
  (swap! update-object-handlers assoc key handler))

(defn hide-frames
  []
  (doseq [[_name {hide :hide}] @editor-objects]
    (hide)))

(defn show-frames
  []
  (doseq [[_name {show :show}] @editor-objects]
    (show)))

(defn- object-name->wrapper-name
  [object-name]
  (if-not (nil? object-name)
    (-> object-name (name) (str "-editor") (keyword))
    nil))

(defn selected-object [db] (get-in db (path-to-db [:editor :selected-object])))

(re-frame/reg-sub
  ::selected-object
  (fn [db]
    (get-in db (path-to-db [:editor :selected-object]))))

(re-frame/reg-event-fx
  ::register-object
  (fn [{:keys [_]} [_ object-wrapper]]
    (swap! editor-objects assoc (:name object-wrapper) object-wrapper)
    (let [wrapper-name (object-name->wrapper-name (:name object-wrapper))]
      {:dispatch [::scene/register-object (assoc object-wrapper :name wrapper-name)]})))

(re-frame/reg-event-fx
  ::select-object
  (fn [{:keys [db]} [_ current-target {:keys [stage-id]}]]
    (let [previous-target (get-in db (path-to-db [:editor :selected-object]))
          handlers (->> @select-object-handlers
                        (map second)
                        (filter (fn [handler-data]
                                  (= stage-id (:stage-id handler-data))))
                        (map #(:handler %))
                        (map #(conj % current-target))
                        (into []))]
      {:db            (assoc-in db (path-to-db [:editor :selected-object]) current-target)
       :dispatch-n    handlers
       :select-object [(->> previous-target (object-name->wrapper-name) (scene/get-scene-object db))
                       (->> current-target (object-name->wrapper-name) (scene/get-scene-object db))]})))

(re-frame/reg-fx
  :select-object
  (fn [[previous-target current-target]]
    (when previous-target (let [{:keys [deselect]} previous-target] (deselect)))
    (when current-target (let [{:keys [select]} current-target] (select)))))

(re-frame/reg-fx
  :deselect-object
  (fn [previous-target]
    (when previous-target (let [{:keys [deselect]} previous-target] (deselect)))))

(re-frame/reg-event-fx
  ::deselect-object
  (fn [{:keys [db]} [_]]
    (let [previous-target (get-in db (path-to-db [:editor :selected-object]))]
      {:deselect-object (->> previous-target (object-name->wrapper-name) (scene/get-scene-object db))})))

(re-frame/reg-event-fx
  ::update-selected-object
  (fn [{:keys [db]} [_ state]]
    (let [target (get-in db (path-to-db [:editor :selected-object]))
          handlers (->> @update-object-handlers
                        (map second)
                        (map #(conj % target state))
                        (into []))]
      {:dispatch-n handlers})))
