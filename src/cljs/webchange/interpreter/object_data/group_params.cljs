(ns webchange.interpreter.object-data.group-params
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- prepare-action-data
  [{:keys [id] :as action} event]
  (let [event-params-name (:pick-event-param action)
        params (if event-params-name
                 (->> (if (sequential? event-params-name) event-params-name [event-params-name])
                      (map keyword)
                      (select-keys event)
                      (merge (:params action)))
                 (:params action))]
    (merge action {:params       params
                   :display-name id})))

(defn- prepare-action
  [action]
  {(keyword (str "on-" (:on action))) #(re-frame/dispatch [::ce/execute-action (prepare-action-data action %)])})

(defn- prepare-actions
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

(defn- with-draggable
  [{:keys [draggable] :as object}]
  (if draggable
    (assoc object :draggable true)
    object))

(defn- with-collision
  [{:keys [actions collidable?] :as object}]
  (if collidable?
    (let [{:keys [test pick-event-param]} (some (fn [[_ {:keys [on test] :as event}]] (and (= on "collide") event)) actions)]
      (-> object
          (assoc :collide-test test)
          (assoc :pick-params (if (sequential? pick-event-param) pick-event-param [pick-event-param]))))
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
