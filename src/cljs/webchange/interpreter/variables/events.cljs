(ns webchange.interpreter.variables.events
  (:require
    [clojure.set :refer [union]]
    [re-frame.core :as re-frame]
    [webchange.common.events :as e]
    ))

(e/reg-simple-executor :dataset-var-provider ::execute-dataset-var-provider)
(e/reg-simple-executor :vars-var-provider ::execute-vars-var-provider)
(e/reg-simple-executor :test-var ::execute-test-var)
(e/reg-simple-executor :counter ::execute-counter)
(e/reg-simple-executor :set-variable ::execute-set-variable)

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

(defn unprocessed
  [db items provider-id]
  (let [processed (get-processed db provider-id)]
    (->> items
         (filter #(->> % :id (contains? processed) not)))))

(defn provide
  [db items variables provider-id]
  (let [new-items (->> (unprocessed db items provider-id)
                       shuffle
                       (take (count variables)))
        processed (->> new-items (map :id) (into #{}))
        vars (zipmap variables new-items)]
    (-> db
        (set-variables vars)
        (add-processed provider-id processed))))

(defn has-next
  [db items provider-id]
  (let [unprocessed (unprocessed db items provider-id)]
    (->> unprocessed
         count
         (< 0))))

(re-frame/reg-event-fx
  ::execute-set-variable
  (fn [{:keys [db]} [_ {:keys [var-name var-value] :as action}]]
    {:db (set-variable db var-name var-value)
     :dispatch (e/success-event action)}))

(re-frame/reg-event-fx
  ::execute-clear-vars
  (fn [{:keys [db]} [_ _]]
    (let [scene-id (:current-scene db)]
      {:db (-> db
               (update-in [:scenes scene-id] dissoc :variables)
               (update-in [:scenes scene-id] dissoc :providers))})))

(re-frame/reg-event-fx
  ::execute-vars-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id on-end] :as action}]]
    (let [items (->> from
                     (map #(get-variable db %)))
          has-next (has-next db items provider-id)
          scene-id (:current-scene db)
          on-end-action (->> on-end
                             keyword
                             (vector :scenes scene-id :actions)
                             (get-in db))]
      (if has-next
        {:db (provide db items variables provider-id)
         :dispatch (e/success-event action)}
        {:dispatch [::e/execute-action on-end-action]}))))

(re-frame/reg-event-fx
  ::execute-dataset-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id] :as action}]]
    (let [scene-id (:current-scene db)
          items (-> (get-in db [:scenes scene-id :datasets (keyword from)]) vals)]
      {:db (provide db items variables provider-id)
       :dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::execute-test-var
  (fn [{:keys [db]} [_ {:keys [var var-name property success fail] :as action}]]
    (let [test (get-variable db var-name)
          key (keyword property)
          success (e/get-action success db action)
          fail (e/get-action fail db action)]
      (if (= (key var) (key test))
        {:dispatch-n (list [::e/execute-action success] (e/success-event action))}
        {:dispatch-n (list [::e/execute-action fail] (e/success-event action))}))))

(re-frame/reg-event-fx
  ::execute-counter
  (fn [{:keys [db]} [_ {:keys [counter-action counter-id] :as action}]]
    (let [fn (case (keyword counter-action)
               :increase inc
               :decrease dec)]
      {:db (->> counter-id
               (get-variable db)
               fn
               (set-variable db counter-id))
       :dispatch (e/success-event action)})))