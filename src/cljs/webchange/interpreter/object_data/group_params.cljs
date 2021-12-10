(ns webchange.interpreter.object-data.group-params
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- prepare-action-data
  [{:keys [id] :as action} event callback]
  (let [event-params-name (:pick-event-param action)
        params (if event-params-name
                 (->> (if (sequential? event-params-name) event-params-name [event-params-name])
                      (map keyword)
                      (select-keys event)
                      (merge (:params action)))
                 (:params action))]
    (merge action {:params       params
                   :display-name id
                   :user-event?  true
                   :callback     callback})))

(defn- prepare-action
  [action]
  (let [event-name (:on action)
        handler-name (->> (str "on-" event-name) (keyword))
        handler-options-name (->> (str "on-" event-name "-options") (keyword))]
    (cond-> {handler-name #(re-frame/dispatch [::ce/execute-action (prepare-action-data action %1 %2)])}
            (contains? action :options) (assoc handler-options-name (:options action)))))

(defn prepare-actions
  [{:keys [actions] :as object}]
  (let [has-actions? (->> actions keys nil? not)
        group? (-> object :type keyword (= :group))
        prepare-actions? (or has-actions? group?)]
    (if prepare-actions?
      (->> actions
           (map second)
           (map #(assoc % :var (:var object)))
           (map prepare-action)
           (into {})
           (merge object))
      object)))

(defn- with-origin-offset
  [{:keys [width height origin] :as object}]
  (let [{:keys [type]} origin]
    (case type
      "center-center" (-> object
                          (assoc :offset {:x (/ width 2) :y (/ height 2)}))
      "center-top" (-> object
                       (assoc :offset {:x (/ width 2)}))
      "center-bottom" (-> object
                          (assoc :offset {:x (/ width 2) :y height}))
      object)))

(defn- hack-on-click
  [{:keys [on-drag-move on-click] :as object}]
  (let [moved? (atom false)
        wrapped-move (fn []
                       (reset! moved? true)
                       (when on-drag-move
                         (on-drag-move)))
        wrapped-on-click (fn []
                           (when (and on-click (not @moved?))
                             (on-click))
                           (reset! moved? false))]
    (if on-click
      (assoc object
        :on-drag-move wrapped-move
        :on-click wrapped-on-click)
      object)))

(defn- with-draggable
  [{:keys [draggable] :as object}]
  (if draggable
    (-> object
        (assoc :draggable true)
        (hack-on-click))
    object))

(defn- with-collision
  [{:keys [actions collidable?] :as object}]
  (if collidable?
    (reduce (fn [object event-name]
              (let [{:keys [test pick-event-param]} (some (fn [[_ {:keys [on] :as event}]]
                                                            (and (= on event-name) event))
                                                          actions)
                    event-key (-> (str "on-" event-name) (keyword))]
                (assoc object event-key {:handler     (get object event-key)
                                         :test        test
                                         :pick-params (if (sequential? pick-event-param)
                                                        pick-event-param
                                                        [pick-event-param])})))
            object
            ["collide-enter" "collide-leave"])
    object))

(defn- with-scale
  [object]
  (-> (cond-> object
              (and (contains? object :scale-x)
                   (not (contains? object :scale))) (assoc-in [:scale :x] (:scale-x object))
              (and (contains? object :scale-y)
                   (not (contains? object :scale))) (assoc-in [:scale :y] (:scale-y object)))
      (dissoc :scale-x)
      (dissoc :scale-y)))

(defn with-group-params
  [object]
  (-> object
      prepare-actions
      with-origin-offset
      with-transition
      with-draggable
      with-collision
      with-scale))
