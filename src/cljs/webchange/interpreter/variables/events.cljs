(ns webchange.interpreter.variables.events
  (:require
    [clojure.set :refer [union]]
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [webchange.common.events :as e]
    [webchange.state.lessons.subs :as lessons]
    [webchange.interpreter.variables.core :as core]))

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

(re-frame/reg-event-fx
  ::execute-set-variable
  (fn [{:keys [db]} [_ {:keys [var-name var-value] :as action}]]
    (core/set-variable! var-name var-value)
    {:dispatch (e/success-event action)}))

(re-frame/reg-event-fx
  ::execute-set-progress
  (fn [{:keys [db]} [_ {:keys [var-name var-value] :as action}]]
    {:db (core/set-progress db var-name var-value)
     :dispatch-n (list (e/success-event action) [:progress-data-changed])}))

(re-frame/reg-event-fx
  ::execute-copy-variable
  (fn [{:keys [db]} [_ {:keys [var-name from] :as action}]]
    (let [var-value (core/get-variable from)]
      (core/set-variable! var-name var-value)
      {:dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::execute-map-value
  (fn [{:keys [db]} [_ {:keys [var-name value from to] :as action}]]
    (let [var-value (get to (.indexOf from value))]
      (core/set-variable! var-name var-value)
      {:dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::clear-vars
  (fn [{:keys [db]} [_ {:keys [keep-running]}]]
    (core/clear-vars! keep-running)))

(re-frame/reg-event-fx
  ::execute-clear-vars
  (fn [{:keys [db]} [_ action]]
    (core/clear-vars! true)
    {:dispatch-n (list (e/success-event action))}))

(re-frame/reg-event-fx
  ::execute-vars-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id on-end] :as action}]]
    (let [items (->> from
                     (map (fn [var-name]
                            (cond-> (core/get-variable var-name)
                                    provider-id (assoc :id var-name)))))
          has-next (core/has-next items provider-id)
          scene-id (:current-scene db)
          on-end-action (->> on-end
                             keyword
                             (vector :scenes scene-id :actions)
                             (get-in db))]
      (if has-next
        (do
          (core/provide! items variables provider-id action)
          {:dispatch (e/success-event action)})
        {:dispatch [::e/execute-action on-end-action]}))))

;TODO: level get lessons from levels
(re-frame/reg-event-fx
  ::execute-lesson-var-provider
  (fn [{:keys [db]} [_ {:keys [from variables provider-id on-end] :as action}]]
    (let [items (lessons/lesson-dataset-items db from)
          has-next (core/has-next items provider-id action)
          scene-id (:current-scene db)
          on-end-action (->> on-end
                             keyword
                             (vector :scenes scene-id :actions)
                             (get-in db))]
      (if has-next
        (do
          (core/provide! items variables provider-id action)
          {:dispatch (e/success-event action)})
        {:dispatch [::e/execute-action on-end-action]}))))

(re-frame/reg-event-fx
  ::execute-test-var
  (fn [{:keys [db]} [_ {:keys [var var-name property success fail] :as action}]]
    (let [test (core/get-variable var-name)
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
    (let [test (core/get-variable var-name)]
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
        {:dispatch-n (list [::e/execute-action (cond-action db action fail)])}
        {:dispatch-n (list (e/success-event action))}
        ))))

(re-frame/reg-event-fx
  ::execute-test-var-list
  (fn-traced [{:keys [db]} [_ {:keys [values var-names success fail] :as action}]]
    (let [test (map core/get-variable var-names)]
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
                (core/get-variable)
                fn
                (core/set-variable! counter-id))
       :dispatch (e/success-event action)})))

(re-frame/reg-event-fx
  ::execute-calc
  (fn [{:keys [db]} [_ {:keys [var-name operation value-1 value-2] :as action}]]
    (let [fn (case (keyword operation)
               :div-floor (comp Math/floor /)
               :div-ceil (comp Math/ceil /))]
      (core/set-variable! var-name (fn value-1 value-2))
      {:dispatch (e/success-event action)})))
