(ns webchange.interpreter.variables.events
  (:require
    [clojure.set :refer [union]]
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.events :as events]
    ))

(defn get-variable
  [db var-name]
  (let [scene-id (:current-scene db)]
    (get-in db [:scenes scene-id :variables var-name])))

(defn set-variable
  [db var-name value]
  (let [scene-id (:current-scene db)]
    (assoc-in db [:scenes scene-id :variables var-name] value)))

(defn set-variables
  [db vars]
  (reduce (fn [db [k v]] (set-variable db k v)) db vars))

(defn get-processed
  [db provider-id]
  (let [scene-id (:current-scene db)]
    (get-in db [:scenes scene-id :providers provider-id :processed])))

(defn add-processed
  [db provider-id processed]
  (let [scene-id (:current-scene db)]
    (update-in db [:scenes scene-id :providers provider-id :processed] union processed)))

(defn provide
  [db items variables provider-id]
  (let [previously-processed (get-processed db provider-id)
        new-items (->> items
                   (filter #(->> % :id (contains? previously-processed) not))
                   shuffle
                   (take (count variables)))
        processed (->> new-items (map :id) (into #{}))
        vars (zipmap variables new-items)]
    (-> db
        (set-variables vars)
        (add-processed provider-id processed))))

(re-frame/reg-event-fx
  ::set-variable
  (fn [{:keys [db]} [_ var-name var]]
    {:db (set-variable db var-name var)}))

(re-frame/reg-event-fx
  ::execute-vars-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id]}]]
    (let [items (->> from
                     (map #(get-variable db %)))]
      {:db (provide db items variables provider-id)})))

(re-frame/reg-event-fx
  ::execute-dataset-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id]}]]
    (let [scene-id (:current-scene db)
          items (-> (get-in db [:scenes scene-id :datasets (keyword from)]) vals)]
      {:db (provide db items variables provider-id)})))

(re-frame/reg-event-fx
  ::execute-test-var
  (fn [{:keys [db]} [_ {:keys [var var-name property success] :as action}]]
    (let [scene-id (:current-scene db)
          scene (get-in db [:scenes scene-id])
          test (get-variable db var-name)
          key (keyword property)
          success (get-in scene [:actions (keyword success)])]
      (if (= (key var) (key test))
        {:dispatch [::events/execute-action success]}))))