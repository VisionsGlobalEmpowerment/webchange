(ns webchange.interpreter.renderer.state.scene
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]
    [webchange.logger :as logger]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:objects]) (atom {}))}))

(re-frame/reg-fx
  :add-scene-object
  (fn [{:keys [scene-objects object-wrapper]}]
    (let [object-name (:name object-wrapper)]
      (when (contains? @scene-objects object-name)
        (-> (str "Object with name " object-name " already exists.") js/Error. throw))
      (swap! scene-objects assoc object-name object-wrapper))))

(re-frame/reg-event-fx
  ::register-object
  (fn [{:keys [db]} [_ object-wrapper]]
    {:add-scene-object {:scene-objects  (get-in db (path-to-db [:objects]))
                        :object-wrapper object-wrapper}}))

;; Change object events

(re-frame/reg-event-fx
  ::set-scene-object-state
  (fn [{:keys [db]} [_ object-name state]]
    (let [available-actions {:set-position   [:x :y]
                             :set-scale      [:scale]
                             :set-visibility [:visible]}
          execute-actions (->> available-actions
                               (reduce (fn [result [action params]]
                                         (assoc result action (select-keys state params)))
                                       {})
                               (filter (fn [[_ params]] (-> params empty? not))))
          not-handled-params (clojure.set/difference (->> state (keys) (set))
                                                     (->> available-actions (vals) (apply concat) (set)))]
      (when-not (empty? not-handled-params)
        (logger/warn "not-processed-params:" (clj->js not-handled-params)))
      {:dispatch [::change-scene-object object-name execute-actions]})))

(re-frame/reg-event-fx
  ::change-scene-object
  (fn [{:keys [db]} [_ object-name actions]]
    (let [objects (get-in db (path-to-db [:objects]))
          object-wrapper (get @objects object-name)]
      (reduce (fn [result [action params]]
                (assoc result action [object-wrapper params]))
              {}
              actions))))

(re-frame/reg-fx
  :add-filter
  (fn [[object-wrapper filter-data]]
    ((:add-filter object-wrapper) filter-data)))

(re-frame/reg-fx
  :set-position
  (fn [[object-wrapper position]]
    ((:set-position object-wrapper) position)))

(re-frame/reg-fx
  :set-visibility
  (fn [[object-wrapper {:keys [visible]}]]
    ((:call object-wrapper) :set-visibility visible)))

(re-frame/reg-fx
  :set-value
  (fn [[object-wrapper value]]
    ((:call object-wrapper) :set-value value)))
