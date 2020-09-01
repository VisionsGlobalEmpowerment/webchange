(ns webchange.interpreter.object-data.group-params
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- prepare-action-data
  [action event]
  (let [event-param-name (keyword (:pick-event-param action))
        params (if event-param-name
                 (merge (:params action) (hash-map event-param-name (get event event-param-name)))
                 (:params action))]
    (merge action {:params params})))

(defn- prepare-action
  [action]
  (let [type (:on action)]
    (if (= type "click")
      {:on-click #(re-frame/dispatch [::ce/execute-action action])}
      {(keyword (str "on-" (:on action))) #(re-frame/dispatch [::ce/execute-action (prepare-action-data action %)])})))

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
      (assoc object :listening false))))

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

(defn- with-scale
  [object]
  (cond-> object
    (contains? object :scale-x) (assoc-in [:scale :x] (:scale-x object))
    (contains? object :scale-y) (assoc-in [:scale :y] (:scale-y object))))

(defn with-group-params
  [object]
  (-> object
      prepare-actions
      with-origin-offset
      with-transition
      with-draggable
      with-scale))
