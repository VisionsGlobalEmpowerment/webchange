(ns webchange.interpreter.variables.events
  (:require
    [clojure.set :refer [union]]
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [webchange.common.events :as e]))

(e/reg-simple-executor :dataset-var-provider ::execute-dataset-var-provider)
(e/reg-simple-executor :lesson-var-provider ::execute-lesson-var-provider)
(e/reg-simple-executor :vars-var-provider ::execute-vars-var-provider)
(e/reg-simple-executor :test-var ::execute-test-var)
(e/reg-simple-executor :test-var-scalar ::execute-test-var-scalar)
(e/reg-simple-executor :test-var-list ::execute-test-var-list)
(e/reg-simple-executor :test-value ::execute-test-value)
(e/reg-simple-executor :case ::execute-case)
(e/reg-simple-executor :counter ::execute-counter)
(e/reg-simple-executor :calc ::execute-calc)
(e/reg-simple-executor :set-variable ::execute-set-variable)
(e/reg-simple-executor :set-progress ::execute-set-progress)
(e/reg-simple-executor :copy-variable ::execute-copy-variable)
(e/reg-simple-executor :clear-vars ::execute-clear-vars)
(e/reg-simple-executor :map-value ::execute-map-value)

(defn get-variable
  [db var-name]
  (let [scene-id (:current-scene db)]
    (get-in db [:scenes scene-id :variables var-name])))

(defn set-variable
  [db var-name value]
  (let [scene-id (:current-scene db)]
    (assoc-in db [:scenes scene-id :variables var-name] value)))

(defn set-progress
  [db var-name value]
  (assoc-in db [:progress-data :variables (keyword var-name)] value))

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

(defn filter-property-values
  [exclude-property-values items]
  (let [filter-map exclude-property-values
        key (-> filter-map keys first)
        filter-map-value (get filter-map key)
        filter-values (if (sequential? filter-map-value) filter-map-value [filter-map-value])]
    (filter (fn [item]
              (not (some (fn [filter-value] (= filter-value (get item key))) filter-values))) items)))

(defn provide
  ([db items variables provider-id]
   (provide db items variables provider-id {}))
  ([db items variables provider-id params]
   (let [new-items (cond->> (unprocessed db items provider-id)
                            (:exclude-values params) (remove (into #{} (:exclude-values params)))
                            (:exclude-property-values params) (filter-property-values (:exclude-property-values params))
                            (:limit params) (take (:limit params))
                            (:repeat params) (#(apply concat (repeat (:repeat params) %)))
                            (:shuffled params) shuffle
                            (:unique params) distinct
                            :always (take (count variables)))
         processed (->> new-items (map :id) (into #{}))
         vars (zipmap variables new-items)]
     (cond-> db
             provider-id (add-processed provider-id processed)
             :always (set-variables vars)))))

(defn has-next
  ([db items provider-id]
   (has-next db items provider-id {}))
  ([db items provider-id params]
   (let [unprocessed (cond->> (unprocessed db items provider-id)
                              (:exclude-values params) (remove (into #{} (:exclude-values params)))
                              (:exclude-property-values params) (filter-property-values (:exclude-property-values params)))]
     (->> unprocessed count (< 0)))))

(re-frame/reg-event-fx
  ::execute-set-variable
  (fn [{:keys [db]} [_ {:keys [var-name var-value] :as action}]]
    {:db (set-variable db var-name var-value)
     :dispatch (e/success-event action)}))

(re-frame/reg-event-fx
  ::execute-set-progress
  (fn [{:keys [db]} [_ {:keys [var-name var-value] :as action}]]
    {:db (set-progress db var-name var-value)
     :dispatch-n (list (e/success-event action) [:progress-data-changed])}))

(re-frame/reg-event-fx
  ::execute-copy-variable
  (fn [{:keys [db]} [_ {:keys [var-name from] :as action}]]
    (let [var-value (get-variable db from)]
      {:db (set-variable db var-name var-value)
       :dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::execute-map-value
  (fn [{:keys [db]} [_ {:keys [var-name value from to] :as action}]]
    (let [var-value (get to (.indexOf from value))]
      {:db (set-variable db var-name var-value)
       :dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::clear-vars
  (fn [{:keys [db]} [_ {:keys [keep-running]}]]
    (let [scene-id (:current-scene db)]
      {:db (-> db
               (update-in [:scenes scene-id] dissoc :variables)
               (update-in [:scenes scene-id] dissoc :providers)
               (assoc-in [:scenes scene-id :variables "status"] (if keep-running :running nil)))})))

(re-frame/reg-event-fx
  ::execute-clear-vars
  (fn [{:keys [db]} [_ action]]
    {:dispatch-n (list [::clear-vars {:keep-running true}] (e/success-event action))}))

(re-frame/reg-event-fx
  ::execute-vars-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id on-end] :as action}]]
    (let [items (->> from
                     (map (fn [var-name]
                            (cond-> (get-variable db var-name)
                                    provider-id (assoc :id var-name)))))
          has-next (has-next db items provider-id)
          scene-id (:current-scene db)
          on-end-action (->> on-end
                             keyword
                             (vector :scenes scene-id :actions)
                             (get-in db))]
      (if has-next
        {:db (provide db items variables provider-id action)
         :dispatch (e/success-event action)}
        {:dispatch [::e/execute-action on-end-action]}))))

(re-frame/reg-event-fx
  ::execute-dataset-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id] :as action}]]
    (let [scene-id (:current-scene db)
          items (-> (get-in db [:scenes scene-id :datasets (keyword from)]) vals)]
      {:db (provide db items variables provider-id {:shuffled true})
       :dispatch (e/success-event action)})))

(defn get-lesson
  [db lesson-set-name]
  (let [lesson (get-in db [:lessons lesson-set-name])]
    (when (nil? lesson) (throw (js/Error. (str "Lesson '" lesson-set-name "' is not defined"))))
    lesson))

(re-frame/reg-event-fx
  ::execute-lesson-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id on-end] :as action}]]
    (let [current-lesson-id (:activity-lesson db)
          course-lessons (get-in db [:course-data :lessons])
          lesson-set-name (-> (filter #(= current-lesson-id (:id %)) course-lessons)
                              first
                              :lesson-sets
                              (get (keyword from)))
          lesson (get-lesson db lesson-set-name)
          items (->> (:item-ids lesson)
                     (map #(get-in db [:dataset-items %]))
                     (map #(merge (:data %) {:id (:name %)})))
          has-next (has-next db items provider-id action)
          scene-id (:current-scene db)
          on-end-action (->> on-end
                             keyword
                             (vector :scenes scene-id :actions)
                             (get-in db))]
      (if has-next
        {:db       (provide db items variables provider-id action)
         :dispatch (e/success-event action)}
        {:dispatch [::e/execute-action on-end-action]}))))

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

(defn cond-action [db {:keys [flow-id action-id] :as action} params]
  (let [action-data (if (string? params) (e/get-action params db action) params)]
    (cond-> action-data
            flow-id (assoc :flow-id flow-id)
            action-id (assoc :action-id action-id)
            :always (e/with-prev action))))

(re-frame/reg-event-fx
  ::execute-test-var-scalar
  (fn [{:keys [db]} [_ {:keys [value var-name success fail] :as action}]]
    (let [test (get-variable db var-name)]
      (if (= value test)
        {:dispatch [::e/execute-action (cond-action db action success)]}
        {:dispatch [::e/execute-action (cond-action db action fail)]}))))

(re-frame/reg-event-fx
  ::execute-test-value
  [e/event-as-action e/with-vars]
  (fn [{:keys [db]} {:keys [value1 value2 success fail] :as action}]
    (if (= value1 value2)
      {:dispatch-n (list [::e/execute-action (cond-action db action success)])}
      (if fail
        {:dispatch-n (list [::e/execute-action (cond-action db action fail)] (e/success-event action))}
        {:dispatch-n (list (e/success-event action))}
        ))))

(re-frame/reg-event-fx
  ::execute-test-var-list
  (fn-traced [{:keys [db]} [_ {:keys [values var-names success fail] :as action}]]
    (let [test (map #(get-variable db %) var-names)]
      (if (= values test)
        {:dispatch-n (list [::e/execute-action (e/get-action success db action)] (e/success-event action))}

        (if fail
          {:dispatch-n (list [::e/execute-action (e/get-action fail db action)] (e/success-event action))}
          {:dispatch-n (list (e/success-event action))}
          )))))

(re-frame/reg-event-fx
  ::execute-case
  [e/event-as-action e/with-vars]
  (fn [{:keys [db]} {:keys [value options] :as action}]
    (let [success (get options (keyword value))]
      (if value
        {:dispatch-n (list [::e/execute-action success] (e/success-event action))}))))

(re-frame/reg-event-fx
  ::execute-counter
  (fn [{:keys [db]} [_ {:keys [counter-action counter-value counter-id] :as action}]]
    (let [fn (case (keyword counter-action)
               :increase inc
               :decrease dec
               :reset (constantly counter-value))]
      {:db (->> counter-id
               (get-variable db)
               fn
               (set-variable db counter-id))
       :dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::execute-calc
  (fn [{:keys [db]} [_ {:keys [var-name operation value-1 value-2] :as action}]]
    (let [fn (case (keyword operation)
               :div-floor (comp Math/floor /)
               :div-ceil (comp Math/ceil /))]
      {:db (set-variable db var-name (fn value-1 value-2))
       :dispatch (e/success-event action)})))
